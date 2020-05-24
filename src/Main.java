import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    private static ArrayList<Player> players;
    Game game;

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Carlos gay!");
        players = new ArrayList<>();
        start();
        for (Player p : players) {
            System.out.println(p.getName());
        }
    }

    public static void start() throws IOException, InterruptedException {
        DatagramSocket serverSocket = new DatagramSocket(9876);

        serverSocket.setSoTimeout(15000);

        try {
            while (players.size() < 3) {
                byte[] receiveData = new byte[1024];
                // declara o pacote a ser recebido
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                // recebe o pacote do cliente
                serverSocket.receive(receivePacket);

                new Thread(() -> {
                    try {
                        addPlayer(receivePacket);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (Exception e) {

        }
    }

    private static void addPlayer(DatagramPacket receivePacket) throws InterruptedException {
        if (players.size() >= 3) return;
        String[] data = new String(receivePacket.getData()).split(" ");
        InetAddress IPAddress = receivePacket.getAddress();
        int port = receivePacket.getPort();

        for (Player p : players) {
            if (p.getName().equals(data[0])) {
                return;
            }
        }
        Player p = new Player(data[0], IPAddress, port);
        players.add(p);
    }

    public ArrayList<String[]> questionOrder(int dif) {
        ArrayList<String[]> list = new ArrayList<>();
        Random r = new Random();
        int cont = dif == 0 ? 4 : 3;
        for (int i = 0; i < cont; i++) {
            list.add(new String[]{dif + "", i + ""});
        }
        if (dif == 0) {
            list.add(new String[]{"1", r.nextInt(3) + ""});
        } else {
            if (dif == 1) {
                list.add(new String[]{"0", r.nextInt(4) + ""});
                list.add(new String[]{"2", r.nextInt(3) + ""});
            } else {
                list.add(new String[]{"1", r.nextInt(3) + ""});
                list.add(new String[]{"1", r.nextInt(3) + ""});
            }
        }
        return list;
    }
}