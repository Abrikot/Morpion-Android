package sebere_peree_dulie_cornaton.xoxo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sebere_peree_dulie_cornaton.xoxo.DataBase.UserDataBaseManagement;

/**
 * Created by Maxence on 07/01/2017.
 */

public class ScoresActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        LinearLayout layout = (LinearLayout)findViewById(R.id.scoreLayout);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        UserDataBaseManagement db = new UserDataBaseManagement(this);
        db.open();
        ArrayList<User> userList = db.getAllEntries();
        db.close();

        User.sortUserListByScore(userList);

        for (User user : userList)
        {
            TextView text = new TextView(this);
            text.setText(user.getNickname() + " : " + user.getScore());
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            layout.addView(text);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.backButton:
                finish();
                return true;
        }
        return false;
    }
}
