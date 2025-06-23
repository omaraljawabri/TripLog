package frontend.framesUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Consumer;

public class ListaDeViagensFrame {

    private final JTextField txtFiltroDestino = new JTextField(15);
    private final JTextField txtFiltroSaldoMin = new JTextField(6);
    private final JTextField txtFiltroSaldoMax = new JTextField(6);
    private final JButton btnFiltrar = new JButton("Filtrar");

    private final JPanel mainPanel = new JPanel(new BorderLayout());
    private final JPanel listaPanel = new JPanel();
    private final JScrollPane scroll;

    private final List<Viagem> viagens;
    private final Consumer<Viagem> onVerDetalhes;
    private final Consumer<Viagem> onExcluir;
    private final ActionListener btnVoltarListener;

    // Novos botões
    private final JButton btnAdicionarViagem = new JButton("Adicionar Viagem");
    private final JButton btnAtualizarLista = new JButton("Atualizar Lista");

    public ListaDeViagensFrame(List<Viagem> viagens,
                               Consumer<Viagem> verDetalhesListener,
                               Consumer<Viagem> excluirListener,
                               ActionListener voltarListener,
                               ActionListener adicionarListener) {  // listener para botão adicionar

        this.viagens = viagens;
        this.onVerDetalhes = verDetalhesListener;
        this.onExcluir = excluirListener;
        this.btnVoltarListener = voltarListener;

        mainPanel.setBackground(Color.WHITE);

        JPanel topWrapper = new JPanel();
        topWrapper.setLayout(new BoxLayout(topWrapper, BoxLayout.Y_AXIS));
        topWrapper.setBackground(Color.WHITE);
        montarTopo(topWrapper);
        topWrapper.add(criarPainelFiltro());
        mainPanel.add(topWrapper, BorderLayout.NORTH);

        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
        listaPanel.setBackground(Color.WHITE);
        listaPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        scroll = new JScrollPane(null,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        scroll.getViewport().setLayout(new BorderLayout());
        scroll.getViewport().add(listaPanel, BorderLayout.NORTH);

        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
        wrapper.add(Box.createHorizontalGlue());

        JPanel widthLimiter = new JPanel(new BorderLayout());
        widthLimiter.setOpaque(false);
        widthLimiter.setMaximumSize(new Dimension(1200, Integer.MAX_VALUE));
        widthLimiter.add(scroll, BorderLayout.CENTER);

        wrapper.add(widthLimiter);
        wrapper.add(Box.createHorizontalGlue());

        mainPanel.add(wrapper, BorderLayout.CENTER);

        // Painel inferior com botões Adicionar, Atualizar e Voltar
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        footerPanel.setBackground(Color.WHITE);

        // Botão Adicionar Viagem
        stylePrimaryButton(btnAdicionarViagem);
        btnAdicionarViagem.setPreferredSize(new Dimension(150, 36));
        btnAdicionarViagem.addActionListener(adicionarListener);
        footerPanel.add(btnAdicionarViagem);

        // Botão Atualizar Lista
        styleSecondaryButton(btnAtualizarLista);
        btnAtualizarLista.setPreferredSize(new Dimension(140, 36));
        btnAtualizarLista.addActionListener(e -> atualizarLista());
        footerPanel.add(btnAtualizarLista);

        // Botão Voltar para o perfil
        JButton btnVoltar = new JButton("Voltar para o perfil");
        styleSecondaryButton(btnVoltar);
        btnVoltar.setPreferredSize(new Dimension(180, 36));
        btnVoltar.addActionListener(btnVoltarListener);
        footerPanel.add(btnVoltar);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        atualizarLista();
    }

    private void montarTopo(JPanel parent) {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("../resources/images/beachIcon.png"));
            Image scaled = icon.getImage().getScaledInstance(120, -1, Image.SCALE_SMOOTH);
            JLabel imgLbl = new JLabel(new ImageIcon(scaled));
            imgLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            topPanel.add(imgLbl);
        } catch (Exception ignored) {}

