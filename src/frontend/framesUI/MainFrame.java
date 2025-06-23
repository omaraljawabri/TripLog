package frontend.framesUI;

import backend.main.entities.Viajante;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    // Instâncias das telas para controle
    private LoginFrame login;
    private CadastroFrame cadastro;
    private CadastroViagemFrame cadastroViagem;
    private HomeFrame home;
    private ProfileUserFrame perfil;  // Tela de perfil

    private Viajante usuarioLogado;

    public MainFrame() {
        setTitle("Sistema de Acesso");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 500);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        login = new LoginFrame(this);
        cadastro = new CadastroFrame();
        cadastroViagem = new CadastroViagemFrame();

        // Inicialmente home e perfil nulos
        home = null;
        perfil = null;

        // Adiciona telas fixas
        cardPanel.add(login.getPanel(), "login");
        cardPanel.add(cadastro.getPanel(), "cadastro");
        cardPanel.add(cadastroViagem.getPanel(), "cadastroViagem");

        setContentPane(cardPanel);
        cardLayout.show(cardPanel, "login");

        // Navegação login <-> cadastro
        login.getTelaDeCadastroButton().addActionListener(e -> cardLayout.show(cardPanel, "cadastro"));
        cadastro.getTelaDeLoginButton().addActionListener(e -> cardLayout.show(cardPanel, "login"));

        // Cancelar cadastro de viagem volta para home ou login
        cadastroViagem.addCancelarListener(e -> {
            if (usuarioLogado != null) {
                abrirHome(usuarioLogado);
            } else {
                cardLayout.show(cardPanel, "login");
            }
        });
    }

    // Mostra tela pelo nome no cardLayout
    public void mostrarTela(String nomeTela) {
        cardLayout.show(cardPanel, nomeTela);
    }

    // Abre tela Home e configura botões para navegação
    public void abrirHome(Viajante viajante) {
        this.usuarioLogado = viajante;

        if (home == null) {
            home = new HomeFrame(viajante);

            // Configura listeners dos botões do HomeFrame
            home.setCadastrarViagemButtonListener(e -> abrirCadastroViagem());
            home.setMinhasViagensButtonListener(e -> abrirPerfil(usuarioLogado));

            cardPanel.add(home.getPanel(), "home");
        }
        cardLayout.show(cardPanel, "home");
        revalidate();
        repaint();
    }

    // Abre tela cadastro viagem
    public void abrirCadastroViagem() {
        cardLayout.show(cardPanel, "cadastroViagem");
    }

    // Abre tela perfil do usuário, configura botões de navegação e logout
    public void abrirPerfil(Viajante viajante) {
        this.usuarioLogado = viajante;

        if (perfil != null) {
            cardPanel.remove(perfil.getPanel());
        }
        perfil = new ProfileUserFrame(viajante);

        // Botão "Voltar para início" volta para Home
        perfil.setBtnInicioListener(this, usuarioLogado);

        // Botão logout volta para login e limpa usuário logado
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

    // Método auxiliar para voltar para home ou login conforme estado de login
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
