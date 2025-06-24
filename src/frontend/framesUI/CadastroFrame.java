package frontend.framesUI;

import backend.main.entities.Viajante;
import backend.main.repositories.ViajanteRepository;
import backend.main.services.AuthService;
import backend.main.utils.EmailUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class CadastroFrame {
    private final JPanel mainPanel;
    private final JButton cadastrarButton;
    private final JButton telaDeLoginButton;
    private final JTextField inputNomeCadastro;        // novo campo nome
    private final JTextField inputEmailCadastro;
    private final JPasswordField inputSenhaCadastro;
    private final JPasswordField inputConfirmarSenhaCadastro;

    public CadastroFrame() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Painel da esquerda (formulário)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0); // espaçamento entre elementos
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        JLabel title = new JLabel("Criar conta");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(33, 70, 120));

        JLabel subtitle = new JLabel("Junte-se à plataforma TripLog!");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(new Color(80, 80, 80));

        inputNomeCadastro = new JTextField(20);       // inicializa campo nome
        inputEmailCadastro = new JTextField(20);
        inputSenhaCadastro = new JPasswordField(20);
        inputConfirmarSenhaCadastro = new JPasswordField(20);

        JLabel nomeLabel = new JLabel("Nome");        // novo label nome
        JLabel emailLabel = new JLabel("Email");
        JLabel senhaLabel = new JLabel("Senha");
        JLabel confirmarLabel = new JLabel("Confirmar Senha");

        cadastrarButton = new JButton("Cadastrar");
        styleButton(cadastrarButton, new Color(0, 123, 255));

        JLabel loginRedirect = new JLabel("Fazer login");
        loginRedirect.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginRedirect.setForeground(new Color(100, 100, 100));
        loginRedirect.setFont(new Font("SansSerif", Font.PLAIN, 12));

        telaDeLoginButton = new JButton();
        telaDeLoginButton.setVisible(false);
        loginRedirect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                telaDeLoginButton.doClick();
            }
        });

        cadastrarButton.addMouseListener(new java.awt.event.MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!EmailUtil.validarEmail(getEmail())){
                    JOptionPane.showMessageDialog(mainPanel, "Formato de email inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!getSenha().equals(getConfirmarSenha())){
                    JOptionPane.showMessageDialog(mainPanel, "As senhas não conferem!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    ViajanteRepository viajanteRepository = new ViajanteRepository("viajante.ser");
                    Viajante viajante = new Viajante(getNome(), getSenha(), getEmail());
                    AuthService authService = new AuthService(viajanteRepository);

                    authService.cadastrar(viajante);
                    telaDeLoginButton.doClick();
                } catch (RuntimeException ex){
                    JOptionPane.showMessageDialog(mainPanel, "Falha ao cadastrar usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        styleLabel(nomeLabel);     // aplica estilos no label nome
        styleLabel(emailLabel);
        styleLabel(senhaLabel);
        styleLabel(confirmarLabel);
        styleField(inputNomeCadastro);   // aplica estilos no campo nome
        styleField(inputEmailCadastro);
        styleField(inputSenhaCadastro);
        styleField(inputConfirmarSenhaCadastro);

        int row = 0;

        gbc.gridy = row++;
        formPanel.add(title, gbc);

        gbc.gridy = row++;
        formPanel.add(subtitle, gbc);

        gbc.gridy = row++;
        formPanel.add(nomeLabel, gbc);

        gbc.gridy = row++;
        formPanel.add(inputNomeCadastro, gbc);

        gbc.gridy = row++;
        formPanel.add(emailLabel, gbc);

        gbc.gridy = row++;
        formPanel.add(inputEmailCadastro, gbc);

        gbc.gridy = row++;
        formPanel.add(senhaLabel, gbc);

        gbc.gridy = row++;
        formPanel.add(inputSenhaCadastro, gbc);

        gbc.gridy = row++;
        formPanel.add(confirmarLabel, gbc);

        gbc.gridy = row++;
        formPanel.add(inputConfirmarSenhaCadastro, gbc);

        gbc.gridy = row++;
        formPanel.add(cadastrarButton, gbc);

        gbc.gridy = row++;
        formPanel.add(loginRedirect, gbc);

        gbc.gridy = row++;
        formPanel.add(telaDeLoginButton, gbc);

        // Painel da direita (imagem) com fundo azul escuro
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(new Color(33, 70, 120)); // azul escuro
        imagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/viagem_site.png"));
            Image scaledImage = originalIcon.getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            JLabel imageLabel = new JLabel(scaledIcon);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imagePanel.add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel errorImage = new JLabel("Imagem não encontrada");
            errorImage.setHorizontalAlignment(SwingConstants.CENTER);
            errorImage.setForeground(Color.WHITE); // contraste no fundo azul
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

    public JPanel getPanel() {
        return mainPanel;
    }

    public JButton getTelaDeLoginButton() {
        return telaDeLoginButton;
    }

    public JButton getCadastrarButton() {
        return cadastrarButton;
    }

    public String getNome() {
        return inputNomeCadastro.getText();
    }

    public String getEmail() {
        return inputEmailCadastro.getText();
    }

    public String getSenha() {
        return new String(inputSenhaCadastro.getPassword());
    }

    public String getConfirmarSenha() {
        return new String(inputConfirmarSenhaCadastro.getPassword());
    }
}
