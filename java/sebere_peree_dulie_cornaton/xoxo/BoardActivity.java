package sebere_peree_dulie_cornaton.xoxo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import sebere_peree_dulie_cornaton.xoxo.DataBase.MatchDataBaseManagement;
import sebere_peree_dulie_cornaton.xoxo.DataBase.ScoreDataBaseManagement;
import sebere_peree_dulie_cornaton.xoxo.Fragments.BoardFragment;
import sebere_peree_dulie_cornaton.xoxo.GameController.Match;

/**
 * Created by Maxence on 05/12/2016.
 */

public class BoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        final BoardFragment fragment = (BoardFragment) getFragmentManager().findFragmentById(R.id.boardFragment);


        Intent parameters = getIntent();
        String user1 = "User 1";
        String user2 = "User 2";
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int id1 = preferences.getInt("idMainUser", 0);
        int id2 = -1;
        if (getIntent().getBooleanExtra("isLookingAgain", false)) {
            final Match match = (Match) getIntent().getSerializableExtra("lookAgainMatch");
            String players[] = match.getPlayers();
            user1 = players[0];
            user2 = players[1];
            id2 = match.getPlayersId()[1];
            fragment.setMatch(match);
        } else {
            if (parameters.getBooleanExtra("unfinished", false)) {
                int idMatch = preferences.getInt("unfinishedMatch" + id1, -1);
                if (idMatch != -1) {
                    final MatchDataBaseManagement db = new MatchDataBaseManagement(getApplication());
                    db.open();
                    Match match = db.getMatch(idMatch);
                    db.close();
                    if (match != null) {
                        String players[] = match.getPlayers();
                        user1 = players[0];
                        user2 = players[1];
                        id2 = match.getPlayersId()[1];
                        fragment.setMatch(match);
                    }
                }
            } else {
                int matchNumber = parameters.getIntExtra("matchNumber", 0);
                user1 = preferences.getString("nicknameMainUser", "User 1");
                user2 = preferences.getString("nicknameSecondUser", "User 2");
                id2 = preferences.getInt("idSecondUser", 0);
                if (matchNumber % 2 == 0)
                    fragment.newMatch(id1, user1, id2, user2);
                else
                    fragment.newMatch(id2, user2, id1, user1);
            }
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("unfinishedMatch" + id1);
        editor.apply();

        ScoreDataBaseManagement scoreDb = new ScoreDataBaseManagement(this);
        scoreDb.open();
        int scores[] = scoreDb.getScore(id1, id2);
        Log.d("Scores dans Board", scores[0] + " " + scores[1]);
        scoreDb.close();
        int scoreMainUser = scores[0];
        int scoreSecondUser = scores[1];
        if (scoreMainUser < 0 || scoreSecondUser < 0)
        {
            int tmp = -scoreMainUser;
            scoreMainUser = -scoreSecondUser;
            scoreSecondUser = tmp;
        }

        TextView playerTurn = (TextView) findViewById(R.id.playerTurn);
        int matchNumber = parameters.getIntExtra("matchNumber", 0);
        if (matchNumber % 2 == 0)
            playerTurn.setText(user1 + " " + scoreMainUser + "-" + scoreSecondUser + " " + user2);
        else
            playerTurn.setText(user2 + " " + scoreSecondUser + "-" + scoreMainUser + " " + user1);

        final Button rulesButton = (Button) findViewById(R.id.rulesButton);
        rulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoardActivity.this, RulesActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backButton:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (!getIntent().getBooleanExtra("isLookingAgain", false)) {
            BoardFragment fragment = (BoardFragment) getFragmentManager().findFragmentById(R.id.boardFragment);
            Match match = fragment.getMatch();
            final MatchDataBaseManagement db = new MatchDataBaseManagement(this);
            db.open();
            db.addMatch(match);
            db.close();

            db.open();
            int idMatch = db.getLastMatchId();
            db.close();

            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            int idMainUser = preferences.getInt("idMainUser", -1);
            editor.putInt("unfinishedMatch" + idMainUser, idMatch);
            editor.commit();
            Intent intent = new Intent(BoardActivity.this, MenuActivity.class);
            startActivity(intent);
        } else {
            final BoardFragment fragment = (BoardFragment) getFragmentManager().findFragmentById(R.id.boardFragment);
            finish();
        }
    }

}
