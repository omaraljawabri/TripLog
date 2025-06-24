package frontend.framesUI;

import backend.main.entities.Viajante;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import backend.main.entities.Viagem;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private LoginFrame login;
    private CadastroFrame cadastro;
    private CadastroViagemFrame cadastroViagem;
    private HomeFrame home;
    private ProfileUserFrame perfil;
    private ListaDeViagensFrame listaDeViagensFrame;

    private Viajante usuarioLogado;
    private final List<Viagem> viagens = new ArrayList<>();

    public MainFrame() {
        setTitle("Sistema de Acesso");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 500);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        login = new LoginFrame(this);
        cadastro = new CadastroFrame();
        home = null;
        perfil = null;
        cadastroViagem = null;

        cardPanel.add(login.getPanel(), "login");
        cardPanel.add(cadastro.getPanel(), "cadastro");

        setContentPane(cardPanel);
        cardLayout.show(cardPanel, "login");

        login.getTelaDeCadastroButton().addActionListener(e -> cardLayout.show(cardPanel, "cadastro"));
        cadastro.getTelaDeLoginButton().addActionListener(e -> cardLayout.show(cardPanel, "login"));
    }

    public void mostrarTela(String nomeTela) {
        cardLayout.show(cardPanel, nomeTela);
    }

    public void abrirHome(Viajante viajante) {
        this.usuarioLogado = viajante;

        // Remove o painel antigo se existir
        if (home != null) {
            cardPanel.remove(home.getPanel());
        }

        home = new HomeFrame(viajante);
        home.setCadastrarViagemButtonListener(e -> abrirCadastroViagem());
        home.setMinhasViagensButtonListener(e -> abrirListaDeViagens());
        home.setMeuPerfilButtonListener(e -> abrirPerfil(usuarioLogado));
        cardPanel.add(home.getPanel(), "home");

        cardLayout.show(cardPanel, "home");
        revalidate();
        repaint();
    }

    public void abrirCadastroViagem() {
        if (usuarioLogado != null) {
            if (cadastroViagem != null) {
                cardPanel.remove(cadastroViagem.getPanel());
            }

            cadastroViagem = new CadastroViagemFrame(usuarioLogado);
            cadastroViagem.addCancelarListener(e -> voltarParaHomeOuLogin());

            cardPanel.add(cadastroViagem.getPanel(), "cadastroViagem");
            revalidate();
            repaint();
            cardLayout.show(cardPanel, "cadastroViagem");
        } else {
            JOptionPane.showMessageDialog(this, "Usuário não está logado.");
        }
    }

    public void abrirPerfil(Viajante viajante) {
        this.usuarioLogado = viajante;

        if (perfil != null) {
            cardPanel.remove(perfil.getPanel());
        }

        perfil = new ProfileUserFrame(viajante);

        perfil.setBtnInicioListener(() -> abrirHome(usuarioLogado));
        perfil.getBtnListaViagens().addActionListener(e -> abrirListaDeViagens());
        perfil.getBtnLogout().addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logout efetuado!");
            usuarioLogado = null;
            cardLayout.show(cardPanel, "login");
        });

        cardPanel.add(perfil.getPanel(), "perfil");
        cardLayout.show(cardPanel, "perfil");
        revalidate();
        repaint();
    }

    public void abrirListaDeViagens() {
        if (listaDeViagensFrame != null) {
            cardPanel.remove(listaDeViagensFrame.getPanel());
        }

        listaDeViagensFrame = new ListaDeViagensFrame(
                usuarioLogado,
                usuarioLogado.getViagens(),
                viagem -> JOptionPane.showMessageDialog(this, "Abrindo detalhes de: " + viagem.getLugarDeChegada()),
                viagem -> {
                    JOptionPane.showMessageDialog(this, "Viagem para " + viagem.getLugarDeChegada() + " excluída!");
                    usuarioLogado.getViagens().remove(viagem);
                    listaDeViagensFrame.atualizarListaPublic();
                },
                e -> abrirPerfil(usuarioLogado),
                e -> abrirCadastroViagem()
        );

        cardPanel.add(listaDeViagensFrame.getPanel(), "listaViagens");
        cardLayout.show(cardPanel, "listaViagens");
        revalidate();
        repaint();
    }



    private void voltarParaHomeOuLogin() {
        if (usuarioLogado != null) {
            abrirHome(usuarioLogado);
        } else {
            cardLayout.show(cardPanel, "login");
        }
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        FlatLightLaf.setup();

        UIManager.put("Button.arc", 20);
        UIManager.put("TextComponent.arc", 10);
        UIManager.put("Component.arc", 10);

        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
