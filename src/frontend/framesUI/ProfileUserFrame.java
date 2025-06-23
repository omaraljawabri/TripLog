package frontend.framesUI;

import backend.main.entities.Viajante;

import javax.swing.*;
import java.awt.*;

/**
 * Tela de perfil do usuário com campos centralizados e responsivos.
 */
public class ProfileUserFrame {

    private final JPanel mainPanel;
    private final JButton btnInicio;
    private final JButton btnListaViagens;
    private final JButton btnLogout;  // botão logout vermelho

    private JTextField nomeField;
    private JTextField emailField;

    public ProfileUserFrame(Viajante viajante) {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        /* ---------- TOPO ---------- */
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

        try {
            ImageIcon ico = new ImageIcon(getClass().getResource("../resources/images/ProfileIcon.png"));
            Image scaled = ico.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(scaled));
            imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            topPanel.add(imgLabel);
        } catch (Exception e) {
            JLabel placeholder = new JLabel("Imagem não encontrada");
            placeholder.setForeground(new Color(33, 70, 120));
            placeholder.setAlignmentX(Component.CENTER_ALIGNMENT);
            topPanel.add(placeholder);
        }

        JLabel titulo = new JLabel("Meu Perfil");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(new Color(33, 70, 120));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(titulo);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        /* ---------- DADOS ---------- */
        JPanel dadosPanel = new JPanel(new GridBagLayout());
        dadosPanel.setBackground(Color.WHITE);
        dadosPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        int row = 0;
        addLabel(dadosPanel, gbc, row++, "Nome");
        nomeField = addEditableField(dadosPanel, gbc, row++, viajante.getNome());

        addLabel(dadosPanel, gbc, row++, "E‑mail cadastrado");
        emailField = addEditableField(dadosPanel, gbc, row++, viajante.getEmail());

        // Botão Logout vermelho abaixo do email
        gbc.gridy = row++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(20, 10, 10, 10);

        btnLogout = new JButton("Logout");
        styleLogoutButton(btnLogout);
        dadosPanel.add(btnLogout, gbc);

        // Painel com largura máxima e centralizado
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setBackground(Color.WHITE);
        dadosPanel.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
        containerPanel.add(Box.createVerticalStrut(20));
        containerPanel.add(dadosPanel);
        containerPanel.add(Box.createVerticalStrut(20));

        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.X_AXIS));
        centerWrapper.setBackground(Color.WHITE);
        centerWrapper.add(Box.createHorizontalGlue());
        centerWrapper.add(containerPanel);
        centerWrapper.add(Box.createHorizontalGlue());

        JScrollPane scrollPane = new JScrollPane(centerWrapper);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        /* ---------- FOOTER ---------- */
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        footerPanel.setBackground(Color.WHITE);

        btnInicio = new JButton("Voltar para Início");
        btnListaViagens = new JButton("Lista de Viagens");

        stylePrimaryButton(btnInicio);
        styleSecondaryButton(btnListaViagens);

        footerPanel.add(btnInicio);
        footerPanel.add(btnListaViagens);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);
    }

    private void addLabel(JPanel p, GridBagConstraints gbc, int y, String text) {
        gbc.gridy = y;
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 14));
        l.setForeground(new Color(60, 60, 60));
        p.add(l, gbc);
    }

    private JTextField addEditableField(JPanel p, GridBagConstraints gbc, int y, String value) {
        gbc.gridy = y;
        gbc.gridwidth = 1;

        JTextField f = new JTextField(value);
        f.setEditable(false);
        f.setBackground(new Color(245, 245, 245));
        f.setFont(new Font("SansSerif", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        JButton btnEditar = new JButton("Editar");
        styleEditButton(btnEditar);

        btnEditar.addActionListener(e -> {
            if (f.isEditable()) {
                f.setEditable(false);
                f.setBackground(new Color(245, 245, 245));
                btnEditar.setText("Editar");
                // Aqui você pode persistir a mudança se desejar
            } else {
                f.setEditable(true);
                f.setBackground(Color.WHITE);
                btnEditar.setText("Salvar");
            }
        });

        JPanel fieldWrapper = new JPanel(new BorderLayout(8, 0));
        fieldWrapper.setBackground(Color.WHITE);
        fieldWrapper.add(f, BorderLayout.CENTER);
        fieldWrapper.add(btnEditar, BorderLayout.EAST);

        p.add(fieldWrapper, gbc);

        return f;
    }

    /* ----------- Estilo de botões ----------- */
    private void stylePrimaryButton(JButton button) {
        Color normalBG = new Color(0, 123, 255);
        Color hoverBG = new Color(0, 105, 217);

        button.setBackground(normalBG);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 45));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverBG);
            }

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
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(borderColor, 2));
        button.setPreferredSize(new Dimension(180, 45));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverBG);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });
    }

    private void styleLogoutButton(JButton button) {
        Color normalBG = new Color(220, 53, 69); // vermelho Bootstrap-ish
        Color hoverBG = new Color(200, 35, 51);

        button.setBackground(normalBG);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 45));
        button.setBorder(BorderFactory.createEmptyBorder());

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverBG);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(normalBG);
            }
        });
    }

    private void styleEditButton(JButton button) {
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(80, 32));
    }

    /* -------- Getters -------- */
    public JButton getBtnInicio() {
        return btnInicio;
    }

    public JButton getBtnListaViagens() {
        return btnListaViagens;
    }

    public JButton getBtnLogout() {
        return btnLogout;
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    /**
     * Configura listener para o botão de voltar para o início.
     * Recebe JFrame pai e o objeto Viajante para criar a tela Home.
     */
    public void setBtnInicioListener(JFrame framePai, Viajante viajante) {
        btnInicio.addActionListener(e -> {
            HomeFrame home = new HomeFrame(viajante);
            framePai.setContentPane(home.getPanel());
            framePai.revalidate();
            framePai.repaint();
        });
    }
}
