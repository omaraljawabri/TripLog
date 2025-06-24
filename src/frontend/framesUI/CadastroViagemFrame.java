package frontend.framesUI;

import backend.main.entities.*;
import backend.main.repositories.ViagemRepository;
import backend.main.services.ViagemService;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CadastroViagemFrame {
    /* ----------------- Campos da janela ----------------- */

    private final JPanel mainPanel;

    private JTextField inputLugarPartida;
    private JTextField inputLugarChegada;
    private JTextField inputSaldo;
    private JTextField inputCompanhia;
    private JFormattedTextField inputDataSaida;
    private JFormattedTextField inputDataChegada;

    private JPanel hospedagemEntriesPanel;
    private List<HospedagemEntry> hospedagemEntries = new ArrayList<>();

    private JPanel atividadesEntriesPanel;
    private List<AtividadeEntry> atividadeEntries = new ArrayList<>();

    private JPanel deslocamentoEntriesPanel;
    private List<DeslocamentoEntry> deslocamentoEntries = new ArrayList<>();

    // Botões Salvar e Cancelar do rodapé
    private JButton btnSalvar;
    private JButton btnCancelar;

    private ActionListener salvarComSucessoListener;

    public void addCancelarListener(ActionListener listener) {
        btnCancelar.addActionListener(listener);
    }

    public void addSalvarComSucessoListener(ActionListener listener) {
        this.salvarComSucessoListener = listener;
    }

    /* ==================================================== */
    public CadastroViagemFrame(Viajante viajante) {

        /* ---------- Estrutura base ---------- */
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 30, 20, 30));

        montarTopo();
        montarCentro();
        montarRodape();

        // Tornar a janela rolável e responsiva:
        JScrollPane scrollPane = new JScrollPane(mainPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(Color.WHITE);
        wrapper.add(scrollPane);

        this.mainPanelWrapper = wrapper;

        btnSalvar.addActionListener(e -> {
            boolean dataValida = true;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (AtividadeEntry entry : atividadeEntries) {
                try {
                    String textoData = entry.dataField.getText();
                    String textoHora = entry.horarioField.getText();
                    LocalDateTime.parse(textoData+" "+textoHora, formatter);
                } catch (Exception ex) {
                    dataValida = false;
                    JOptionPane.showMessageDialog(mainPanel,
                            "Data inválida em uma atividade! Use o formato: dd/MM/yyyy HH:mm",
                            "Erro de validação", JOptionPane.ERROR_MESSAGE);
                    break;
                }
            }

            if (dataValida) {
                try {
                    String lugarDePartida = inputLugarPartida.getText();
                    String lugarDeChegada = inputLugarChegada.getText();
                    String dataSaida = inputDataSaida.getText();
                    String dataChegada = inputDataChegada.getText();
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate dataSaidaDate = LocalDate.parse(dataSaida, dateTimeFormatter);
                    LocalDate dataChegadaDate = LocalDate.parse(dataChegada, dateTimeFormatter);
                    double saldo = Double.parseDouble(inputSaldo.getText());
                    String companhia = inputCompanhia.getText();
                    List<Deslocamento> deslocamentos = new ArrayList<>();
                    for (DeslocamentoEntry deslocamentoEntry : deslocamentoEntries){
                        double custo = Double.parseDouble(deslocamentoEntry.custoField.getText());
                        String meioTransporte = deslocamentoEntry.meioTransporteField.getText();
                        Deslocamento deslocamento = new Deslocamento(meioTransporte, custo);
                        deslocamentos.add(deslocamento);
                    }
                    List<Hospedagem> hospedagens = new ArrayList<>();
                    for (HospedagemEntry hospedagemEntry : hospedagemEntries){
                        String nomeLocalHospedagem = hospedagemEntry.nomeLocalField.getText();
                        int numeroDeNoites = Integer.parseInt(hospedagemEntry.noitesField.getText());
                        double valorDiaria = Double.parseDouble(hospedagemEntry.valorDiariaField.getText());
                        Hospedagem hospedagem = new Hospedagem(nomeLocalHospedagem, numeroDeNoites, valorDiaria);
                        hospedagens.add(hospedagem);
                    }
                    List<Atividade> atividades = new ArrayList<>();
                    for (AtividadeEntry atividadeEntry : atividadeEntries){
                        String textoData = atividadeEntry.dataField.getText();
                        String textoHora = atividadeEntry.horarioField.getText();
                        LocalDateTime dataAtividade = LocalDateTime.parse(textoData + " " + textoHora, formatter);
                        double gastoAtividade = Double.parseDouble(atividadeEntry.custoField.getText());
                        String nomeAtividade = atividadeEntry.nomeField.getText();
                        String tipoAtividade = String.valueOf(atividadeEntry.tipoCombo.getSelectedItem());

                        if (tipoAtividade.equals("Evento")){
                            String temaEvento = atividadeEntry.temaField.getText();
                            Evento evento = new Evento(nomeAtividade, gastoAtividade, dataAtividade, temaEvento);
                            atividades.add(evento);
                        } else if (tipoAtividade.equals("Passeio")){
                            String nomeLocalPasseio = atividadeEntry.nomeLocalField.getText();
                            Passeio passeio = new Passeio(nomeAtividade, gastoAtividade, dataAtividade, nomeLocalPasseio);
                            atividades.add(passeio);
                        } else if (tipoAtividade.equals("Restaurante")){
                            String nomeRestaurante = atividadeEntry.nomeRestauranteField.getText();
                            String culinariaRestaurante = atividadeEntry.culinariaField.getText();
                            String pratoRestaurante = atividadeEntry.pratoField.getText();
                            Restaurante restaurante = new Restaurante(nomeAtividade, gastoAtividade, dataAtividade, nomeRestaurante, culinariaRestaurante, pratoRestaurante);
                            atividades.add(restaurante);
                        }
                    }
                    Viagem viagem = new Viagem(lugarDePartida, lugarDeChegada, deslocamentos, hospedagens, saldo, companhia, atividades, viajante.getEmail(), dataChegadaDate, dataSaidaDate);
                    ViagemRepository viagemRepository = new ViagemRepository("viagem.ser");
                    ViagemService viagemService = new ViagemService(viagemRepository);
                    viagemService.adicionarViagem(viagem, viajante);
                    JOptionPane.showMessageDialog(mainPanel, "Viagem cadastrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    if (salvarComSucessoListener != null) {
                        salvarComSucessoListener.actionPerformed(
                                new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "salvar_sucesso")
                        );
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainPanel, "Erro ao fazer cadastro de viagem! Credenciais inválidas, tente novamente!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private JPanel mainPanelWrapper;

    /* ----------------------------- TOPO ----------------------------- */
    private void montarTopo() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);

        try {
            ImageIcon img = new ImageIcon(getClass().getResource("../resources/images/beachIcon.png"));
            Image scaled = img.getImage().getScaledInstance(120, -1, Image.SCALE_SMOOTH);
            JLabel imagemLabel = new JLabel(new ImageIcon(scaled));
            imagemLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            topPanel.add(imagemLabel);
        } catch (Exception e) {
            // Ignora se a imagem não for encontrada
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
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JPanel inputsPanel = montarInputsPrincipais();
        centerPanel.add(inputsPanel);
        centerPanel.add(Box.createVerticalStrut(15));

        centerPanel.add(montarHospedagem());
        centerPanel.add(Box.createVerticalStrut(15));

        centerPanel.add(montarAtividades());
        centerPanel.add(Box.createVerticalStrut(15));

        centerPanel.add(montarDeslocamentos());

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(Color.WHITE);
        centerWrapper.add(centerPanel, BorderLayout.NORTH);

        mainPanel.add(centerWrapper, BorderLayout.CENTER);
    }

    private JPanel montarInputsPrincipais() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 15, 4, 15);
        gbc.weightx = 1;

        addLabel(panel, gbc, 0, 0, "Local de Partida");
        addLabel(panel, gbc, 1, 0, "Local de Chegada");
        addLabel(panel, gbc, 2, 0, "Saldo");

        inputLugarPartida = addField(panel, gbc, 0, 1);
        inputLugarChegada = addField(panel, gbc, 1, 1);
        inputSaldo        = addField(panel, gbc, 2, 1);

        addLabel(panel, gbc, 0, 2, "Data de Saída");
        addLabel(panel, gbc, 1, 2, "Data de Chegada");
        addLabel(panel, gbc, 2, 2, "Companhia");

        inputDataChegada    = addMaskedDateField(panel, gbc, 0, 3);
        inputDataSaida  = addMaskedDateField(panel, gbc, 1, 3);
        inputCompanhia    = addField(panel, gbc, 2, 3);

        return panel;
    }

    private JFormattedTextField addMaskedDateField(JPanel p, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x; gbc.gridy = y;
        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            JFormattedTextField dateField = new JFormattedTextField(dateMask);
            styleField(dateField);
            p.add(dateField, gbc);
            return dateField;
        } catch (ParseException e) {
            e.printStackTrace();
            return new JFormattedTextField();
        }
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
        styleAddButton(btnAddHosp);
        btnAddHosp.addActionListener(e -> addHospedagemEntry());
        JPanel btnP = new JPanel(new FlowLayout(FlowLayout.LEFT, -5, 5));
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
        styleAddButton(btnAddAtv);
        btnAddAtv.addActionListener(e -> addAtividadeEntry());
        JPanel btnP = new JPanel(new FlowLayout(FlowLayout.LEFT, -5, 5));
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
        styleAddButton(btnAddDesl);
        btnAddDesl.addActionListener(e -> addDeslocamentoEntry());
        JPanel btnP = new JPanel(new FlowLayout(FlowLayout.LEFT, -5, 5));
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

        private JTextField nomeLocalField;
        private JTextField noitesField;
        private JTextField valorDiariaField;

        public HospedagemEntry() {
            panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            panel.setBackground(Color.WHITE);

            panel.add(new JLabel("Nome Local:"));
            nomeLocalField = campo(200);
            panel.add(nomeLocalField);

            panel.add(new JLabel("Noites:"));
            noitesField = campo(60);
            panel.add(noitesField);

            panel.add(new JLabel("Valor Diária:"));
            valorDiariaField = campo(80);
            panel.add(valorDiariaField);

            btnRemove = new JButton("Remover");
            styleRemoveButton(btnRemove);
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

        private JFormattedTextField dataField;
        private JTextField nomeField;
        private JFormattedTextField horarioField;
        private JTextField temaField;
        private JTextField nomeLocalField;
        private JTextField nomeRestauranteField;
        private JTextField culinariaField;
        private JTextField pratoField;
        private JTextField custoField;

        private JPanel camposEspecificosPanel;

        public AtividadeEntry() {
            panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.setBackground(Color.WHITE);
            panel.setAlignmentX(Component.LEFT_ALIGNMENT);

            camposEspecificosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
            camposEspecificosPanel.setBackground(Color.WHITE);
            panel.add(camposEspecificosPanel);

            nomeField            = campo(150);
            try {
                MaskFormatter timeMask = new MaskFormatter("##:##");
                timeMask.setPlaceholderCharacter('_');
                horarioField = new JFormattedTextField(timeMask);
                horarioField.setPreferredSize(new Dimension(150, 30));
                horarioField.setMaximumSize(new Dimension(150, 30));

                MaskFormatter dateMask = new MaskFormatter("##/##/####");
                dateMask.setPlaceholderCharacter('_');
                dataField = new JFormattedTextField(dateMask);
                dataField.setPreferredSize(new Dimension(150, 30));
                dataField.setMaximumSize(new Dimension(150, 30));

            } catch (ParseException e) {
                e.printStackTrace();
                horarioField = new JFormattedTextField();
                dataField = new JFormattedTextField();
            }
            temaField            = campo(150);
            nomeLocalField       = campo(150);
            nomeRestauranteField = campo(150);
            culinariaField       = campo(100);
            pratoField           = campo(150);
            custoField           = campo(80);

            tipoCombo = new JComboBox<>(new String[]{"Evento", "Passeio", "Restaurante"});
            tipoCombo.setPreferredSize(new Dimension(120, 30));
            tipoCombo.addActionListener(e -> rebuildCampos());

            btnRemove = new JButton("Remover");
            styleRemoveButton(btnRemove);
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

            camposEspecificosPanel.add(new JLabel("Tipo:"));
            camposEspecificosPanel.add(tipoCombo);
            camposEspecificosPanel.add(btnRemove);

            camposEspecificosPanel.add(new JLabel("Nome:"));
            camposEspecificosPanel.add(nomeField);
            camposEspecificosPanel.add(new JLabel("Data:"));
            camposEspecificosPanel.add(dataField);
            camposEspecificosPanel.add(new JLabel("Horário:"));
            camposEspecificosPanel.add(horarioField);
            camposEspecificosPanel.add(new JLabel("Custo:"));
            camposEspecificosPanel.add(custoField);

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
            styleRemoveButton(btnRemove);
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
    public JPanel getPanel() { return mainPanelWrapper; }

    /* ===============  MÉTODOS addEntry  =============== */
    private void addHospedagemEntry() {
        HospedagemEntry e = new HospedagemEntry();
        hospedagemEntries.add(e);
        hospedagemEntriesPanel.add(Box.createVerticalStrut(10)); // espaço entre entradas
        hospedagemEntriesPanel.add(e.getPanel());
        hospedagemEntriesPanel.revalidate();
        hospedagemEntriesPanel.repaint();
    }
    private void addAtividadeEntry() {
        AtividadeEntry e = new AtividadeEntry();
        atividadeEntries.add(e);
        atividadesEntriesPanel.add(Box.createVerticalStrut(20));
        atividadesEntriesPanel.add(e.getPanel());
        atividadesEntriesPanel.revalidate();
        atividadesEntriesPanel.repaint();
    }
    private void addDeslocamentoEntry() {
        DeslocamentoEntry e = new DeslocamentoEntry();
        deslocamentoEntries.add(e);
        deslocamentoEntriesPanel.add(Box.createVerticalStrut(10));
        deslocamentoEntriesPanel.add(e.getPanel());
        deslocamentoEntriesPanel.revalidate();
        deslocamentoEntriesPanel.repaint();
    }
}