        JLabel titulo = new JLabel("Viagens Cadastradas");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Arial", Font.BOLD, 38));
        titulo.setForeground(new Color(33, 70, 120));
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(titulo);
        topPanel.add(Box.createVerticalStrut(18));

        parent.add(topPanel);
    }

    private JPanel criarPainelFiltro() {
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        filtros.setBackground(Color.WHITE);
        filtros.setBorder(BorderFactory.createEmptyBorder(0, 40, 10, 40));

        filtros.add(new JLabel("Destino:"));
        styleField(txtFiltroDestino); filtros.add(txtFiltroDestino);

        filtros.add(new JLabel("Saldo mín.:"));
        styleField(txtFiltroSaldoMin); filtros.add(txtFiltroSaldoMin);

        filtros.add(new JLabel("Saldo máx.:"));
        styleField(txtFiltroSaldoMax); filtros.add(txtFiltroSaldoMax);

        stylePrimaryButton(btnFiltrar);
        btnFiltrar.setPreferredSize(new Dimension(130, 38));
        btnFiltrar.addActionListener(e -> atualizarLista());
        filtros.add(btnFiltrar);

        return filtros;
    }

    private JPanel criarCabecalho() {
        JPanel cab = new JPanel(new GridBagLayout());
        cab.setBackground(new Color(245, 247, 250));
        cab.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(220, 220, 220)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 15, 6, 15);

        gbc.gridx = 0; gbc.weightx = 0;
        JLabel destinoHeader = colunaCabecalho("Destino");
        destinoHeader.setPreferredSize(new Dimension(350, 24));
        cab.add(destinoHeader, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(6, -30, 6, 15);
        JLabel diasHeader = colunaCabecalho("Dias");
        diasHeader.setPreferredSize(new Dimension(100, 24));
        cab.add(diasHeader, gbc);

        gbc.gridx = 2;
        gbc.insets = new Insets(6, 100, 6, 15);
        JLabel valorHeader = colunaCabecalho("Valor gasto");
        valorHeader.setPreferredSize(new Dimension(120, 24));
        cab.add(valorHeader, gbc);

        gbc.gridx = 3;
        gbc.insets = new Insets(6, 15, 6, 15);
        cab.add(Box.createHorizontalStrut(160), gbc);

        return cab;
    }

    private JPanel criarLinhaViagem(Viagem v) {
        JPanel linha = new JPanel(new GridBagLayout());
        linha.setBackground(Color.WHITE);
        linha.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.insets = new Insets(6, 15, 6, 15);
        gbc.gridx = 0;
        JLabel destinoValor = colunaTexto(v.getLugarChegada());
        destinoValor.setPreferredSize(new Dimension(350, 24));
        linha.add(destinoValor, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(6, 60, 6, 15);
        JLabel diasValor = colunaTexto(String.valueOf(v.getDiasPercorridos()));
        diasValor.setPreferredSize(new Dimension(100, 24));
        linha.add(diasValor, gbc);

        gbc.gridx = 2;
        gbc.insets = new Insets(6, 100, 6, 15);
        JLabel valorValor = colunaTexto(String.format("R$ %.2f", v.getSaldo()));
        valorValor.setPreferredSize(new Dimension(120, 24));
        linha.add(valorValor, gbc);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botoes.setOpaque(false);

        JButton btnDetalhes = new JButton("Ver detalhes");
        styleSecondaryButton(btnDetalhes);
        btnDetalhes.setPreferredSize(new Dimension(130, 32));
        btnDetalhes.addActionListener(e -> onVerDetalhes.accept(v));

        JButton btnExcluir = new JButton("Excluir");
        styleRemoveButton(btnExcluir);
        btnExcluir.setPreferredSize(new Dimension(100, 32));
        btnExcluir.addActionListener(e -> {
            onExcluir.accept(v);
            viagens.remove(v);
            atualizarLista();
        });

        botoes.add(btnDetalhes);
        botoes.add(btnExcluir);

        gbc.gridx = 3;
        gbc.insets = new Insets(6, 15, 6, 15);
        linha.add(botoes, gbc);

        return linha;
    }

    private void atualizarLista() {
        listaPanel.removeAll();
        listaPanel.add(criarCabecalho());

        String destinoFiltro = txtFiltroDestino.getText().trim().toLowerCase();
        double min = parseDouble(txtFiltroSaldoMin.getText(), Double.NEGATIVE_INFINITY);
        double max = parseDouble(txtFiltroSaldoMax.getText(), Double.POSITIVE_INFINITY);

        viagens.stream()
                .filter(v -> v.getLugarChegada().toLowerCase().contains(destinoFiltro))
                .filter(v -> v.getSaldo() >= min && v.getSaldo() <= max)
                .forEach(v -> listaPanel.add(criarLinhaViagem(v)));

        if (listaPanel.getComponentCount() == 1) {
            JLabel vazio = new JLabel("Nenhuma viagem encontrada.");
            vazio.setFont(new Font("SansSerif", Font.ITALIC, 14));
            vazio.setForeground(new Color(120, 120, 120));
            vazio.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
            vazio.setAlignmentX(Component.CENTER_ALIGNMENT);
            listaPanel.add(vazio);
        }

        listaPanel.revalidate();
        listaPanel.repaint();
    }

    public void atualizarListaPublic() {
        atualizarLista();
    }

    private double parseDouble(String txt, double fallback) {
        try { return Double.parseDouble(txt.replace(',', '.')); }
        catch (Exception e) { return fallback; }
    }

    private JLabel colunaTexto(String s) {
        JLabel l = new JLabel(s);
        l.setFont(new Font("SansSerif", Font.PLAIN, 14));
        l.setForeground(new Color(50, 50, 50));
        return l;
    }

    private JLabel colunaCabecalho(String s) {
        JLabel l = new JLabel(s);
        l.setFont(new Font("SansSerif", Font.BOLD, 14));
        l.setForeground(new Color(40, 40, 40));
        return l;
    }

    private void styleField(JTextField f) {
        f.setPreferredSize(new Dimension(140, 36));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        f.setFont(new Font("SansSerif", Font.PLAIN, 14));
    }

    private void stylePrimaryButton(JButton b) {
        Color bg = new Color(0, 123, 255), hover = new Color(0, 105, 217);
        b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(hover); }
            public void mouseExited (java.awt.event.MouseEvent e) { b.setBackground(bg); }
        });
    }

    private void styleSecondaryButton(JButton b) {
        Color border = new Color(0, 123, 255), hover = new Color(230, 240, 255);
        b.setBackground(Color.WHITE); b.setForeground(border);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBorder(BorderFactory.createLineBorder(border, 2));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(hover); }
            public void mouseExited (java.awt.event.MouseEvent e) { b.setBackground(Color.WHITE); }
        });
    }

    private void styleRemoveButton(JButton b) {
        Color bg = new Color(220, 53, 69), hover = new Color(180, 40, 55);
        b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(hover); }
            public void mouseExited (java.awt.event.MouseEvent e) { b.setBackground(bg); }
        });
    }

    public JPanel getPanel() { return mainPanel; }

    // Getters para botões externos (opcional)
    public JButton getBtnAdicionarViagem() {
        return btnAdicionarViagem;
    }

    public static class Viagem {
        private final String destino;
        private final int dias;
        private final double saldo;

        public Viagem(String destino, int dias, double saldo) {
            this.destino = destino;
            this.dias = dias;
            this.saldo = saldo;
        }

        public String getLugarChegada() { return destino; }
        public int getDiasPercorridos() { return dias; }
        public double getSaldo() { return saldo; }
    }
}
