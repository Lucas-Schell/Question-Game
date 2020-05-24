// Le uma linha do teclado
// Envia o pacote (linha digitada) ao servidor

import java.io.*; // classes para input e output streams e
import java.net.*;// DatagramaSocket,InetAddress,DatagramaPacket

class Client {

    DatagramSocket clientSocket;
    byte[] sendData;
    byte[] receiveData;


    public void main(String args[]) throws Exception {
        // declara socket cliente
        DatagramSocket clientSocket = new DatagramSocket(9876);

        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            // chama o metodo de cadastro
            startPlaying();
            while (true) {


                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                clientSocket.receive(receivePacket);
                if ((receivePacket.getData() + "").equals("Escolha a dificuldade: (0-facil, 1-medio, 2-dificil)")) {


                    // requisita-se a dificuldade
                    System.out.println(receivePacket.getData());

                    // le uma linha do teclado
                    String sentence = inFromUser.readLine();
                    sendData = sentence.getBytes();

                    receivePacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("localhost"), 9876);

                    //envia o pacote
                    clientSocket.send(receivePacket);
                }
                // verifica se o jogo terminou
                if ((receivePacket.getData() + "").equals("end")) {

                    // verifica se o jogador quer outra partida
                    // assim garantindo se devera fechar o socket e retornar ou permanecer ativo
                    System.out.println("Obrigado por jogar!");
                    System.out.println("Gostaria de jogar novamente?");
                    System.out.println("Se não, digite 0");

                    String sentence = inFromUser.readLine();

                    if(sentence.equals("0")){
                        // fecha o cliente
                        clientSocket.close();
                        return;
                    }

                    break;
                }
                // chama um metodo para responder questoes
                newQuestion(receivePacket);


            }
        }
    }

    // metodo onde cada resposta sera respondida
    public void newQuestion(DatagramPacket data) {

    }

    public void startPlaying() throws IOException {
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
