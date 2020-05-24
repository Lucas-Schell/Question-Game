import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Game {

    private final ArrayList<Player> players;
    private final int[] difPoints = {3, 6, 9};

    private String qustionCorrect;

    public Game(ArrayList<Player> players) {
        this.players = players;
    }

    public String getQuestion(String dif, String index) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("respostas.json"));
        JSONObject json = (JSONObject) obj;

        String question;

        question = (String) ((JSONObject) ((JSONObject) json.get(dif)).get(index)).get("pergunta");
        question += (String) ((JSONObject) ((JSONObject) json.get(dif)).get(index)).get("respostas");
        qustionCorrect = (String) ((JSONObject) ((JSONObject) json.get(dif)).get(index)).get("gabarito");
        System.out.println(question);
        return question;
    }

    public ArrayList<Player> score() {
        if (players.size() <= 1) return players;
        ArrayList<Player> top = new ArrayList<>();
        Player p1;
        Player p2;
        Player p3;
        if (players.size() == 2) {
            p1 = players.get(0).getPoints() > players.get(1).getPoints() ? players.get(0) : players.get(1);
            p2 = players.get(0).getPoints() > players.get(1).getPoints() ? players.get(1) : players.get(0);
            top.add(p1);
            top.add(p2);
            return top;
        }
        p1 = players.get(0).getPoints() > players.get(1).getPoints() ? players.get(0) : players.get(1);
        p2 = players.get(0).getPoints() > players.get(1).getPoints() ? players.get(1) : players.get(0);

        if(players.get(2).getPoints() > p1.getPoints()){
            top.add(players.get(2));
            top.add(p1);
            top.add(p2);
        }else{
            if(players.get(2).getPoints() > p2.getPoints()){
                top.add(p1);
                top.add(players.get(2));
                top.add(p2);
            }else{
                top.add(p1);
                top.add(p2);
                top.add(players.get(2));
            }
        }
        return top;
    }

    public String getCorrect() {
        return qustionCorrect;
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
