package sebere_peree_dulie_cornaton.xoxo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Maxence on 04/12/2016.
 */

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        TextView welcome = (TextView) findViewById(R.id.welcome);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        welcome.setText(getString(R.string.welcome, preferences.getString("nicknameMainUser", "")));

        final Button newGameButton = (Button) findViewById(R.id.newGame);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ConnectionActivity.class);
                intent.putExtra("mainUser", false);
                startActivity(intent);
            }
        });

        final Button rulesButton = (Button) findViewById(R.id.rules);
        rulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, RulesActivity.class);
                startActivity(intent);
            }
        });

        final Button resumeButton = (Button) findViewById(R.id.resume);
        final int idUser = preferences.getInt("idMainUser", -1);
        if (idUser != -1)
        {
            int idMatch = preferences.getInt("unfinishedMatch" + idUser, -1);
            if (idMatch == -1)
                resumeButton.setVisibility(View.GONE);
        }
        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, BoardActivity.class);
                intent.putExtra("unfinished", true);
                startActivity(intent);
            }
        });

        final Button scoreButton = (Button) findViewById(R.id.score);
        scoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ScoresActivity.class);
                startActivity(intent);
            }
        });

        final Button historyButton = (Button)findViewById(R.id.archive);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, HistoryActivity.class);
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

        Intent intent = new Intent(MenuActivity.this, ConnectionActivity.class);
        startActivity(intent);
        finish();
    }
}
