package sebere_peree_dulie_cornaton.xoxo.GameController;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Maxence on 05/12/2016.
 */

public class Match implements Serializable {
    private int id = -1;
    private int winner = -1; // -1 indicates that the game is not finished. -2 indicates that this is a draw. Another number indicates the winner's id.
    private int[][] grid;
    private ArrayList<Integer[]> order;
    private int playersId[];
    private String players[];
    private int activePlayer = 0;

    // Constructeur
    public Match() {
    }

    public Match(int id1, String player1, int id2, String player2) {
        grid = new int[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                grid[i][j] = -1;
        order = new ArrayList();

        playersId = new int[2];
        playersId[0] = id1;
        playersId[1] = id2;

        players = new String[2];
        players[0] = player1;
        players[1] = player2;
        ;
    }

    public Match(int id, int winner, int[] playersId, String[] players) {
        this.id = id;
        this.winner = winner;
        this.playersId = playersId;
        this.players = players;

        grid = new int[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                grid[i][j] = -1;
        order = new ArrayList();
    }

    // Getters
    public int getId() {
        return id;
    }

    public int[][] getGrid() {
        return grid;
    }

    public ArrayList<Integer[]> getOrder() {
        return order;
    }

    public String getActivePlayer() {
        return players[activePlayer];
    }

    public int getWinner() {
        return winner;
    }

    public String[] getPlayers() {
        return players;
    }

    public int[] getPlayersId() {
        return playersId;
    }

    public String getPlayerById(int idPlayer)
    {
        if (idPlayer == playersId[0])
            return players[0];
        else if (idPlayer == playersId[1])
            return players[1];
        else
            return null;
    }

    // Setters
    public void setMoves(ArrayList<Integer[]> movesList) {
        for (int i = 0; i < movesList.size(); ++i) {
            Integer move[] = movesList.get(i);
            int x = move[0];
            int y = move[1];
            grid[x][y] = i % 2;
            order.add(move);
            activePlayer = (activePlayer + 1) % 2;
        }
    }

    public boolean setMove(int x, int y, int player) {
        if (isFree(x, y)) {
            grid[x][y] = player;
            Integer[] tmp = {x, y};
            order.add(tmp);
            return true;
        }
        return false;
    }

    public boolean setMoveActivePlayer(int x, int y) {
        if (isFree(x, y) && this.winner == -1 && isFinished() == -1) {
            grid[x][y] = activePlayer;
            Integer[] tmp = {x, y};
            order.add(tmp);
            activePlayer = (activePlayer + 1) % 2;
            return true;
        }
        return false;
    }

    // Other methods
    public boolean isFree(int x, int y) {
        if (grid[x][y] == -1)
            return true;
        return false;
    }

    public int isFinished() {

        for (int i = 0; i < 3; i++) // Is a row or a column full ?
        {
            if (grid[0][i] != -1 && grid[0][i] == grid[1][i] && grid[0][i] == grid[2][i]) {
                winner = playersId[grid[0][i]];
                return winner;
            }
            if (grid[i][0] != -1 && grid[i][0] == grid[i][1] && grid[i][0] == grid[i][2]) {
                winner = playersId[grid[i][0]];
                return winner;
            }
        }

        // Is a diagonal full ?
        if (grid[0][0] != -1 && grid[0][0] == grid[1][1] && grid[0][0] == grid[2][2]) {
            winner = playersId[grid[0][0]];
            return winner;
        }
        if (grid[2][0] != -1 && grid[2][0] == grid[1][1] && grid[2][0] == grid[0][2]) {
            winner = playersId[grid[2][0]];
            return winner;
        }

        boolean fullBoard = true;
        outerloop:
        // Defines a label for the first loop
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (isFree(i, j)) {
                    fullBoard = false;
                    break outerloop; // Breaks both of the loops
                }
            }
        }

        if (fullBoard) {
            this.winner = -2;
            return winner;
        }

        // No player won
        return -1;
    }

    @Override
    public String toString() {
        String tmp = "";
        for(Integer[] move : order)
            tmp += " x : " + move[0] + "\ty : " + move[1] + "\n";
        return tmp;
    }
}
