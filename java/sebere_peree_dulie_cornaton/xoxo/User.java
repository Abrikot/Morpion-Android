package sebere_peree_dulie_cornaton.xoxo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Maxence on 05/12/2016.
 */

public class User {
    private int id;
    private String nickname = "";
    private int score = 0;

    public User(int id, String nickname, int score) {
        this.id = id;
        this.nickname = nickname;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public int getScore() {
        return score;
    }

    public static ArrayList<User> sortUserListByScore(ArrayList<User> userList)
    {
        Collections.sort(userList, new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                if (u1.getScore() > u2.getScore())
                    return -1;
                else if (u1.getScore() < u2.getScore())
                    return 1;
                return 0;
            }
        });
        return userList;
    }
}
