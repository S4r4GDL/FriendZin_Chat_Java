import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class ServerThread extends Mensageiro implements Runnable {

    private Socket socket;
    private List <ServerThread> usuarios;

    private String nomeCliente;

    public ServerThread(Socket socket){
        this.socket = socket;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public Socket getSocket() {
        return socket;
    }


    public void setUsuarios(List<ServerThread> usuarios) throws IOException {
        this.usuarios = usuarios;
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
            try {

                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                int protocolo;
                String mensagem;

                while (true) {
                    if ((mensagem = entrada.readLine()) != null && usuarios != null) {
                        protocolo = Integer.parseInt(valorCampo(mensagem, 0));


                        if (protocolo == 1){

                            if (nomeCliente == null || nomeCliente.isEmpty()) {
                                setNomeCliente(valorCampo(mensagem, 1));
                                enviarMensagem(mensagem, usuarios);
                                notificar(usuarios);
                            }
                            System.out.println(getNomeCliente() + " está conectado");
                        }
                        else if( protocolo == 2) {
                            enviarMensagem(mensagem, usuarios);
                        }
                        else if (protocolo == 3) {
                            int in = Integer.parseInt(valorCampo(mensagem, 3));
                            System.out.println("Usuario particular " + usuarios.get(in));
                            enviarMensagem(mensagem, usuarios.get(in));
                        } else {
                            System.out.println("ERRO 05 - PROTOCOLO INVÁLIDO");
                        }
                        mensagem = "";
                    }
                }


            } catch(IOException e){

                String n = nomeCliente;
                usuarios.remove(this);
                notificar(usuarios);
                try {
                    socket.close();
                    System.out.println( n + " saiu do chat");
                } catch (IOException f) {
                    System.err.println("Erro ao fechar o socket: " + f.getMessage());
                    throw new RuntimeException(f);
                }
        }
    }
    public void notificar(List <ServerThread> usuarios) {
        String nomeCliente = getNomeCliente();

        if (nomeCliente != null && !nomeCliente.isEmpty()) {
            System.out.println("Notificação: ");
            String lista = "4";
            for (int j = 0; j < usuarios.size(); j++) {
               lista += ";" + usuarios.get(j).getNomeCliente();
            }

            enviarMensagem(lista, usuarios);
        }
    }

    @Override
    public void enviarMensagem (String mens, List <ServerThread>  usuarios){
        try{
            for (int j = 0; j < usuarios.size(); j++) {
                PrintWriter saida = new PrintWriter(usuarios.get(j).getSocket().getOutputStream(), true);
                saida.println(mens);
                saida.flush();
            }
        }
        catch ( IOException e){

        }

    }
    @Override
    public void enviarMensagem (String mens, ServerThread  usuario){
            try{
            PrintWriter saida = new PrintWriter(usuario.getSocket().getOutputStream(), true);
            saida.println(mens);
            saida.flush();
        }
            catch ( IOException e){

        }
    }
}
