package framesUI;

import javax.swing.*;

public class CadastroFrame {
    private JPanel plnContainerPrincipal;
    private JPanel plnCenterUI;
    private JPanel plnButton;
    private JButton cadastrarButton;
    private JButton telaDeLoginButton;
    private JLabel possuiContaLabel;
    private JLabel sejaBemVindoLabel;
    private JLabel telaDeCadastroLabel;
    private JPanel plnHeader;
    private JTextField inputEmailCadastro;
    private JTextField inputSenhaCadastro;
    private JTextField inputConfirmarSenhaCadastro;
    private JLabel emailLabel;
    private JLabel senhaLabel;
    private JLabel confirmarSenhaLabel;

    // GETTERS PARA ACESSO EXTERNO

    public JPanel getPanel() {
        return plnContainerPrincipal;
    }

    public JButton getTelaDeLoginButton() {
        return telaDeLoginButton;
    }

    public JButton getCadastrarButton() {
        return cadastrarButton;
    }

    public String getEmail() {
        return inputEmailCadastro.getText();
    }

    public String getSenha() {
        return inputSenhaCadastro.getText();
    }

    public String getConfirmarSenha() {
        return inputConfirmarSenhaCadastro.getText();
    }
}
