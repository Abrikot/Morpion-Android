package sebere_peree_dulie_cornaton.xoxo.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * Created by Maxence on 07/01/2017.
 */

public class ScoreDataBaseManagement extends DataBaseManagement {
    private Context context;

    public ScoreDataBaseManagement(Context pContext) {
        super(pContext);
        this.context = pContext;
    }

    public void addScore(int idUser1, int idUser2, int score1, int score2) {
        Cursor cs = db.rawQuery("SELECT * FROM Score WHERE idUser1 = ? AND idUser2 = ?;", new String[]{idUser1 + "", idUser2 + ""});
        if (cs.moveToFirst()) {
            cs = db.rawQuery("UPDATE Score SET scoreUser1 = scoreUser1 + ?, scoreUser2 = scoreUser2 + ? WHERE idUser1 = ? AND idUser2 = ?", new String[]{score1 + "", score2 + "", idUser1 + "", idUser2 + ""});
            cs.moveToFirst();
            cs.close();
            UserDataBaseManagement userDB = new UserDataBaseManagement(context);
            userDB.open();
            userDB.addScore(idUser1, score1);
            userDB.addScore(idUser2, score2);
            userDB.close();
            return;
        }
        cs = db.rawQuery("SELECT * FROM Score WHERE idUser1 = ? AND idUser2 = ?;", new String[]{idUser2 + "", idUser1 + ""});
        if (cs.moveToFirst())
        {
            cs = db.rawQuery("UPDATE Score SET scoreUser1 = scoreUser1 + ?, scoreUser2 = scoreUser2 + ? WHERE idUser1 = ? AND idUser2 = ? OR idUser1 = ? AND idUser2 = ?", new String[]{score2 + "", score1 + "", idUser2 + "", idUser1 + ""});
            cs.moveToFirst();
            cs.close();
            UserDataBaseManagement userDB = new UserDataBaseManagement(context);
            userDB.open();
            userDB.addScore(idUser1, score1);
            userDB.addScore(idUser2, score2);
            userDB.close();
            return;
        }
        cs = db.rawQuery("INSERT INTO Score VALUES (?,?,?,?);", new String[]{idUser1 + "", score1 + "", idUser2 + "", score2 + ""});
        cs.moveToFirst();
        cs.close();

        UserDataBaseManagement userDB = new UserDataBaseManagement(context);
        userDB.open();
        userDB.addScore(idUser1, score1);
        userDB.addScore(idUser2, score2);
        userDB.close();
    }

    public int[] getScore(int idUser1, int idUser2) {
        int scores[] = new int[]{0, 0};
        Cursor cs = db.rawQuery("SELECT scoreUser1, scoreUser2 FROM Score WHERE idUser1 = ? AND idUser2 = ?;", new String[]{idUser1 + "", idUser2 + ""});
        int indexScore1 = cs.getColumnIndex("scoreUser1");
        int indexScore2 = cs.getColumnIndex("scoreUser2");
        if (cs.moveToFirst()) {
            scores[0] = cs.getInt(indexScore1);
            scores[1] = cs.getInt(indexScore2);
            Log.d("Scores récupérés", scores[0] + " ; " + scores[1]);
            cs.close();
            return scores;
        }
        else {
            cs = db.rawQuery("SELECT scoreUser1, scoreUser2 FROM Score WHERE idUser1 = ? AND idUser2 = ?;", new String[]{idUser2 + "", idUser1 + ""});
            indexScore1 = cs.getColumnIndex("scoreUser1");
            indexScore2 = cs.getColumnIndex("scoreUser2");
            if (cs.moveToFirst()) {
                scores[0] = -cs.getInt(indexScore2);
                scores[1] = -cs.getInt(indexScore1);
                Log.d("Scores récupérés", scores[0] + " ; " + scores[1]);
                cs.close();
                return scores;
            }
        }
        cs.close();
        return scores;
    }
}
