import java.net.Socket;
import java.util.Hashtable;
import java.util.Map;

public class DadosClientes {
    private Socket socket;
    private String nome;

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
