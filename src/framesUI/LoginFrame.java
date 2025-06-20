package framesUI;

import main.entities.Viajante;
import main.repositories.ViajanteRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class LoginFrame {
    private final JPanel mainPanel;
    private final JButton loginButton;
    private final JButton telaDeCadastroButton;
    private final JTextField inputEmailLogin;
    private final JPasswordField inputSenhaLogin;

    public LoginFrame() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Painel da esquerda (formulário de login)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        JLabel title = new JLabel("Login");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(33, 70, 120));

        JLabel subtitle = new JLabel("Bem-vindo de volta ao TravelPOO!");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(new Color(80, 80, 80));

        inputEmailLogin = new JTextField(20);
        inputSenhaLogin = new JPasswordField(20);

        JLabel emailLabel = new JLabel("Email");
        JLabel senhaLabel = new JLabel("Senha");

        loginButton = new JButton("Entrar");
        styleButton(loginButton, new Color(0, 123, 255));

        JLabel cadastroRedirect = new JLabel("Não tem uma conta? Cadastre-se");
        cadastroRedirect.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cadastroRedirect.setForeground(new Color(100, 100, 100));
        cadastroRedirect.setFont(new Font("SansSerif", Font.PLAIN, 12));

        telaDeCadastroButton = new JButton();
        telaDeCadastroButton.setVisible(false);
        cadastroRedirect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                telaDeCadastroButton.doClick();
            }
        });

        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    ViajanteRepository viajanteRepository = new ViajanteRepository("viajante.txt");


                    Viajante viajante = new Viajante(null, getEmail(), getSenha(), null, viajanteRepository);
                    viajante.login();

                    JOptionPane.showMessageDialog(mainPanel, "Login bem-sucedido!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (RuntimeException exception){
                    JOptionPane.showMessageDialog(mainPanel, "Credenciais incorretas!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        styleLabel(emailLabel);
        styleLabel(senhaLabel);
        styleField(inputEmailLogin);
        styleField(inputSenhaLogin);

        int row = 0;
        gbc.gridy = row++;
        formPanel.add(title, gbc);

        gbc.gridy = row++;
        formPanel.add(subtitle, gbc);

        gbc.gridy = row++;
        formPanel.add(emailLabel, gbc);

        gbc.gridy = row++;
        formPanel.add(inputEmailLogin, gbc);

        gbc.gridy = row++;
        formPanel.add(senhaLabel, gbc);

        gbc.gridy = row++;
        formPanel.add(inputSenhaLogin, gbc);

        gbc.gridy = row++;
        formPanel.add(loginButton, gbc);

        gbc.gridy = row++;
        formPanel.add(cadastroRedirect, gbc);

        gbc.gridy = row++;
        formPanel.add(telaDeCadastroButton, gbc);

        // Painel da direita (imagem com fundo azul escuro)
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(new Color(33, 70, 120));
        imagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/resources/images/viagem_site.png"));
            Image scaledImage = originalIcon.getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            JLabel imageLabel = new JLabel(scaledIcon);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imagePanel.add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel errorImage = new JLabel("Imagem não encontrada");
            errorImage.setHorizontalAlignment(SwingConstants.CENTER);
            errorImage.setForeground(Color.WHITE);
            imagePanel.add(errorImage, BorderLayout.CENTER);
        }

        JPanel contentPanel = new JPanel(new GridLayout(1, 2));
        contentPanel.add(formPanel);
        contentPanel.add(imagePanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }

    private void styleField(JTextField field) {
        field.setPreferredSize(new Dimension(0, 40));
        field.setMinimumSize(new Dimension(0, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }

    private void styleLabel(JLabel label) {
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(new Color(60, 60, 60));
    }

    private void styleButton(JButton button, Color background) {
        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(0, 40));
        button.setMinimumSize(new Dimension(0, 40));
    }

    // Getters para uso externo
    public JPanel getPanel() {
        return mainPanel;
    }

    public JButton getTelaDeCadastroButton() {
        return telaDeCadastroButton;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public String getEmail() {
        return inputEmailLogin.getText();
    }

    public String getSenha() {
        return new String(inputSenhaLogin.getPassword());
    }
}