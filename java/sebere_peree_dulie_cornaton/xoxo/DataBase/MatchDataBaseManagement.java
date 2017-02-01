package sebere_peree_dulie_cornaton.xoxo.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import sebere_peree_dulie_cornaton.xoxo.GameController.Match;

/**
 * Created by Maxence on 05/01/2017.
 */

public class MatchDataBaseManagement extends DataBaseManagement {
    Context context = null;

    public MatchDataBaseManagement(Context pContext) {
        super(pContext);
        this.context = pContext;
    }

    public void addMatch(Match match) {
        if (match.getId() != -1) {
            updateMatch(match);
            return;
        }
        int ids[] = match.getPlayersId();
        int id1 = ids[0];
        int id2 = ids[1];
        int winner = match.getWinner();
        ContentValues valuesMatch = new ContentValues();
        valuesMatch.put("idUser1", id1);
        valuesMatch.put("idUser2", id2);
        valuesMatch.put("winner", winner);
        db.insert("Match", null, valuesMatch);

        ArrayList moveList = match.getOrder();
        int matchId = getLastMatchId();
        for (int i = 0; i < moveList.size(); ++i) {
            if (matchId != -1) {
                Integer[] move = (Integer[]) moveList.get(i);
                ContentValues valuesMove = new ContentValues();
                valuesMove.put("idMatch", matchId);
                valuesMove.put("moveOrder", i);
                valuesMove.put("x", move[0]);
                valuesMove.put("y", move[1]);
                db.insert("Moves", null, valuesMove);
            }
        }

        Match test = getMatch(matchId);
    }

    public void updateMatch(Match match) {
        int idMatch = match.getId();
        int winner = match.getWinner();
        Cursor cs = db.rawQuery("UPDATE Match SET winner = ? WHERE id = ?", new String[]{winner + "", idMatch + ""});
        cs.moveToFirst();
        cs.close();
        ArrayList<Integer[]> moveList = match.getOrder();
        cs = db.rawQuery("SELECT max(moveOrder) maxOrder FROM Moves WHERE idMatch = ?", new String[]{idMatch + ""});
        int indexMaxOrder = cs.getColumnIndex("maxOrder");
        if (cs.moveToFirst()) {
            int maxOrder = cs.getInt(indexMaxOrder);
            if (maxOrder < moveList.size() - 1) {
                for (int i = maxOrder + 1; i < moveList.size(); ++i) {
                    cs = db.rawQuery("INSERT INTO Moves (idMatch, moveOrder, x, y) VALUES (?,?,?,?);", new String[]{idMatch + "", i + "", moveList.get(i)[0] + "", moveList.get(i)[1] + ""});
                    cs.moveToFirst();
                    cs.close();
                }
            }
        }
    }

    public void deleteAll() {
        db.delete("Moves", null, null);
        db.delete("Match", null, null);
    }

    public ArrayList<Match> getAllEntries() {
        ArrayList<Match> result = new ArrayList<Match>();
        Cursor cs = db.rawQuery("SELECT * FROM Match;", null);
        if (cs.moveToFirst()) {
            int indexId = cs.getColumnIndex("id");
            int indexIdUser1 = cs.getColumnIndex("idUser1");
            int indexIdUser2 = cs.getColumnIndex("idUser2");
            int indexWinner = cs.getColumnIndex("winner");

            do {
                int id = cs.getInt(indexId);
                int idUser1 = cs.getInt(indexIdUser1);
                int idUser2 = cs.getInt(indexIdUser2);
                int winner = cs.getInt(indexWinner);

                final UserDataBaseManagement db = new UserDataBaseManagement(context);
                db.open();
                String user1 = db.getName(idUser1);
                String user2 = db.getName(idUser2);
                if (user1 == null)
                    user1 = "User 1";
                if (user2 == null)
                    user2 = "User 2";
                db.close();

                Match match = new Match(id, winner, new int[]{idUser1, idUser2}, new String[]{user1, user2});
                result.add(match);
            } while (cs.moveToNext());
        }
        cs.close();
        return result;
    }

