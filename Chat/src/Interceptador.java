import java.util.List;

public class Interceptador{
    public Interceptador(){

    }
    public void enviar(String mensagem, Cliente cliente, int destinatario, int protocolo) {
        cliente.setDest(destinatario);
        cliente.setTexto(mensagem);
        cliente.enviarMensagem(protocolo);
    }
    public void receber(Tela tela, String mensagem) {
        tela.atualizarConversa(mensagem);

    }
    public void receberLista(Tela tela, List<String> lista){
        tela.atualizaLista(lista);
    }

}
