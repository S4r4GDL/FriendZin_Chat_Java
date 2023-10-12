import java.io.IOException;
import java.util.List;

public abstract class Mensageiro{


    public String valorCampo(String txt, int indice) {
        String [] campos = txt.split(";");
        return campos[indice];
    }

    public void recebeMensagem(String texto) throws IOException {

    }
    public void enviarMensagem(int acao) {

    }
    public void enviarMensagem(String mensagem, Cliente cliente, int destinatario) {

    }
    public void enviarMensagem(String mens, ServerThread usuario) {

    }

    public void enviarMensagem(String mens, List<ServerThread> usuarios) {

    }
}
