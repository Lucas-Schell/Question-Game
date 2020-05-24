import java.net.InetAddress;

public class Player {

    private String name;
    private int points;

    private InetAddress ip;

    private int port;

    // construtor do objeto aluno, contendo todas as informacoes importantes
    public Player(String name, InetAddress ip, int port){
        this.name = name;
        this.ip = ip;
        this.port = port;
        points = 0;
    }

    // caso o jogador pontue, serão adicionados pontos no seu total
    public void addPoints(int pts){
        points += pts;
    }

    // retorna o IP do jogador
    public InetAddress getIp() {
        return ip;
    }

    // retorna a porta do jogador
    public int getPort() {
        return port;
    }

    // caso seja necessario consultar os pontos atuais do jogador, esse metodo sera utilizado
    public int getPoints(){
        return points;
    }

    // retornara o nome do jogador
    public String getName(){
        return name;
    }
}
