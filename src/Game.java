

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {

    private final ArrayList<Player> players;
    private final int[] difPoints = {3, 6, 9};
    private String qustionCorrect;

    public Game(ArrayList<Player> players) {
        this.players = players;
    }

    public String[] getQuestion(String dif, String index) throws IOException, JSONException {
        JSONObject json = new JSONObject("respostas.json");
        String[] question = new String[2];

        question[0] = json.getJSONObject(dif).getJSONObject(index).getString("pergunta");
        question[1] = json.getJSONObject(dif).getJSONObject(index).getString("respostas");
        qustionCorrect = json.getJSONObject(dif).getJSONObject(index).getString("gabarito");

        return question;
    }

    public void addPoints(ArrayList<String[]> answers, int dif) {
        int points = difPoints[dif];

        for (String[] str : answers) {
            if (qustionCorrect.equalsIgnoreCase(str[0])) {
                Player p = getPlayer(str[1]);
                if (p != null) {
                    getPlayer(str[1]).addPoints(points);
                    points--;
                }
            }
        }
    }

    private Player getPlayer(String name) {
        for (Player p : players) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }
}
