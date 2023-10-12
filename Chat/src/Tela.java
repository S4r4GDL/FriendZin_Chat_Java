import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

public class Tela extends JFrame{
    Cliente cliente;
    JPanel chatPanel, listaUserPanel, conversaPanel, escrevePanel;
    JButton enviaBt;

    final int L = 5;
    final Color azulFriendzin = new Color(153, 214, 193);
    final Color azulEscuroFriendzin = new Color(124, 173, 156);
    final Color cianoEscuroFriendzin = new Color(45, 91, 75);
    final Color cinzaFriendzin = new Color(217, 217, 217);
    final Color verdeFriendzin = new Color(193, 255, 114);


    JTextField escreveTf;
    JTextArea conversaTa;
    JList<String> lista;
    DefaultListModel modelo;

    JList<String> listaUsuarios;

    public Tela() throws IOException {

        banner();
        digitaEnvia();
        listarUsuario();
        historicoConversa();
        montaFrame("North", chatPanel,
                "East", listaUserPanel, "South" ,
                escrevePanel, "Center", conversaPanel);

        String nome = caixaDialogo();
        cliente = new Cliente(this, nome);
        cliente.start();
        cliente.enviarMensagem(1);
        setTitle("FrendZin - " + nome);
        mostraTela();
    }
    public String caixaDialogo(){
        String nome;
        nome =  JOptionPane.showInputDialog(null,"Insira seu nome", "FriendZin", JOptionPane.INFORMATION_MESSAGE);
        return nome;
    }

    public void banner(){

        chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setBackground(azulFriendzin);
        chatPanel.add("Center", new JLabel(new ImageIcon("FriendZin.png")));

    }



    public void listarUsuario(){



        List<String> listaUser = new ArrayList<>();
        listaUser.add("Todos");

        lista = criaLista("    Pessoas Online    ", listaUser, azulFriendzin);

        listaUserPanel = new JPanel();
        listaUserPanel.setSize(110, 80);
        listaUserPanel.setLayout(new BorderLayout());
        listaUserPanel.setBackground(azulEscuroFriendzin);

        listaUserPanel.add(lista);
        listaUserPanel.setBorder(BorderFactory.createMatteBorder(L, L, L, L, cinzaFriendzin));


    }
    private  JList<String> criaLista(String titulo, List<String> conteudo, Color cor){

        modelo = new DefaultListModel<>();
        modelo.addElement(titulo);
        modelo.addAll(conteudo);

        JList<String> lista = new JList<String>();
        lista.setModel(modelo);
        lista.setBackground(cor);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setSelectedIndex(1);
        lista.setSelectionBackground(cianoEscuroFriendzin);
        lista.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(lista);

        scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);

        return lista;

    }
    public void atualizaLista(List<String> usuariosOnline) {

        DefaultListModel<String> modelo = (DefaultListModel<String>) lista.getModel();
        modelo.clear();

        modelo.addElement("    Pessoas Online    ");
        modelo.addElement("Todos");
        lista.setSelectedIndex(1);

        for (String usuario : usuariosOnline) {
            modelo.addElement(usuario);
        }
    }
    public void digitaEnvia(){

        ActionListener acaoBotoes = new AcaoBotoes();
        escreveTf = new JTextField();
        enviaBt = new JButton("Enviar");
        enviaBt.setBackground(verdeFriendzin);
        enviaBt.setPreferredSize(new Dimension(120, 30));
        enviaBt.addActionListener(acaoBotoes);
        JScrollPane scrollPane = new JScrollPane(escreveTf);

        scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);

        escrevePanel = new JPanel();
        escrevePanel.setLayout(new BorderLayout());
        escrevePanel.add("Center", scrollPane);
        escrevePanel.add("East", enviaBt);
        escrevePanel.setBorder(BorderFactory.createMatteBorder(L, L, L, L, cinzaFriendzin));

    }

    public void historicoConversa(){

        conversaTa = new JTextArea();
        conversaTa.setLineWrap(true);
        conversaTa.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(conversaTa);

        scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);

        conversaPanel = new JPanel();
        conversaPanel.setLayout(new BorderLayout());
        conversaPanel.add("Center", scrollPane);
        conversaPanel.setBorder(BorderFactory.createMatteBorder(L, L, L, L, cinzaFriendzin));
    }

    public void montaFrame(String local1, JPanel panel1, String local2,
                           JPanel panel2, String local3, JPanel panel3,
                           String local4, JPanel panel4){

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(local1, panel1);
        getContentPane().add(local2, panel2);
        getContentPane().add(local3, panel3);
        getContentPane().add(local4, panel4);
    }

    public void mostraTela()
    {
        setSize(800,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public void atualizarConversa(String mensagem){
        conversaTa.append(mensagem);
    }
    public class AcaoBotoes implements ActionListener {
    Interceptador interceptador = new Interceptador();
        @Override
        public void actionPerformed(ActionEvent e) {

            if(Objects.equals(e.getActionCommand(), "Enviar")){
                if(!escreveTf.getText().isEmpty() && !lista.isSelectionEmpty()){
                    if(lista.getSelectedIndex() == 1)
                    {
                        interceptador.enviar(escreveTf.getText(), cliente, lista.getSelectedIndex(), 2);
                        System.out.println("passou do envia");
                    }
                    else{

                        interceptador.enviar(escreveTf.getText(), cliente, lista.getSelectedIndex()-2, 3);

                    }

                    escreveTf.setText("");
                }
            }
        }
    }
}
