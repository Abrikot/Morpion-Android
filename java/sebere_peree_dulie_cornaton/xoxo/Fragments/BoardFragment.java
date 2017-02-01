package sebere_peree_dulie_cornaton.xoxo.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import sebere_peree_dulie_cornaton.xoxo.EndGameActivity;
import sebere_peree_dulie_cornaton.xoxo.GameController.Match;
import sebere_peree_dulie_cornaton.xoxo.R;

/**
 * Created by Maxence on 05/12/2016.
 */

public class BoardFragment extends Fragment {
    private Match match = null;
    private ArrayList<Button> caseList;
    int orderMax = 0; // This attribut lets us making the timer to show a match
    Timer timer = null;
    boolean lookingAgain = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        caseList = new ArrayList<>();
        caseList.add((Button) view.findViewById(R.id._00));
        caseList.add((Button) view.findViewById(R.id._01));
        caseList.add((Button) view.findViewById(R.id._02));
        caseList.add((Button) view.findViewById(R.id._10));
        caseList.add((Button) view.findViewById(R.id._11));
        caseList.add((Button) view.findViewById(R.id._12));
        caseList.add((Button) view.findViewById(R.id._20));
        caseList.add((Button) view.findViewById(R.id._21));
        caseList.add((Button) view.findViewById(R.id._22));
    }

    private void initializeButtons() {
        for (final Button b : caseList)
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (match != null) {
                        int index = caseList.indexOf(b);
                        int row = index / 3;
                        int column = index % 3;

                        match.setMoveActivePlayer(row, column);
                        updateView();
                    }
                }
            });
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
        this.lookingAgain = true;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() { // Mandatory method. Can't access to a UI element from any thread that hasn't the one which created the UI element.
                    @Override
                    public void run() {
                        viewMatch();
                        orderMax++;
                    }
                });
            }
        }, 500, 1000);
    }

    public void newMatch(int id1, String player1, int id2, String player2) {
        match = new Match(id1, player1, id2, player2);
        initializeButtons();
    }

    public void updateView() {
        int grid[][] = match.getGrid();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                if (!match.isFree(i, j)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        caseList.get(i * 3 + j).setBackground(getResources().getDrawable((grid[i][j] == 0 ? R.drawable.white_token : R.drawable.black_token)));
                    } else {
                        caseList.get(i * 3 + j).setBackgroundDrawable(getResources().getDrawable((grid[i][j] == 0 ? R.drawable.white_token : R.drawable.black_token)));
                    }
                }
            }

        if (match.isFinished() != -1) {
            Intent intent = new Intent(getActivity(), EndGameActivity.class);
            intent.putExtra("match", match);
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String user1 = preferences.getString("nicknameMainUser", "User 1");
            intent.putExtra("matchNumber", getActivity().getIntent().getIntExtra("matchNumber", 0));
            startActivity(intent);
        } else {
            String string = getString(R.string.players_turn, match.getActivePlayer());
            Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
        }
    }

    public void viewMatch() {
        int grid[][] = match.getGrid();
        ArrayList<Integer[]> order = match.getOrder();
        IsUnderOrder testFunction = new IsUnderOrder();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                if (!match.isFree(i, j) && testFunction.isUnderOrder(i, j, orderMax, order)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        caseList.get(i * 3 + j).setBackground(getResources().getDrawable((grid[i][j] == 0 ? R.drawable.white_token : R.drawable.black_token)));
                    } else {
                        caseList.get(i * 3 + j).setBackgroundDrawable(getResources().getDrawable((grid[i][j] == 0 ? R.drawable.white_token : R.drawable.black_token)));
                    }
                }
            }
        if (orderMax >= order.size()) {
            timer.cancel();
            this.lookingAgain = false;
            if (orderMax < 9 && match.getWinner() == -1) {
                initializeButtons();
                String string = getString(R.string.players_turn, match.getActivePlayer());
                Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
                getActivity().getIntent().putExtra("isLookingAgain", false);
            } else {
                int winner = match.getWinner();
                if (winner == -2) // Draw
                    Toast.makeText(getActivity(), getString(R.string.draw), Toast.LENGTH_SHORT).show();
                else // A player won the game
                {
                    String winnerName = match.getPlayerById(winner);
                    Toast.makeText(getActivity(), getString(R.string.endGame, winnerName), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class IsUnderOrder {
        public boolean isUnderOrder(int x, int y, int orderMax, ArrayList<Integer[]> order) {
            int position = findItem(x, y, order);
            if (position != -1) {
                if (position <= orderMax)
                    return true;
            }

            return false;
        }

        public int findItem(int x, int y, ArrayList<Integer[]> order) {
            for (int i = 0; i < order.size(); ++i) {
                Integer move[] = order.get(i);
                if (move[0] == x && move[1] == y)
                    return i;
            }

            return -1;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null)
            timer.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null)
            timer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.lookingAgain && timer != null) {
            timer.cancel();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() { // Mandatory method. Can't access to a UI element from any thread that hasn't the one which created the UI element.
                        @Override
                        public void run() {
                            viewMatch();
                            orderMax++;
                        }
                    });
                }
            }, 700, 1000);
        }
    }
}
