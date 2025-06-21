package frontend.framesUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CadastroViagemFrame {

    /* ----------------- Campos da janela ----------------- */

    private final JPanel mainPanel;

    private JTextField inputLugarPartida;
    private JTextField inputLugarChegada;
    private JTextField inputSaldo;
    private JTextField inputDiasViagem;
    private JTextField inputCompanhia;

    private JPanel hospedagemEntriesPanel;
    private List<HospedagemEntry> hospedagemEntries;

    private JPanel atividadesEntriesPanel;
    private List<AtividadeEntry> atividadeEntries;

    private JPanel deslocamentoEntriesPanel;
    private List<DeslocamentoEntry> deslocamentoEntries;

    // Botões Salvar e Cancelar do rodapé
    private JButton btnSalvar;
    public void addCancelarListener(ActionListener listener) {
        btnCancelar.addActionListener(listener);
    }
    private JButton btnCancelar;


    /* ==================================================== */
    public CadastroViagemFrame() {

        /* ---------- Estrutura base ---------- */
        mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 30, 20, 30));

        montarTopo();
        montarCentro();
        montarRodape();  // <-- chamar método que cria o rodapé com os botões
    }

    /* ----------------------------- TOPO ----------------------------- */
    private void montarTopo() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);

        try {
            ImageIcon img = new ImageIcon(getClass().getResource("../resources/images/beachIcon.png"));
            Image scaled = img.getImage().getScaledInstance(120, -1, Image.SCALE_SMOOTH);
            topPanel.add(new JLabel(new ImageIcon(scaled)));
        } catch (Exception e) {
            /* se a imagem não for encontrada, apenas ignora */
        }

        JLabel title = new JLabel("Cadastro de Nova Viagem");
        title.setFont(new Font("Arial", Font.BOLD, 38));
        title.setForeground(new Color(33, 70, 120));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(title);

        mainPanel.add(topPanel, BorderLayout.NORTH);
    }

    /* ---------------------------- CENTRO --------------------------- */
    private void montarCentro() {

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 15, 4, 15);
        gbc.weightx = 1;

        /* ---------- Inputs principais ---------- */
        addLabel(centerPanel, gbc, 0, 0, "Local de Partida");
        addLabel(centerPanel, gbc, 1, 0, "Local de Chegada");
        addLabel(centerPanel, gbc, 2, 0, "Saldo");

        inputLugarPartida = addField(centerPanel, gbc, 0, 1);
        inputLugarChegada = addField(centerPanel, gbc, 1, 1);
        inputSaldo        = addField(centerPanel, gbc, 2, 1);

        addLabel(centerPanel, gbc, 0, 2, "Dias de Viagem");
        addLabel(centerPanel, gbc, 1, 2, "Companhia");

        inputDiasViagem = addField(centerPanel, gbc, 0, 3);
        inputCompanhia  = addField(centerPanel, gbc, 1, 3);

        /* ---------- Hospedagem ---------- */
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 3;
        gbc.fill  = GridBagConstraints.BOTH; gbc.weighty = 1;
        centerPanel.add(montarHospedagem(), gbc);

        /* ---------- Atividades ---------- */
        gbc.gridy = 5;
        centerPanel.add(montarAtividades(), gbc);

        /* ---------- Deslocamentos ---------- */
        gbc.gridy = 6;
        centerPanel.add(montarDeslocamentos(), gbc);

        /* ---------- Wrapper ---------- */
        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 0));
        wrap.setBackground(Color.WHITE);
        wrap.add(centerPanel);

        mainPanel.add(wrap, BorderLayout.CENTER);
    }

    // ---------- NOVO: montar rodapé com botões ----------
    private void montarRodape() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        footerPanel.setBackground(Color.WHITE);

        btnSalvar = new JButton("Salvar");
        stylePrimaryButton(btnSalvar);

        btnCancelar = new JButton("Cancelar");
        styleSecondaryButton(btnCancelar);

        footerPanel.add(btnSalvar);
        footerPanel.add(btnCancelar);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);
    }


    /* ======================  PAINEL HOSPEDAGEM  ====================== */
    private JPanel montarHospedagem() {

        JPanel hospedagemPanel = new JPanel(new BorderLayout());
        hospedagemPanel.setBackground(Color.WHITE);

        JLabel lbl = new JLabel("Hospedagem");
        styleLabelSemibold(lbl);
        hospedagemPanel.add(lbl, BorderLayout.NORTH);

        hospedagemEntriesPanel = new JPanel();
        hospedagemEntriesPanel.setLayout(new BoxLayout(hospedagemEntriesPanel, BoxLayout.Y_AXIS));
        hospedagemEntriesPanel.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(hospedagemEntriesPanel);
        scroll.setPreferredSize(new Dimension(800, 100));
        hospedagemPanel.add(scroll, BorderLayout.CENTER);

        JButton btnAddHosp = new JButton("Adicionar");
        styleAddButton(btnAddHosp);  // <-- aplicar estilo verde
        btnAddHosp.addActionListener(e -> addHospedagemEntry());
        JPanel btnP = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnP.setBackground(Color.WHITE);
        btnP.add(btnAddHosp);
        hospedagemPanel.add(btnP, BorderLayout.SOUTH);

        hospedagemEntries = new ArrayList<>();
        return hospedagemPanel;
    }

    /* ======================  PAINEL ATIVIDADES  ====================== */
    private JPanel montarAtividades() {

        JPanel atividadesPanel = new JPanel(new BorderLayout());
        atividadesPanel.setBackground(Color.WHITE);

        JLabel lbl = new JLabel("Atividades");
        styleLabelSemibold(lbl);
        atividadesPanel.add(lbl, BorderLayout.NORTH);

        atividadesEntriesPanel = new JPanel();
        atividadesEntriesPanel.setLayout(new BoxLayout(atividadesEntriesPanel, BoxLayout.Y_AXIS));
        atividadesEntriesPanel.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(atividadesEntriesPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setPreferredSize(new Dimension(800, 100));
        atividadesPanel.add(scroll, BorderLayout.CENTER);

        JButton btnAddAtv = new JButton("Adicionar");
        styleAddButton(btnAddAtv);  // <-- aplicar estilo verde
        btnAddAtv.addActionListener(e -> addAtividadeEntry());
        JPanel btnP = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnP.setBackground(Color.WHITE);
        btnP.add(btnAddAtv);
        atividadesPanel.add(btnP, BorderLayout.SOUTH);

        atividadeEntries = new ArrayList<>();
        return atividadesPanel;
    }

    /* ======================  PAINEL DESLOCAMENTOS  ====================== */
    private JPanel montarDeslocamentos() {

        JPanel deslocPanel = new JPanel(new BorderLayout());
        deslocPanel.setBackground(Color.WHITE);

        JLabel lbl = new JLabel("Deslocamentos");
        styleLabelSemibold(lbl);
        deslocPanel.add(lbl, BorderLayout.NORTH);

        deslocamentoEntriesPanel = new JPanel();
        deslocamentoEntriesPanel.setLayout(new BoxLayout(deslocamentoEntriesPanel, BoxLayout.Y_AXIS));
        deslocamentoEntriesPanel.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(deslocamentoEntriesPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setPreferredSize(new Dimension(800, 100));
        deslocPanel.add(scroll, BorderLayout.CENTER);

        JButton btnAddDesl = new JButton("Adicionar");
        styleAddButton(btnAddDesl);  // <-- aplicar estilo verde
        btnAddDesl.addActionListener(e -> addDeslocamentoEntry());
        JPanel btnP = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnP.setBackground(Color.WHITE);
        btnP.add(btnAddDesl);
        deslocPanel.add(btnP, BorderLayout.SOUTH);

        deslocamentoEntries = new ArrayList<>();
        return deslocPanel;
    }

    /* ===================  ENTRADA DE HOSPEDAGEM  ==================== */
    private class HospedagemEntry {
        private final JPanel panel;
        private final JButton btnRemove;

        public HospedagemEntry() {

            panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            panel.setBackground(Color.WHITE);

            panel.add(new JLabel("Nome Local:"));  panel.add(campo(200));
            panel.add(new JLabel("Noites:"));      panel.add(campo(60));
            panel.add(new JLabel("Valor Diária:"));panel.add(campo(80));

            btnRemove = new JButton("Remover");
            styleRemoveButton(btnRemove);  // <-- estilizar botão remover vermelho
            panel.add(btnRemove);

            btnRemove.addActionListener(e -> {
                hospedagemEntriesPanel.remove(panel);
                hospedagemEntries.remove(this);
                hospedagemEntriesPanel.revalidate();
                hospedagemEntriesPanel.repaint();
            });
        }

        private JTextField campo(int width) {
            JTextField f = new JTextField();
            f.setPreferredSize(new Dimension(width, 30));
            return f;
        }
        public JPanel getPanel() { return panel; }
    }

    /* =====================  ENTRADA DE ATIVIDADE  ==================== */
    private class AtividadeEntry {
        private JPanel panel;
        private JComboBox<String> tipoCombo;
        private JButton btnRemove;

        private JTextField nomeField;
        private JTextField horarioField;
        private JTextField temaField;
        private JTextField nomeLocalField;
        private JTextField nomeRestauranteField;
        private JTextField culinariaField;
        private JTextField pratoField;

        private JPanel camposEspecificosPanel;

        public AtividadeEntry() {
            panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.setBackground(Color.WHITE);
            panel.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Painel de campos específicos
            camposEspecificosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
            camposEspecificosPanel.setBackground(Color.WHITE);
            panel.add(camposEspecificosPanel);

            // Campos base
            nomeField            = campo(150);
            horarioField         = campo(150);
            temaField            = campo(150);
            nomeLocalField       = campo(150);
            nomeRestauranteField = campo(150);
            culinariaField       = campo(100);
            pratoField           = campo(150);

            // Combo e botão Remover dentro do painel
            tipoCombo = new JComboBox<>(new String[]{"Evento", "Passeio", "Restaurante"});
            tipoCombo.setPreferredSize(new Dimension(120, 30));
            tipoCombo.addActionListener(e -> rebuildCampos());

            btnRemove = new JButton("Remover");
            styleRemoveButton(btnRemove);  // <-- vermelho
            btnRemove.addActionListener(e -> {
                atividadesEntriesPanel.remove(panel);
                atividadeEntries.remove(this);
                atividadesEntriesPanel.revalidate();
                atividadesEntriesPanel.repaint();
            });

            rebuildCampos();
        }

        private void rebuildCampos() {
            camposEspecificosPanel.removeAll();

            // Tipo + botão Remover
            camposEspecificosPanel.add(new JLabel("Tipo:"));
            camposEspecificosPanel.add(tipoCombo);
            camposEspecificosPanel.add(btnRemove);

            // Campos comuns
            camposEspecificosPanel.add(new JLabel("Nome:"));
            camposEspecificosPanel.add(nomeField);
            camposEspecificosPanel.add(new JLabel("Horário:"));
            camposEspecificosPanel.add(horarioField);

            // Campos específicos
            String tipo = (String) tipoCombo.getSelectedItem();
            if ("Evento".equals(tipo)) {
                camposEspecificosPanel.add(new JLabel("Tema:"));
                camposEspecificosPanel.add(temaField);
            } else if ("Passeio".equals(tipo)) {
                camposEspecificosPanel.add(new JLabel("Local:"));
                camposEspecificosPanel.add(nomeLocalField);
            } else {
                camposEspecificosPanel.add(new JLabel("Rest.:"));
                camposEspecificosPanel.add(nomeRestauranteField);
                camposEspecificosPanel.add(new JLabel("Culinária:"));
                camposEspecificosPanel.add(culinariaField);
                camposEspecificosPanel.add(new JLabel("Prato:"));
                camposEspecificosPanel.add(pratoField);
            }

            camposEspecificosPanel.revalidate();
            camposEspecificosPanel.repaint();
        }

        private JTextField campo(int width) {
            JTextField f = new JTextField();
            f.setPreferredSize(new Dimension(width, 30));
            f.setMaximumSize(new Dimension(width, 30));
            return f;
        }

        public JPanel getPanel() {
            return panel;
        }
    }

    /* =====================  ENTRADA DE DESLOCAMENTO  ==================== */
    private class DeslocamentoEntry {
        private final JPanel panel;
        private final JButton btnRemove;

        private JTextField meioTransporteField;
        private JTextField custoField;

        public DeslocamentoEntry() {

            panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            panel.setBackground(Color.WHITE);

            panel.add(new JLabel("Meio Transporte:"));
            meioTransporteField = campo(180);
            panel.add(meioTransporteField);

            panel.add(new JLabel("Custo:"));
            custoField = campo(80);
            panel.add(custoField);

            btnRemove = new JButton("Remover");
            styleRemoveButton(btnRemove);  // vermelho
            panel.add(btnRemove);

            btnRemove.addActionListener(e -> {
                deslocamentoEntriesPanel.remove(panel);
                deslocamentoEntries.remove(this);
                deslocamentoEntriesPanel.revalidate();
                deslocamentoEntriesPanel.repaint();
            });
        }

        private JTextField campo(int width) {
            JTextField f = new JTextField();
            f.setPreferredSize(new Dimension(width, 30));
            return f;
        }

        public JPanel getPanel() { return panel; }
    }

    /* ---------------------  UTILIDADES DE UI --------------------- */
    private void styleLabelSemibold(JLabel l) {
        l.setFont(new Font("SansSerif", Font.BOLD, 14));
        l.setForeground(new Color(60, 60, 60));
    }

    private JTextField addField(JPanel p, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x; gbc.gridy = y;
        JTextField f = new JTextField();
        styleField(f);
        p.add(f, gbc);
        return f;
    }
    private void addLabel(JPanel p, GridBagConstraints gbc, int x, int y, String text) {
        gbc.gridx = x; gbc.gridy = y;
        JLabel l = new JLabel(text);
        styleLabelSemibold(l);
        p.add(l, gbc);
    }
    private void styleField(JTextField f) {
        f.setPreferredSize(new Dimension(250, 40));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8,10,8,10)));
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setForeground(new Color(60,60,60));
    }

    // -------- Estilo botões (baseado no HomeFrame) --------
    private void stylePrimaryButton(JButton button) {
        Color normalBG = new Color(0, 123, 255);
        Color hoverBG = new Color(0, 105, 217);

        button.setBackground(normalBG);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setPreferredSize(new Dimension(180, 48));

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

    // Estilo botão Adicionar verde com letras brancas
    private void styleAddButton(JButton button) {
        Color normalBG = new Color(40, 167, 69);
        Color hoverBG = new Color(30, 140, 50);

        button.setBackground(normalBG);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        button.setPreferredSize(new Dimension(110, 35));

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

    // Estilo botão Remover vermelho com letras brancas
    private void styleRemoveButton(JButton button) {
        Color normalBG = new Color(220, 53, 69);
        Color hoverBG = new Color(180, 40, 55);

        button.setBackground(normalBG);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        button.setPreferredSize(new Dimension(110, 35));

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

    /* ----------------- GET PANEL (para exibir) ----------------- */
    public JPanel getPanel() { return mainPanel; }

    /* ===============  MÉTODOS addEntry  =============== */
    private void addHospedagemEntry() {
        HospedagemEntry e = new HospedagemEntry();
        hospedagemEntries.add(e);
        hospedagemEntriesPanel.add(e.getPanel());
        hospedagemEntriesPanel.revalidate(); hospedagemEntriesPanel.repaint();
    }
    private void addAtividadeEntry() {
        AtividadeEntry e = new AtividadeEntry();
        atividadeEntries.add(e);
        atividadesEntriesPanel.add(e.getPanel());
        atividadesEntriesPanel.revalidate(); atividadesEntriesPanel.repaint();
    }
    private void addDeslocamentoEntry() {
        DeslocamentoEntry e = new DeslocamentoEntry();
        deslocamentoEntries.add(e);
        deslocamentoEntriesPanel.add(e.getPanel());
        deslocamentoEntriesPanel.revalidate(); deslocamentoEntriesPanel.repaint();
    }
}
