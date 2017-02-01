package sebere_peree_dulie_cornaton.xoxo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sebere_peree_dulie_cornaton.xoxo.DataBase.UserDataBaseManagement;

/**
 * Created by Maxence on 05/12/2016.
 */

public class ConnectionActivity extends AppCompatActivity {
    boolean mainUserSelection;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        mainUserSelection = getIntent().getBooleanExtra("mainUser", true);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (!mainUserSelection) {
            ((TextView) findViewById(R.id.selectUser)).setText(R.string.secondUser);
            Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }

        LinearLayout layout = (LinearLayout) findViewById(R.id.connectionLayout);

        final UserDataBaseManagement db = new UserDataBaseManagement(this);
        db.open();
        ArrayList<User> userList = db.getAllEntries();
        if (userList.size() != 0)
        {
            for (final User user : userList)
            {
                if (mainUserSelection || preferences.getInt("idMainUser", -1) != user.getId()) {
                    Button button = new Button(this);
                    button.setText(user.getNickname());
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences.Editor editor = preferences.edit();
                            Intent intent;
                            if (mainUserSelection) {
                                int idActualMainUser = preferences.getInt("idMainUser", -1);
                                if (idActualMainUser != user.getId())
                                {
                                    editor.remove("scoreMainUser");
                                    editor.remove("idSecondUser");
                                    editor.remove("nicknameSecondUser");
                                    editor.remove("scoreSecondUser");
                                    editor.putInt("idMainUser", user.getId());
                                    editor.putString("nicknameMainUser", user.getNickname());
                                    editor.commit();
                                }
                                intent = new Intent(ConnectionActivity.this, MenuActivity.class);
                            } else {
                                int idActualSecondUser = preferences.getInt("idSecondUser", -1);
                                if (idActualSecondUser != user.getId())
                                {
                                    editor.remove("scoreMainUser");
                                    editor.remove("scoreSecondUser");
                                    editor.putInt("idSecondUser", user.getId());
                                    editor.putString("nicknameSecondUser", user.getNickname());
                                    editor.commit();
                                }
                                intent = new Intent(ConnectionActivity.this, BoardActivity.class);
                            }
                            startActivity(intent);
                        }
                    });
                    layout.addView(button);
                }
            }
        }
        db.close();

        ((LinearLayout)findViewById(R.id.mainConnectionLayout)).addView(getLayoutInflater().inflate(R.layout.fragment_selection_user, null));

        final EditText editText = (EditText)findViewById(R.id.newUser);
        final Button button = (Button)findViewById(R.id.newUserButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().length() != 0) {
                    db.open();
                    db.addUser(editText.getText().toString());
                    User newUser = db.getLastEntry(editText.getText().toString());
                    db.close();
                    if (newUser != null)
                    {
                        editText.setText("");
                        SharedPreferences.Editor editor = preferences.edit();
                        Intent intent;
                        if (mainUserSelection)
                        {
                            editor.putInt("idMainUser", newUser.getId());
                            editor.putString("nicknameMainUser", newUser.getNickname());
                            editor.commit();
                            intent = new Intent(ConnectionActivity.this, MenuActivity.class);
                        } else
                        {
                            editor.remove("scoreMainUser");
                            editor.remove("scoreSecondUser");
                            editor.putInt("idSecondUser", newUser.getId());
                            editor.putString("nicknameSecondUser", newUser.getNickname());
                            editor.commit();
                            intent = new Intent(ConnectionActivity.this, BoardActivity.class);
                        }
                        startActivity(intent);
                    }
                }
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
        TextView test = ((TextView) findViewById(R.id.selectUser));
        String string = (String)test.getText();
        if (string != getString(R.string.secondUser))
            finishAffinity();
        else
            finish();
    }
}
