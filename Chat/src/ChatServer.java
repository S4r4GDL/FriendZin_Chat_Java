import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

    private static List <ServerThread> listaServerThread;
    public static synchronized void adicionarThread(ServerThread thread) {
        listaServerThread.add(thread);
    }

    public static synchronized void removerThread(ServerThread thread) {
        listaServerThread.remove(thread);
    }
    private ChatServer(){

    }
    public static void main(String[] args) {
        listaServerThread = new ArrayList<>();

        ServerSocket servidor = null;
        try {
            servidor = new ServerSocket(12345);
            System.out.println("FriendZin Está aberto na porta " + servidor.getLocalPort());


            while (true) {


                Socket novo = servidor.accept();
                ServerThread thread;


                System.out.println("Uma nova conexão foi feita:\n Cliente : " +
                        novo.getInetAddress().toString() +
                        " na porta :" + novo.getLocalPort());

                thread = new ServerThread(novo);
                    int i = listaServerThread.size();
                    thread.start();
                    adicionarThread(thread);
                    thread.setUsuarios(listaServerThread);
                    System.out.println("Thread ligada\n");
                System.out.println("Lista: " +listaServerThread);


            }

        } catch (SocketTimeoutException e) {
            System.out.println("Erro ao conectar cliente ao servidor");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro nas conexões");
        } finally {
            System.out.println("Fim");
        }


    }


}
