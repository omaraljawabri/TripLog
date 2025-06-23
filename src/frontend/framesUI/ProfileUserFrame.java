package frontend.framesUI;

import backend.main.entities.Viagem;
import backend.main.entities.Viajante;
import backend.main.repositories.ViajanteRepository;
import backend.main.services.ViajanteService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/** Tela de perfil do usuário com campos centralizados e responsivos. */
public class ProfileUserFrame {

    /* ----------------- Componentes principais ----------------- */
    private final JPanel   mainPanel;
    private final JButton  btnInicio;
    private final JButton  btnListaViagens;
    private final JButton  btnLogout;

    /* Campos editáveis */
    private JTextField     nomeField;
    private JPasswordField pwdAntigaField;
    private JPasswordField pwdNovaField;
    private JPasswordField pwdConfirmaField;

    public ProfileUserFrame(Viajante viajante) {
        /* ---------- Painel raiz ---------- */
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        /* ---------- TOPO ---------- */
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

        try {
            ImageIcon ico   = new ImageIcon(getClass().getResource("../resources/images/ProfileIcon.png"));
            Image     scaled= ico.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(scaled));
            imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            topPanel.add(imgLabel);
        } catch (Exception ignored){}

        JLabel titulo = new JLabel("Meu Perfil");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(new Color(33, 70, 120));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(titulo);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        /* ---------- CENTRO (dados + senha) ---------- */
        JPanel dadosPanel = new JPanel(new GridBagLayout());
        dadosPanel.setBackground(Color.WHITE);
        dadosPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(10, 10, 10, 10);
        gbc.weightx   = 1.0;
        gbc.gridx     = 0;

        int row = 0;

        /* Nome */
        addLabel(dadosPanel, gbc, row++, "Nome");
        nomeField = addEditableField(dadosPanel, gbc, row++, viajante.getNome());

        /* -------- Seção Alterar Senha -------- */
        addSectionHeader(dadosPanel, gbc, row++, "Alterar Senha");

        addLabel(dadosPanel, gbc, row++, "Senha atual");
        pwdAntigaField = addPasswordField(dadosPanel, gbc, row++);

        addLabel(dadosPanel, gbc, row++, "Nova senha");
        pwdNovaField = addPasswordField(dadosPanel, gbc, row++);

        addLabel(dadosPanel, gbc, row++, "Confirmar nova senha");
        pwdConfirmaField = addPasswordField(dadosPanel, gbc, row++);

        /* Botão Confirmar alterações */
        gbc.gridy      = row++;
        gbc.anchor     = GridBagConstraints.LINE_START;
        JButton btnConfirmarSenha = new JButton("Confirmar alterações");
        stylePrimaryButton(btnConfirmarSenha);
        btnConfirmarSenha.setPreferredSize(new Dimension(220, 42));
        dadosPanel.add(btnConfirmarSenha, gbc);

        /* Botão Logout (vermelho) */
        gbc.gridy      = row++;
        gbc.anchor     = GridBagConstraints.LINE_START;
        btnLogout = new JButton("Logout");
        styleLogoutButton(btnLogout);
        dadosPanel.add(btnLogout, gbc);

