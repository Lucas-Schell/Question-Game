import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {

    private final ArrayList<Player> players;
    private final int[] difPoints = {3, 6, 9};
    private String qustionCorrect;

    public Game(ArrayList<Player> players) {
        this.players = players;
    }

    public String getQuestion(int index) throws FileNotFoundException {
        File f = new File("perguntas.txt");
        Scanner sc = new Scanner(f);

        qustionCorrect = "";
        return "";
    }

    public void addPoints(ArrayList<String> answers, int dif) {
        int points = difPoints[dif];

        for (String str : answers) {
            String[] ans = str.split(" ");
            if (qustionCorrect.equals(ans[0])) {
                Player p = getPlayer(ans[1]);
                if (p != null) {
                    getPlayer(ans[1]).addPoints(points);
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
