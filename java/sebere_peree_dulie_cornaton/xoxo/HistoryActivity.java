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
import android.widget.LinearLayout;

import java.util.ArrayList;

import sebere_peree_dulie_cornaton.xoxo.DataBase.MatchDataBaseManagement;
import sebere_peree_dulie_cornaton.xoxo.GameController.Match;

/**
 * Created by Maxence on 07/01/2017.
 */

public class HistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        LinearLayout layout = (LinearLayout) findViewById(R.id.historyLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int idUser = preferences.getInt("idMainUser", -1);

        MatchDataBaseManagement db = new MatchDataBaseManagement(this);
        db.open();
        ArrayList<Match> matchList = db.getAllEntriesForPlayer(idUser);
        db.close();

        for (final Match match : matchList) {
            int idMatch = match.getId();
            String user1 = match.getPlayers()[0];
            String user2 = match.getPlayers()[1];
            int winner = match.getWinner();
            Button button = new Button(this);
            if (winner >= 0)
                button.setText(user1 + " vs " + user2 + "\n" + getString(R.string.endGame, match.getPlayerById(winner)));
            else if (winner == -2)
                button.setText(user1 + " vs " + user2 + "\n" + getString(R.string.draw));
            else if (winner == -1)
                button.setText(user1 + " vs " + user2);
            layout.addView(button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HistoryActivity.this, BoardActivity.class);
                    intent.putExtra("lookAgainMatch", match);
                    intent.putExtra("isLookingAgain", true);
                    startActivity(intent);
                }
            });
        }
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
                finish();
                return true;
        }
        return false;
    }

}
