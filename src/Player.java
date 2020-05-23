public class Player {

    private String name;
    private int points;
    public Player(String name){
        this.name = name;
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
