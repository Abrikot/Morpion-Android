package sebere_peree_dulie_cornaton.xoxo.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Maxence on 05/12/2016.
 */

abstract class DataBaseManagement {
    protected final static int version = 1;
    protected SQLiteDatabase db = null;
    protected DataBaseOpenHelper dbHelper= null;

    public DataBaseManagement(Context pContext)
    {
        this.dbHelper = new DataBaseOpenHelper(pContext, "database.db", null, version);
    }

    public SQLiteDatabase open()
    {
        db = dbHelper.getWritableDatabase();
        return db;
    }

    public void close()
    {
        db.close();
    }

    public SQLiteDatabase getDb()
    {
        return db;
    }
}