    public ArrayList<Match> getAllEntriesForPlayer(int idPlayer) {
        ArrayList<Match> result = new ArrayList<Match>();
        Cursor cs = db.rawQuery("SELECT * FROM Match WHERE idUser1 = ?;", new String[]{idPlayer + ""});
        if (cs.moveToFirst()) {
            int indexId = cs.getColumnIndex("id");
            int indexIdUser1 = cs.getColumnIndex("idUser1");
            int indexIdUser2 = cs.getColumnIndex("idUser2");
            int indexWinner = cs.getColumnIndex("winner");

            do {
                int id = cs.getInt(indexId);
                int idUser1 = cs.getInt(indexIdUser1);
                int idUser2 = cs.getInt(indexIdUser2);
                int winner = cs.getInt(indexWinner);

                final UserDataBaseManagement db = new UserDataBaseManagement(context);
                db.open();
                String user1 = db.getName(idUser1);
                String user2 = db.getName(idUser2);
                if (user1 == null)
                    user1 = "User 1";
                if (user2 == null)
                    user2 = "User 2";
                db.close();

                Match match = new Match(id, winner, new int[]{idUser1, idUser2}, new String[]{user1, user2});
                match.setMoves(getMovesForMatch(id));
                result.add(match);
            } while (cs.moveToNext());
        }
        cs.close();
        return result;
    }

    public int getLastMatchId() {
        final String[] projection = {"id"};
        Cursor cs = db.rawQuery("SELECT * FROM Match ORDER BY id DESC LIMIT 1", null);
        if (cs.moveToFirst()) {
            int indexId = cs.getColumnIndex("id");
            int tmp = cs.getInt(indexId);
            cs.close();
            return tmp;
        }
        cs.close();
        return -1;
    }

    public Match getMatch(int id) {
        Cursor csMatch = db.rawQuery("SELECT * FROM Match WHERE id = " + id + ";", null);
        if (csMatch.moveToFirst()) {
            int matchIdIndex = csMatch.getColumnIndex("id");
            int idUser1Index = csMatch.getColumnIndex("idUser1");
            int idUser2Index = csMatch.getColumnIndex("idUser2");
            int winnerIndex = csMatch.getColumnIndex("winner");

            int matchId = csMatch.getInt(matchIdIndex);
            int idUser1 = csMatch.getInt(idUser1Index);
            int idUser2 = csMatch.getInt(idUser2Index);
            int winner = csMatch.getInt(winnerIndex);

            final UserDataBaseManagement userDb = new UserDataBaseManagement(context);
            userDb.open();
            String user1 = userDb.getName(idUser1);
            String user2 = userDb.getName(idUser2);
            userDb.close();

            Match match = new Match(matchId, winner, new int[]{idUser1, idUser2}, new String[]{user1, user2});

            ArrayList<Integer[]> moves = getMovesForMatch(matchId);
            match.setMoves(moves);
            csMatch.close();
            return match;
        }
        csMatch.close();
        return null;
    }

    public ArrayList<Integer[]> getMovesForMatch(int idMatch) {
        ArrayList<Integer[]> moveList = new ArrayList<Integer[]>();
        Cursor cs = db.rawQuery("SELECT moveOrder, x, y FROM Moves WHERE idMatch = " + idMatch + ";", null);
        int indexMoveOrder = cs.getColumnIndex("moveOrder");
        int indexX = cs.getColumnIndex("x");
        int indexY = cs.getColumnIndex("y");
        while (cs.moveToNext()) {
            int moveOrder = cs.getInt(indexMoveOrder);
            int x = cs.getInt(indexX);
            int y = cs.getInt(indexY);

            moveList.add(new Integer[]{x, y, moveOrder});
        }
        cs.close();
        moveList = new SortList().sortList(moveList);
        return moveList;
    }

    class SortList {
        ArrayList<Integer[]> sortList(ArrayList<Integer[]> moveList) {
            int size = moveList.size();
            ArrayList<Integer[]> tmpList = new ArrayList<>();
            for (int i = 0; i < size; ++i) {
                for (int j = 0; j < size; ++j) {
                    if (j == i) {
                        Integer move[] = moveList.get(i);
                        tmpList.add(new Integer[]{move[0], move[1]});
                        break;
                    }
                }
            }
            return tmpList;
        }
    }
}
