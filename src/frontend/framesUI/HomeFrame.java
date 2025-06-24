package frontend.framesUI;

import backend.main.entities.Viajante;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

public class HomeFrame {

    /* ---------- Campos ---------- */
    private final JPanel mainPanel;
    private final JButton cadastrarViagemButton;
    private final JButton minhasViagensButton;
    private final JButton meuPerfilButton;

    /* Elementos que precisam ser redimensionados dinamicamente */
    private final JLabel imgLabel;
    private final Image originalImage;
    private final JTextArea description;
    private final JLabel title;

    /* ---------- Construtor ---------- */
    public HomeFrame(Viajante viajante) {

        /* ---------- Painel principal (BorderLayout) ---------- */
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        /* ---------- Painel central com ScrollPane ---------- */
        // Conteúdo central em Y (imagem, título, descrição)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        // Centraliza vertical/horizontalmente (e deixa rolar se ficar pequeno)
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(contentPanel, new GridBagConstraints());

        JScrollPane scrollPane = new JScrollPane(
                centerWrapper,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setBorder(null); // sem borda de scrollPane

        /* ---------- Imagem (topo) ---------- */
        Image img;
        try {
            ImageIcon ico = new ImageIcon(getClass().getResource("/images/planeLogo.png"));
            img = ico.getImage();
        } catch (Exception e) {
            // Se falhar, cria um retângulo placeholder
            img = new BufferedImage(320, 120, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = ((BufferedImage) img).createGraphics();
            g2.setPaint(new Color(230, 230, 230));
            g2.fillRect(0, 0, 320, 120);
            g2.setPaint(Color.GRAY);
            g2.drawString("Imagem não encontrada", 40, 60);
            g2.dispose();
        }
        originalImage = img;
        imgLabel = new JLabel(new ImageIcon(img)); // redimensionaremos depois
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(imgLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        /* ---------- Título ---------- */
        title = new JLabel("Boas-vindas, " + viajante.getNome() + ", ao TripLog!");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(new Color(33, 70, 120));
        contentPanel.add(title);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        /* ---------- Descrição ---------- */
        description = new JTextArea(
                "Aqui você pode registrar suas viagens, passeios, gastos e muito mais.\n\n" +
                        "➡ Para começar, clique em \"Cadastrar nova viagem\" e preencha detalhes com dados da viagem, " +
                        "hospedagem, deslocamento e atividades realizadas, como eventos, restaurantes e os valores gastos.\n\n" +
                        "➡ Para rever ou editar viagens já cadastradas, escolha \"Minhas viagens\". " +
                        "Lá, você encontra todas as suas viagens organizadas em um só lugar!\n\n" +
                        "➡ Acesse seu perfil clicando em \" Meu perfil\". \n\n" +
                        "Boa jornada e divirta-se usando o TripLog!"
        );
        description.setOpaque(false);
        description.setEditable(false);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setAlignmentX(Component.CENTER_ALIGNMENT);
        description.setMaximumSize(new Dimension(900, Integer.MAX_VALUE));
        contentPanel.add(description);

        /* ---------- Rodapé com botões ---------- */
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 40, 60));

        cadastrarViagemButton = new JButton("Cadastrar nova viagem");
        minhasViagensButton   = new JButton("Minhas viagens");
        meuPerfilButton   = new JButton("Meu perfil");

        stylePrimaryButton(cadastrarViagemButton);
        styleSecondaryButton(minhasViagensButton);
        styleSecondaryButton(meuPerfilButton);

        footerPanel.add(cadastrarViagemButton);
        footerPanel.add(minhasViagensButton);
        footerPanel.add(meuPerfilButton);

        /* ---------- Junta tudo ---------- */
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        /* ---------- Responsividade ---------- */
        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajustarLayout(mainPanel.getWidth());
            }
        });
        ajustarLayout( /* inicial */ 800);
    }

    public void setCadastrarViagemButtonListener(java.awt.event.ActionListener listener) {
        cadastrarViagemButton.addActionListener(listener);
    }

    public void setMinhasViagensButtonListener(java.awt.event.ActionListener listener) {
        minhasViagensButton.addActionListener(listener);
    }

    public void setMeuPerfilButtonListener(java.awt.event.ActionListener listener) {
        meuPerfilButton.addActionListener(listener);
    }

    /* ---------- Responsividade: ajusta imagem, fontes, margens ---------- */
    private void ajustarLayout(int larguraPainel) {

        /* Imagem – usa até 60 % da largura, mas entre 160 px e 480 px */
        int imgW = Math.max(160, Math.min((int) (larguraPainel * 0.6), 480));
        int imgH = (int) ((double) originalImage.getHeight(null) /
                originalImage.getWidth(null) * imgW);
        imgLabel.setIcon(new ImageIcon(originalImage.getScaledInstance(
                imgW, imgH, Image.SCALE_SMOOTH)));

        /* Fontes – escala simples */
        float fator = (float) larguraPainel / 800f;          // 1.0 quando 800 px
        fator = Math.max(0.8f, Math.min(fator, 1.25f));      // limites
        title.setFont(title.getFont().deriveFont(34f * fator));
        description.setFont(description.getFont().deriveFont(16f * fator));

        /* Botões – ocupam toda a largura disponível em telas pequenas */
        int btnWidth = larguraPainel < 500 ? larguraPainel - 120 : 240;
        cadastrarViagemButton.setPreferredSize(new Dimension(btnWidth, 48));
        minhasViagensButton  .setPreferredSize(new Dimension(btnWidth < 200 ? 200 : 180, 48));
        meuPerfilButton  .setPreferredSize(new Dimension(btnWidth < 200 ? 200 : 180, 48));

        mainPanel.revalidate();
    }

    /* ---------- Estilos ---------- */
    private void stylePrimaryButton(JButton button) {
        Color bg    = new Color(0, 123, 255);
        Color hover = new Color(0, 105, 217);

        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { button.setBackground(hover); }
            public void mouseExited (java.awt.event.MouseEvent e) { button.setBackground(bg);    }
        });
    }

    private void styleSecondaryButton(JButton button) {
        Color border = new Color(0, 123, 255);
        Color hover  = new Color(230, 240, 255);

        button.setBackground(Color.WHITE);
        button.setForeground(border);
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(border, 2));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { button.setBackground(hover); }
            public void mouseExited (java.awt.event.MouseEvent e) { button.setBackground(Color.WHITE); }
        });
    }

    /* ---------- Getters públicos ---------- */
    public JPanel   getPanel()                { return mainPanel; }
    public JButton  getCadastrarViagemButton(){ return cadastrarViagemButton; }
    public JButton  getMinhasViagensButton()  { return minhasViagensButton;  }
    public JButton getMeuPerfilButton() {return meuPerfilButton; }
}
