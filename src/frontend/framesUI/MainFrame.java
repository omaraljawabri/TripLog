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

    public MainFrame() {
        // Configurações da janela principal
        setTitle("Sistema de Acesso");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 500);
        setLocationRelativeTo(null); // centraliza na tela

        // CardLayout para alternar entre telas
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Instanciando telas
        login = new LoginFrame(this);
        cadastro = new CadastroFrame();
        cadastroViagem = new CadastroViagemFrame();

        // Instancia home depois, quando for mostrar, para passar o viajante

        // Adiciona os painéis ao cardPanel
        cardPanel.add(login.getPanel(), "login");
        cardPanel.add(cadastro.getPanel(), "cadastro");
        cardPanel.add(cadastroViagem.getPanel(), "cadastroViagem");

        // Define o painel principal como cardPanel
        setContentPane(cardPanel);

        // MOSTRA A TELA DE LOGIN PRIMEIRO
        cardLayout.show(cardPanel, "login");

        // Lógica de troca de telas
        login.getTelaDeCadastroButton().addActionListener(e -> cardLayout.show(cardPanel, "cadastro"));
        cadastro.getTelaDeLoginButton().addActionListener(e -> cardLayout.show(cardPanel, "login"));

        // Exemplo: para abrir tela de cadastro de viagem (você deve disparar isso da sua lógica)
        // cadastroViagem.addCancelarListener(e -> voltarParaHomeOuLogin());

        // Opcional: Se quiser um método para abrir cadastro de viagem
        // abrirCadastroViagem();

        // *** Exemplo de como configurar o botão Cancelar do cadastro de viagem ***
        cadastroViagem.addCancelarListener(e -> {
            // Aqui decide para onde voltar, exemplo volta para login
            cardLayout.show(cardPanel, "login");
        });
    }

    // Método para mostrar qualquer tela pelo nome
    public void mostrarTela(String nomeTela) {
        cardLayout.show(cardPanel, nomeTela);
    }

    // Abrir home com viajante, substituindo o conteúdo da janela (não CardLayout)
    public void abrirHome(Viajante viajante) {
        home = new HomeFrame(viajante);
        setContentPane(home.getPanel());
        revalidate();
        repaint();
    }

    // Exemplo de método para abrir a tela de cadastro de viagem
    public void abrirCadastroViagem() {
        cardLayout.show(cardPanel, "cadastroViagem");
    }

    // Caso queira voltar para home ou login (exemplo)
    private void voltarParaHomeOuLogin() {
        // Aqui você pode controlar lógica para voltar para home ou login
        cardLayout.show(cardPanel, "login");
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        // Tema FlatLaf com personalização
        FlatLightLaf.setup();

        UIManager.put("Button.arc", 20);
        UIManager.put("TextComponent.arc", 10);
        UIManager.put("Component.arc", 10);

        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
