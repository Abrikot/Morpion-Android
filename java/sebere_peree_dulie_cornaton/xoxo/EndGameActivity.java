package sebere_peree_dulie_cornaton.xoxo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import sebere_peree_dulie_cornaton.xoxo.DataBase.MatchDataBaseManagement;
import sebere_peree_dulie_cornaton.xoxo.DataBase.ScoreDataBaseManagement;
import sebere_peree_dulie_cornaton.xoxo.GameController.Match;

/**
 * Created by Maxence on 04/01/2017.
 */

public class EndGameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        final Match match = (Match) getIntent().getSerializableExtra("match");
        TextView winnerTextView = (TextView) findViewById(R.id.winner_textview);
        int winner = match.getWinner();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int idMainUser = preferences.getInt("idMainUser", -1);
        int idSecondUser = preferences.getInt("idSecondUser", -1);
        ScoreDataBaseManagement scoreDb = new ScoreDataBaseManagement(this);
        scoreDb.open();
        if (winner == -2) // Draw
        {
            winnerTextView.setText(getString(R.string.draw));
            scoreDb.addScore(idMainUser, idSecondUser, 1, 1);
        }
        else // A player won the game
        {
            winnerTextView.setText(getString(R.string.endGame, match.getPlayerById(winner)));
            if (winner == idMainUser)
                scoreDb.addScore(idMainUser, idSecondUser, 2, 0);
            else if (winner == idSecondUser)
                scoreDb.addScore(idMainUser, idSecondUser, 0, 2);
        }
        scoreDb.close();


        Button backToTheMenu = (Button) findViewById(R.id.back_to_menu_button);
        backToTheMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchCompat saveSwitch = (SwitchCompat) findViewById(R.id.save_switch);
                if (saveSwitch.isChecked()) {
                    final MatchDataBaseManagement db = new MatchDataBaseManagement(getApplicationContext());
                    db.open();
                    db.addMatch(match);
                    db.close();
                }
                Intent intent = new Intent(EndGameActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        Button replayButton = (Button) findViewById(R.id.replay_button);
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchCompat saveSwitch = (SwitchCompat) findViewById(R.id.save_switch);
                if (saveSwitch.isChecked()) {
                    final MatchDataBaseManagement db = new MatchDataBaseManagement(getApplicationContext());
                    db.open();
                    db.addMatch(match);
                    db.close();
                }
                Intent intent = new Intent(EndGameActivity.this, BoardActivity.class);
                intent.putExtra("matchNumber", getIntent().getIntExtra("matchNumber", 0) + 1);
                startActivity(intent);
            }
        });

        Button lookAgainButton = (Button) findViewById(R.id.look_again);
        lookAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EndGameActivity.this, BoardActivity.class);
                intent.putExtra("lookAgainMatch", match);
                intent.putExtra("isLookingAgain", true);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EndGameActivity.this, MenuActivity.class);
        startActivity(intent);
    }
}
