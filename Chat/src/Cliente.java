import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cliente extends Mensageiro implements Runnable {

    private static Socket socket;
    private String texto;
    private Integer dest;
    private String nome;
    private String mensagem;
    private String nomeRemetente;
    private String textoRemetente;
    private String mensagens;

    private List<String> listaUsuarios;
    private List<String> listaUsuarios2;
    private Tela tl;
    private Interceptador interceptador;

    public Cliente(Tela tl, String nome) throws IOException {
        this.nome = nome;
        interceptador = new Interceptador();
        listaUsuarios = new ArrayList<>();
        listaUsuarios2 = new ArrayList<>();
        this.tl = tl;
        socket = new Socket("26.50.210.190", 12345);

    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setDest(Integer d) {
        this.dest = d;
    }

    public void atualizarUsuarios(){
        if (listaUsuarios2 != null) {
            listaUsuarios.clear();
            listaUsuarios.addAll(listaUsuarios2);
            interceptador.receberLista(tl, listaUsuarios);
            listaUsuarios2.clear();
        }
    }
    public void plainMsgs(String msg) {
        interceptador.receber(tl, msg);
    }

    @Override
    public void recebeMensagem(String saidaServ) throws IOException {
        mensagens = "";
        if(valorCampo(saidaServ, 1) != null)
        {
            nomeRemetente = valorCampo(saidaServ, 1)  ;
            if(saidaServ.startsWith("1"))
            {
                textoRemetente = " juntou-se ao chat!";
            }
            else if (saidaServ.startsWith("2")){
                System.out.println("2");
                textoRemetente = ": " + valorCampo(saidaServ, 2);
            }
            else if(saidaServ.startsWith("3")){
                textoRemetente = " (Em particular): " + valorCampo(saidaServ, 2);
            }
            else if(saidaServ.startsWith("4"))
            {
                String [] lista = saidaServ.split(";");
                listaUsuarios2.addAll(Arrays.asList(lista).subList(1, lista.length));
                System.out.println(Arrays.toString(lista));
                atualizarUsuarios();
            }
            saidaServ = "";

            if(textoRemetente!= null && !textoRemetente.isEmpty())
            {
                mensagens += nomeRemetente + textoRemetente + "\n" ;
                plainMsgs(mensagens);
                nomeRemetente = "";
                textoRemetente = "";
                mensagens = "";
            }

        }

    }

    @Override
    public void enviarMensagem(int protocolo) {
        if(nome != null && !nome.isEmpty()){
            if (protocolo == 1) 
            {
                mensagem = "1" + ";" + nome;
            }
            else if (protocolo == 2)
            {
                mensagem = "2" + ";" + nome + ";" + texto;
            }
            else if (protocolo == 3)
            {
                mensagem = "3" + ";" + nome + ";" + texto + ";" + dest.toString();
                mensagens += "Eu" + " para " + tl.lista.getSelectedValue() +  " (Em particular): " + texto + "\n" ;
                System.out.println(mensagens);
                plainMsgs(mensagens);
                mensagens = "";
            }
            PrintWriter saida = null;
            try {
                saida = new PrintWriter(socket.getOutputStream(), true);
    
                if((mensagem != null) && !mensagem.isEmpty())
                {
                    saida.println(mensagem);
                    System.out.println("Mensagem enviada: " + mensagem);
    
                }
                mensagem = null;
                saida.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);}
        }
    }



    @Override
    public void run() {

        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String en;
            plainMsgs("Chat aberto \n");
            while(true)
            {
                if((en = entrada.readLine())!= null)
                {

                        recebeMensagem(en);
                        System.out.println("Recebido do Server: " + en);


                    en = null;
                }
            }


        }catch (IOException e){
            System.out.println("Erro ao enviar dados ao servidor no cliente");
        }

    }


}
