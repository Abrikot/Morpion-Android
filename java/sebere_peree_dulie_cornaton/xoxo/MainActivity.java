package sebere_peree_dulie_cornaton.xoxo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import sebere_peree_dulie_cornaton.xoxo.DataBase.UserDataBaseManagement;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Insérer un splash screen ici
        Intent intent = new Intent(MainActivity.this, ConnectionActivity.class);;
        startActivity(intent);
        this.finishAffinity();
    }

    private void resetDB()
    {
        final UserDataBaseManagement db = new UserDataBaseManagement(this);
        db.open();
        db.deleteAll();
        db.close();
    }
}