        /* ---------- Envelopes de centralização ---------- */
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        dadosPanel.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
        container.add(Box.createVerticalStrut(20));
        container.add(dadosPanel);
        container.add(Box.createVerticalStrut(20));

        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.X_AXIS));
        centerWrapper.setBackground(Color.WHITE);
        centerWrapper.add(Box.createHorizontalGlue());
        centerWrapper.add(container);
        centerWrapper.add(Box.createHorizontalGlue());

        JScrollPane scrollPane = new JScrollPane(centerWrapper);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        /* ---------- RODAPÉ ---------- */
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        footerPanel.setBackground(Color.WHITE);

        btnInicio       = new JButton("Voltar para Início");
        btnListaViagens = new JButton("Lista de Viagens");

        stylePrimaryButton(btnInicio);
        styleSecondaryButton(btnListaViagens);

        footerPanel.add(btnInicio);
        footerPanel.add(btnListaViagens);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        btnConfirmarSenha.addActionListener(e -> {
            try{
                String novaSenha = String.valueOf(pwdNovaField.getPassword());
                String confirmarSenha = String.valueOf(pwdConfirmaField.getPassword());
                String senhaAntiga = String.valueOf(pwdAntigaField.getPassword());

                if (nomeField.getText().isEmpty() || novaSenha.isEmpty() ||senhaAntiga.isEmpty() ||
                        confirmarSenha.isEmpty()){
                    JOptionPane.showMessageDialog(mainPanel, "Todos os campos devem ser preenchidos", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!novaSenha.equals(confirmarSenha)){
                    JOptionPane.showMessageDialog(mainPanel,
                            "A confirmação não coincide com a nova senha!",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ViajanteRepository viajanteRepository = new ViajanteRepository("viajante.ser");
                ViajanteService viajanteService = new ViajanteService(viajanteRepository);

                viajanteService.editarViajante(viajante.getEmail(), nomeField.getText(), senhaAntiga, novaSenha);
                JOptionPane.showMessageDialog(mainPanel, "Credenciais atualizadas com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex){
                JOptionPane.showMessageDialog(mainPanel, "Erro ao alterar credenciais, tente novamente!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /* -------------------- Helpers de criação -------------------- */
    private void addLabel(JPanel p, GridBagConstraints gbc, int y, String text){
        gbc.gridy = y;
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 14));
        l.setForeground(new Color(60,60,60));
        p.add(l, gbc);
    }
    private void addSectionHeader(JPanel p, GridBagConstraints gbc, int y, String text){
        gbc.gridy = y;
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 15));
        l.setForeground(new Color(33,70,120));
        p.add(l, gbc);
    }
    private JTextField addEditableField(JPanel p, GridBagConstraints gbc, int y, String value){
        gbc.gridy = y;
        JTextField f = new JTextField(value);
        f.setEditable(false);
        f.setBackground(new Color(245,245,245));
        f.setFont(new Font("SansSerif", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200)),
                BorderFactory.createEmptyBorder(8,10,8,10)));
        JButton btnEditar = new JButton("Editar");
        styleEditButton(btnEditar);
        btnEditar.addActionListener(e -> {
            boolean edit = !f.isEditable();
            f.setEditable(edit);
            f.setBackground(edit? Color.WHITE : new Color(245,245,245));
            btnEditar.setText(edit? "Salvar":"Editar");
        });
        JPanel w = new JPanel(new BorderLayout(8,0));
        w.setBackground(Color.WHITE);
        w.add(f, BorderLayout.CENTER);
        w.add(btnEditar, BorderLayout.EAST);
        p.add(w, gbc);
        return f;
    }
    private JPasswordField addPasswordField(JPanel p, GridBagConstraints gbc, int y){
        gbc.gridy = y;
        JPasswordField f = new JPasswordField();
        f.setBackground(Color.WHITE);
        f.setFont(new Font("SansSerif", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200)),
                BorderFactory.createEmptyBorder(8,10,8,10)));
        p.add(f, gbc);
        return f;
    }

    /* -------------------- Estilização de botões -------------------- */
    private void stylePrimaryButton(JButton b){
        Color bg=new Color(0,123,255), hover=new Color(0,105,217);
        b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setBorder(BorderFactory.createEmptyBorder(10,25,10,25));
        b.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent e){b.setBackground(hover);}
            public void mouseExited (java.awt.event.MouseEvent e){b.setBackground(bg);}
        });
    }
    private void styleSecondaryButton(JButton b){
        Color border = new Color(0,123,255), hover = new Color(230,240,255);
        b.setBackground(Color.WHITE);
        b.setForeground(border);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setBorder(BorderFactory.createLineBorder(border, 2));
        b.setPreferredSize(new Dimension(180, 45)); // ✅ MESMO TAMANHO DO BOTÃO PRIMÁRIO

        b.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent e){ b.setBackground(hover); }
            public void mouseExited (java.awt.event.MouseEvent e){ b.setBackground(Color.WHITE); }
        });
    }
    private void styleLogoutButton(JButton b){
        Color bg=new Color(220,53,69), hover=new Color(200,35,51);
        b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD,14));
        b.setBorder(BorderFactory.createEmptyBorder(10,25,10,25));
        b.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent e){b.setBackground(hover);}
            public void mouseExited (java.awt.event.MouseEvent e){b.setBackground(bg);}
        });
    }
    private void styleEditButton(JButton b){
        b.setBackground(new Color(0,123,255));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif",Font.BOLD,12));
        b.setPreferredSize(new Dimension(80,32));
    }

    /* -------------------- Getters & Listeners -------------------- */
    public JButton getBtnInicio()        { return btnInicio; }
    public JButton getBtnListaViagens() { return btnListaViagens; }
    public JButton getBtnLogout()       { return btnLogout;    }
    public JPanel  getPanel()           { return mainPanel;    }

    public void setBtnInicioListener(Runnable callback){
        btnInicio.addActionListener(e -> callback.run());
    }

    public void setBtnListaViagensListener(JFrame framePai,
                                           Viajante viajante,
                                           Runnable abrirPerfilCallback,
                                           Runnable abrirCadastroCallback) {
        btnListaViagens.addActionListener(e -> {
            ListaDeViagensFrame lista = new ListaDeViagensFrame(
                    viajante,
                    viajante.getViagens(),
                    v -> JOptionPane.showMessageDialog(framePai, "Detalhes de " + v.getLugarDeChegada()),
                    v -> JOptionPane.showMessageDialog(framePai, "Excluída " + v.getLugarDeChegada()),
                    evt -> abrirPerfilCallback.run(),
                    evt -> abrirCadastroCallback.run()
            );
            framePai.setContentPane(lista.getPanel());
            framePai.revalidate();
            framePai.repaint();
        });
    }
}
