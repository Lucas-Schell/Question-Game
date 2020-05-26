import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    // variaveis globais da classe
    private static ArrayList<Player> players;
    private static DatagramSocket serverSocket;
    private static Game game;

    // comeco da execucao do codigo
    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
        while (true) {
            init();
        }
    }

    // inicializacao do jogo
    public static void init() throws IOException, InterruptedException, ParseException {
        // instancia-se um array de jogadores, armazenando-os ao entrarem
        System.out.println("Aguardando jogadores...");
        players = new ArrayList<>();
        start();

        // caso nao haja jogadores, a aplicacao se desliga
        game = new Game(players);
        if (players.size() == 0) {
            serverSocket.close();
            return;
        }
        System.out.println("dif" + players.get(0).getIp() + " " + players.get(0).getPort());
        byte[] sendData = "Escolha a dificuldade: (0-facil, 1-medio, 2-dificil)".getBytes();
        DatagramPacket packet = new DatagramPacket(sendData, sendData.length, players.get(0).getIp(), players.get(0).getPort());
        serverSocket.send(packet);

        packet = new DatagramPacket(sendData, sendData.length);

        int dif = 1;

        serverSocket.setSoTimeout(10000);
        try {
            serverSocket.receive(packet);
            String s = (new String(packet.getData())).charAt(0) + "";
            dif = Integer.parseInt(s);
        } catch (Exception e) {
        }

        play(questionOrder(dif));
    }

    public static void play(ArrayList<String[]> questions) throws IOException, InterruptedException, ParseException {
        for (String[] str : questions) {
            ArrayList<String[]> answers = new ArrayList<>();
            String q = game.getQuestion(str[0], str[1]);
            serverSocket.setSoTimeout(10000);
            for (Player p : players) {
                byte[] sendData = q.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, p.getIp(), p.getPort());
                serverSocket.send(sendPacket);
            }

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
            } catch (Exception ignored) {

            }

            for (Player p : players) {
                String msg = "Resposta correta: " + game.getCorrect();
                byte[] sendData = msg.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, p.getIp(), p.getPort());
                serverSocket.send(sendPacket);
            }

            game.addPoints(answers, Integer.parseInt(str[0]));
        }

        System.out.println("fim");
        String msg = "Fim \n";
        for (Player p : game.score()) {
            System.out.println(p.getPoints() + " " + p.getName());
            msg += p.getPoints() + " " + p.getName() + " ";
        }
        for (Player p : players) {
            DatagramPacket sendPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, p.getIp(), p.getPort());
            serverSocket.send(sendPacket);
        }
        serverSocket.close();
        Thread.sleep(5000);
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
