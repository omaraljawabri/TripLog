package frontend.framesUI;

import backend.main.entities.Viajante;

import javax.swing.*;
import java.awt.*;

public class HomeFrame {
    private final JPanel mainPanel;
    private final JButton cadastrarViagemButton;
    private final JButton minhasViagensButton;
    private final String nomeUsuario;

    public HomeFrame(Viajante viajante) {
        this.nomeUsuario = viajante.getNome();
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Center wrapper com GridBagLayout para centralizar vertical e horizontalmente
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        // Painel de conteúdo com BoxLayout vertical e margens
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        // --- Imagem (topo) ------------------------------------------------
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("../resources/images/planeLogo.png"));
            Image originalImage = originalIcon.getImage();

            // Define tamanho proporcional (largura fixa, altura ajustada)
            int targetWidth = 320;
            int originalWidth = originalIcon.getIconWidth();
            int originalHeight = originalIcon.getIconHeight();
            int targetHeight = (int) ((double) originalHeight / originalWidth * targetWidth);

            Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(scaledImage));
            imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(imgLabel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        } catch (Exception e) {
            JLabel placeholder = new JLabel("Imagem não encontrada");
            placeholder.setForeground(new Color(33, 70, 120));
            placeholder.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(placeholder);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        // --- Texto --------------------------------------------------------
        JLabel title = new JLabel("Bem‑vindo, " + nomeUsuario + ", ao TravelPOO!");
        title.setFont(new Font("Serif", Font.BOLD, 34));
        title.setForeground(new Color(33, 70, 120));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea description = new JTextArea(
                "Aqui você pode registrar suas viagens, passeios, gastos e muito mais.\n\n" +
                        "➡ Para começar, clique em \"Cadastrar nova viagem\" e preencha detalhes como destinos, " +
                        "restaurantes, eventos e valores gastos.\n\n" +
                        "➡ Se quiser rever ou editar percursos que já cadastrou, escolha \"Minhas viagens\". " +
                        "Lá você encontra todas as suas viagens organizadas em um só lugar!\n\n" +
                        "Boa jornada e divirta‑se usando o TravelPOO!"
        );
        description.setFont(new Font("SansSerif", Font.PLAIN, 16));
        description.setForeground(new Color(60, 60, 60));
        description.setOpaque(false);
        description.setEditable(false);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setAlignmentX(Component.CENTER_ALIGNMENT);
        description.setMaximumSize(new Dimension(700, Integer.MAX_VALUE));

        contentPanel.add(title);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        contentPanel.add(description);

        // Configuração do GridBagConstraints para centralização vertical e horizontal
        GridBagConstraints gbcCenter = new GridBagConstraints();
        gbcCenter.gridx = 0;
        gbcCenter.gridy = 0;
        gbcCenter.weightx = 1;
        gbcCenter.weighty = 1;
        gbcCenter.fill = GridBagConstraints.NONE;
        gbcCenter.anchor = GridBagConstraints.CENTER;

        centerWrapper.add(contentPanel, gbcCenter);

        // --- Rodapé: botões ---
        JPanel footerPanel = new JPanel(new GridBagLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 40, 60));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 10, 0, 10);

        cadastrarViagemButton = new JButton("Cadastrar nova viagem");
        stylePrimaryButton(cadastrarViagemButton);

        minhasViagensButton = new JButton("Minhas viagens");
        styleSecondaryButton(minhasViagensButton);

        footerPanel.add(cadastrarViagemButton, gbc);
        footerPanel.add(minhasViagensButton, gbc);

        mainPanel.add(centerWrapper, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
    }

    private void stylePrimaryButton(JButton button) {
        Color normalBG = new Color(0, 123, 255);
        Color hoverBG = new Color(0, 105, 217);

        button.setBackground(normalBG);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setPreferredSize(new Dimension(240, 48));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverBG);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(normalBG);
            }
        });
    }

    private void styleSecondaryButton(JButton button) {
        Color borderColor = new Color(0, 123, 255);
        Color hoverBG = new Color(230, 240, 255);

        button.setBackground(Color.WHITE);
        button.setForeground(borderColor);
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(borderColor, 2));
        button.setPreferredSize(new Dimension(180, 48));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverBG);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public JButton getCadastrarViagemButton() {
        return cadastrarViagemButton;
    }

    public JButton getMinhasViagensButton() {
        return minhasViagensButton;
    }
}
