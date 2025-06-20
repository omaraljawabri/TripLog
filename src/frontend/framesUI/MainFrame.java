package frontend.framesUI;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;

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
        LoginFrame login = new LoginFrame();
        CadastroFrame cadastro = new CadastroFrame();

        // Adiciona os painéis ao cardPanel
        cardPanel.add(login.getPanel(), "login");
        cardPanel.add(cadastro.getPanel(), "cadastro");

        // Define o painel principal como cardPanel
        setContentPane(cardPanel);

        // MOSTRA A TELA DE LOGIN PRIMEIRO
        cardLayout.show(cardPanel, "login");

        // Lógica de troca de telas
        login.getTelaDeCadastroButton().addActionListener(e -> cardLayout.show(cardPanel, "cadastro"));
        cadastro.getTelaDeLoginButton().addActionListener(e -> cardLayout.show(cardPanel, "login"));
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
