// Le uma linha do teclado
// Envia o pacote (linha digitada) ao servidor

import java.io.*; // classes para input e output streams e
import java.net.*;// DatagramaSocket,InetAddress,DatagramaPacket

class Client {

    private static DatagramSocket clientSocket;
    private static byte[] sendData;
    private static byte[] receiveData;


    public static void main(String args[]) throws Exception {
        // declara socket cliente
        clientSocket = new DatagramSocket();

        byte[] sendData = new byte[1024];
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            // chama o metodo de cadastro
            startPlaying();
            while (true) {

                byte[] receiveData = new byte[3000];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                System.out.println("Agardando servidor...");
                clientSocket.receive(receivePacket);
                String[] str = new String(receivePacket.getData()).split(" ");
                if (str[0].equals("Escolha")) {


                    // requisita-se a dificuldade
                    System.out.println(new String(receivePacket.getData()));

                    // le uma linha do teclado
                    String sentence = inFromUser.readLine();
                    sendData = sentence.getBytes();

                    receivePacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("localhost"), 9876);

                    //envia o pacote
                    clientSocket.send(receivePacket);
                } else {
                    // verifica se o jogo terminou
                    if (str[0].equals("Fim")) {

                        // verifica se o jogador quer outra partida
                        // assim garantindo se devera fechar o socket e retornar ou permanecer ativo
                        String s = new String(receivePacket.getData());
                        System.out.println(s);
                        System.out.println("Obrigado por jogar!" + s.length());
                        System.out.println("Gostaria de jogar novamente?");
                        System.out.println("Sim - 1, Nao - 0");

                        String sentence = inFromUser.readLine();

                        if (sentence.equals("0")) {
                            // fecha o cliente
                            clientSocket.close();
                            return;
                        }

                        break;
                    } else {
                        if (str[0].equals("Resposta")) {
                            System.out.println((new String(receivePacket.getData())));
                        } else {
                            // chama um metodo para responder questoes
                            newQuestion(receivePacket, inFromUser);
                        }
                    }
                }
            }
        }
    }

    // metodo onde cada resposta sera respondida
    public static void newQuestion(DatagramPacket data, BufferedReader keyboard) throws IOException {

        // mostra a pergunta junto das respostas
        // tambem requisita a resposta do jogador
        System.out.println((new String(data.getData())));
        System.out.println("Digite apenas a letra correspondente a resposta abaixo:");

        // converte a resposta obtida em bytes para envio
        long startTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - startTime) < 10000
                && !keyboard.ready()) {
        }
        if (!keyboard.ready()) {
            return;
        }
        String sentence = keyboard.readLine();
        sendData = sentence.getBytes();

        // envia a resposta adquirida
        data = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("localhost"), 9876);
        clientSocket.send(data);
    }

    public static void startPlaying() throws IOException {
        // cria o stream do teclado
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        // obtem endereco IP do servidor com o DNS
        InetAddress IPAddress = InetAddress.getByName("localhost");

        // requisita-se o username do jogador
        System.out.println("Digite o seu username:");
        System.out.println("OBS: Use apenas uma palavra para o mesmo.");

        // le uma linha do teclado
        String sentence = inFromUser.readLine();
        sendData = sentence.getBytes();

        // cria pacote com o dado, o endereco do server e porta do servidor
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);

        //envia o pacote
        clientSocket.send(sendPacket);

    }
}
