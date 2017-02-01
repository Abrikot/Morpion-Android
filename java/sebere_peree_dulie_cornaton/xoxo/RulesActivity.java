package sebere_peree_dulie_cornaton.xoxo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Maxence on 08/12/2016.
 */

public class RulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {  // Méthode appelée lors de la création de l'activité
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);    // Définition du layout (affichage) à utiliser

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);  // Récupération de la Toolbar afin de la traiter
        toolbar.setTitle("");   // Suppression du titre de base
        setSupportActionBar(toolbar);   // Affectation de la Toolbar à la fenêtre
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {     // Appelé lors de la création de la Toolbar
        getMenuInflater().inflate(R.menu.main_menu, menu);  // Permet de récupérer le menu présent dans la Toolbar
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   // Appelé lors de l'appui sur un item du menu
        switch (item.getItemId())
        {
            case R.id.backButton:   // Si on appuie sur le boutton "Back"
                finish();           // On quitte l'activité en cours pour revenir à la précédente
                return true;
        }
        return false;
    }
}


