package sebere_peree_dulie_cornaton.xoxo.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Maxence on 05/12/2016.
 */

class DataBaseOpenHelper extends SQLiteOpenHelper {

    // Table User
    private static final String createUserTable =
            "CREATE TABLE User (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nickname TEXT NOT NULL," +
                    "score INTEGER NOT NULL);";
    private static final String dropUserTable = "DROP TABLE IF EXISTS User;";

    // Table Match
    private static final String crezteMatchTable =
            "CREATE TABLE Match (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "idUser1 INTEGER NOT NULL," +
                    "idUser2 INTEGER NOT NULL," +
                    "winner INTEGER NOT NULL," +
                    "FOREIGN KEY(idUser1) REFERENCES User(id)," +
                    "FOREIGN KEY(idUser2) REFERENCES User(id));";
    private static final String dropMatchTable = "DROP TABLE IF EXISTS Match;";

    // Table Moves
    private static final String createMovesTable =
            "CREATE TABLE Moves (" +
                    "idMove INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "idMatch INTEGER NOT NULL," +
                    "moveOrder INTEGER NOT NULL," +
                    "x INTEGER NOT NULL," +
                    "y INTEGER NOT NULL," +
                    "FOREIGN KEY(idMatch) REFERENCES Match(id));";
    private static final String dropMoveTable = "DROP TABLE IF EXISTS Moves;";

    private static final String createScoreTable =
            "CREATE TABLE Score (" +
                    "idUser1 INTEGER NOT NULL," +
                    "scoreUser1 INTEGER NOT NULL," +
                    "idUser2 INTEGER NOT NULL," +
                    "scoreUser2 INTEGER NOT NULL," +
                    "FOREIGN KEY(idUser1) REFERENCES User(id)," +
                    "FOREIGN KEY(idUser2) REFERENCES User(id));";
    private static final String dropScoreTable = "DROP TABLE IF EXISTS Score;";

    public DataBaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createUserTable);
        db.execSQL(crezteMatchTable);
        db.execSQL(createMovesTable);
        db.execSQL(createScoreTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropUserTable);
        db.execSQL(dropMatchTable);
        db.execSQL(dropMoveTable);
        db.execSQL(dropScoreTable);
        onOpen(db);
    }
}
