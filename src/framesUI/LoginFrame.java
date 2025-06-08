package framesUI;

import javax.swing.*;

public class LoginFrame {
    private JPanel plnContainerPrincipal;
    private JPanel plnButton;
    private JButton loginButton;
    private JPanel plnCenterUI;
    private JTextField emailInputLogin;
    private JTextField senhaInputLogin;
    private JLabel usuarioLabel;
    private JLabel senhaLabel;
    private JPanel plnHeader;
    private JLabel bemVindoLabel;
    private JButton telaDeCadastroButton;
    private JLabel naoPossuiContaLabel;

    // Getter do painel principal
    public JPanel getPanel() {
        return plnContainerPrincipal;
    }

    // Getters para botões
    public JButton getTelaDeCadastroButton() {
        return telaDeCadastroButton;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    // Getters para os campos de entrada
    public String getEmail() {
        return emailInputLogin.getText();
    }

    public String getSenha() {
        return senhaInputLogin.getText();
    }
}
