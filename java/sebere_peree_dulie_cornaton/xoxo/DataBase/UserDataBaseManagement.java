package sebere_peree_dulie_cornaton.xoxo.DataBase;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import sebere_peree_dulie_cornaton.xoxo.User;

/**
 * Created by Maxence on 05/12/2016.
 */

public class UserDataBaseManagement extends DataBaseManagement {
    Context context = null;
    public UserDataBaseManagement(Context pContext)
    {
        super(pContext);
        this.context = pContext;
    }

    public void addUser(String nickname)
    {
        ContentValues values = new ContentValues();
        values.put("nickname", nickname);
        values.put("score", 0);
        db.insert("User", null, values);
    }

    public void delete(int id)
    {
        db.delete("User", "id = ?", new String[] {String.valueOf(id)});
    }

    public void deleteAll()
    {
        db.delete("User", null, null);
    }

    public ArrayList<User> getAllEntries()
    {
        ArrayList<User> result = new ArrayList<User>();
        Cursor cs = db.rawQuery("SELECT * FROM User", null);
        if (cs.moveToFirst())
        {
            int indexId = cs.getColumnIndex("id");
            int indexNickname = cs.getColumnIndex("nickname");
            int indexScore = cs.getColumnIndex("score");
            do {
                User tmp = new User(cs.getInt(indexId), cs.getString(indexNickname), cs.getInt(indexScore));
                result.add(tmp);
            } while (cs.moveToNext());
        }
        cs.close();
        return result;
    }

    public User getLastEntry(String name)
    {
        Cursor cs;
        if (name != null)
            cs = db.rawQuery("SELECT * FROM User WHERE nickname=? ORDER BY id DESC LIMIT 1", new String[]{name});
        else
            cs = db.rawQuery("SELECT * FROM User ORDER BY id DESC LIMIT 1", null);
        if (cs.moveToFirst())
        {
            int indexId = cs.getColumnIndex("id");
            int indexNickname = cs.getColumnIndex("nickname");
            int indexScore = cs.getColumnIndex("score");
            User tmp = new User(cs.getInt(indexId), cs.getString(indexNickname), cs.getInt(indexScore));
            cs.close();
            return tmp;
        }
        else
            return null;
    }

    public String getName(int id)
    {
        String result = null;
        Cursor cs = db.rawQuery("SELECT nickname FROM User WHERE id = " + id + ";", null);
        if (cs.moveToFirst())
        {
            int indexNickname = cs.getColumnIndex("nickname");
            result = cs.getString(indexNickname);
        }
        cs.close();
        return result;
    }

    public void setScore(int id, int score)
    {
        Cursor cs = db.rawQuery("UPDATE User SET score = ? WHERE id = ?;", new String[]{score + "", id + ""});
        cs.moveToFirst();
        cs.close();
    }

    public void addScore(int id, int score)
    {
        Log.d("Score ! ", "User " + id + " won " + score);
        Cursor cs = db.rawQuery("UPDATE User SET score = score + ? WHERE id = ?;", new String[]{score + "", id + ""});
        cs.moveToFirst();
        cs.close();
    }
}
