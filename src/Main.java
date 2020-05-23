import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        System.out.println("Carlos gay!");


    }

    public void start() throws IOException {
        DatagramSocket serverSocket = new DatagramSocket(9876);

        byte[] receiveData = new byte[1024];
        while (true) {
            // declara o pacote a ser recebido
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            // recebe o pacote do cliente
            serverSocket.receive(receivePacket);
        }
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