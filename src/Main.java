import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    private static ArrayList<Player> players;
    private static DatagramSocket serverSocket;
    private Game game;

    public static void main(String[] args) throws IOException, InterruptedException {
        while (true) {
            init();
        }
    }

    public static void init() throws IOException, InterruptedException {
        System.out.println("Agurdando jogadores...");
        players = new ArrayList<>();
        start();
        System.out.println("dif" + players.get(0).getIp() + " " + players.get(0).getPort());
        byte[] sendData = "Escolha a dificuldade: (0-facil, 1-medio, 2-dificil)".getBytes();
        DatagramPacket packet = new DatagramPacket(sendData, sendData.length, players.get(0).getIp(), players.get(0).getPort());
        serverSocket.send(packet);

        packet = new DatagramPacket(sendData, sendData.length);

        int dif = 1;

        serverSocket.setSoTimeout(10000);
        try {
            serverSocket.receive(packet);
            dif = Integer.parseInt(packet.getData() + "");
        } catch (Exception e) {
        }

        play(questionOrder(dif));
    }

    public static void play(ArrayList<String[]> questions) throws SocketException {
        ArrayList<String[]> answers = new ArrayList<>();
        for (String[] str : questions) {
            serverSocket.setSoTimeout(10000);

            try {
                while (answers.size() < players.size()) {
                    byte[] receiveData = new byte[1024];
                    // declara o pacote a ser recebido
                    DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);

                    // recebe o pacote do cliente
                    serverSocket.receive(packet);

                    new Thread(() -> {
                        try {
                            answer(packet, answers);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            } catch (Exception e) {

            }
        }
    }

    public static void answer(DatagramPacket packet, ArrayList<String[]> answers) {
        String data = new String(packet.getData());
        int port = packet.getPort();

        for (Player p : players) {
            if (p.getPort() == port) {
                answers.add(new String[]{data.charAt(0) + "", p.getName()});
                return;
            }
        }
    }

    public static void start() throws IOException, InterruptedException {
        serverSocket = new DatagramSocket(9876);

        serverSocket.setSoTimeout(15000);
        System.out.println("k");
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
        System.out.println("player" + p.getIp() + " " + p.getPort());
        players.add(p);
    }

    public static ArrayList<String[]> questionOrder(int dif) {
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