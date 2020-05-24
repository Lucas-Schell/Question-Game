import java.net.InetAddress;

public class Player {

    private String name;
    private int points;
    private InetAddress ip;
    private int port;

    public Player(String name, InetAddress ip, int port){
        this.name = name;
        this.ip = ip;
        this.port = port;
        points = 0;
    }

    public void addPoints(int pts){
        points += pts;
    }

    public int getPoints(){
        return points;
    }

    public String getName(){
        return name;
    }
}
