package frontend.framesUI;

import backend.main.entities.Viajante;
import backend.main.repositories.ViagemRepository;
import backend.main.services.ViagemService;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private LoginFrame login;
    private CadastroFrame cadastro;
    private CadastroViagemFrame cadastroViagem;
    private HomeFrame home;
    private ProfileUserFrame perfil;
    private ListaDeViagensFrame listaDeViagensFrame;

    private Viajante usuarioLogado;
    private final List<ListaDeViagensFrame.Viagem> viagens = new ArrayList<>();

    public MainFrame() {
        setTitle("Sistema de Acesso");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 500);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        login = new LoginFrame(this);
        cadastro = new CadastroFrame();
        home = null;
        perfil = null;
        cadastroViagem = null;

        cardPanel.add(login.getPanel(), "login");
        cardPanel.add(cadastro.getPanel(), "cadastro");

        setContentPane(cardPanel);
        cardLayout.show(cardPanel, "login");

        login.getTelaDeCadastroButton().addActionListener(e -> cardLayout.show(cardPanel, "cadastro"));
        cadastro.getTelaDeLoginButton().addActionListener(e -> cardLayout.show(cardPanel, "login"));
    }

    public void mostrarTela(String nomeTela) {
        cardLayout.show(cardPanel, nomeTela);
    }

    public void abrirHome(Viajante viajante) {
        this.usuarioLogado = viajante;

        // Remove o painel antigo se existir
        if (home != null) {
            cardPanel.remove(home.getPanel());
        }

        home = new HomeFrame(viajante);
        home.setCadastrarViagemButtonListener(e -> abrirCadastroViagem());
        home.setMinhasViagensButtonListener(e -> abrirListaDeViagens());
        home.setMeuPerfilButtonListener(e -> abrirPerfil(usuarioLogado));
        cardPanel.add(home.getPanel(), "home");

        cardLayout.show(cardPanel, "home");
        revalidate();
        repaint();
    }

    public void abrirCadastroViagem() {
        if (usuarioLogado != null) {
            if (cadastroViagem != null) {
                cardPanel.remove(cadastroViagem.getPanel());
            }

            cadastroViagem = new CadastroViagemFrame(usuarioLogado);
            cadastroViagem.addCancelarListener(e -> voltarParaHomeOuLogin());

            cardPanel.add(cadastroViagem.getPanel(), "cadastroViagem");
            revalidate();
            repaint();
            cardLayout.show(cardPanel, "cadastroViagem");
        } else {
            JOptionPane.showMessageDialog(this, "Usuário não está logado.");
        }
    }

    public void abrirPerfil(Viajante viajante) {
        this.usuarioLogado = viajante;

        if (perfil != null) {
            cardPanel.remove(perfil.getPanel());
        }

        perfil = new ProfileUserFrame(viajante);

        perfil.setBtnInicioListener(() -> abrirHome(usuarioLogado));
        perfil.getBtnLogout().addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logout efetuado!");
            usuarioLogado = null;
            cardLayout.show(cardPanel, "login");
        });

        ViagemService viagemService = new ViagemService(new ViagemRepository("viagem.ser"));
        List<backend.main.entities.Viagem> viagensBack = viagemService.buscarTodasViagensPorEmailViajante(this.usuarioLogado.getEmail());

        List<ListaDeViagensFrame.Viagem> viagensFront = viagensBack.stream()
                .map(MainFrame::converterParaFrontend)
                .toList();

        // ✅ Atualize a lista global para usar depois
        this.viagens.clear();
        this.viagens.addAll(viagensFront);

        perfil.setBtnListaViagensListener(
                this,
                viagensFront,
                () -> abrirPerfil(usuarioLogado),
                this::abrirCadastroViagem
        );

        cardPanel.add(perfil.getPanel(), "perfil");
        cardLayout.show(cardPanel, "perfil");
        revalidate();
        repaint();
    }

    public static ListaDeViagensFrame.Viagem converterParaFrontend(backend.main.entities.Viagem v) {
        return new ListaDeViagensFrame.Viagem(
                v.getLugarDeChegada(),
                v.calcularDiasDeViagem(),
                v.calcularTotalGastos()
        );
    }

    public void abrirListaDeViagens() {
        if (usuarioLogado == null) {
            JOptionPane.showMessageDialog(this, "Usuário não está logado.");
            return;
        }

        ViagemService viagemService = new ViagemService(new ViagemRepository("viagem.ser"));
        List<backend.main.entities.Viagem> viagensBack = viagemService.buscarTodasViagensPorEmailViajante(usuarioLogado.getEmail());

        List<ListaDeViagensFrame.Viagem> viagensFront = viagensBack.stream()
                .map(MainFrame::converterParaFrontend)
                .toList();

        // Atualize a lista global para usar depois
        this.viagens.clear();
        this.viagens.addAll(viagensFront);

        listaDeViagensFrame = new ListaDeViagensFrame(
                viagensFront,
                viagem -> JOptionPane.showMessageDialog(this, "Abrindo detalhes de: " + viagem.getLugarChegada()),
                viagem -> {
                    JOptionPane.showMessageDialog(this, "Viagem para " + viagem.getLugarChegada() + " excluída!");
                    viagens.remove(viagem);
                    // Você pode adicionar aqui lógica para excluir da base, se quiser
                    listaDeViagensFrame.atualizarListaPublic(); // se esse método existir para atualizar a lista visual
                },
                e -> abrirPerfil(usuarioLogado),
                e -> abrirCadastroViagem()
        );

        cardPanel.add(listaDeViagensFrame.getPanel(), "listaViagens");
        cardLayout.show(cardPanel, "listaViagens");
        revalidate();
        repaint();
    }


    private void voltarParaHomeOuLogin() {
        if (usuarioLogado != null) {
            abrirHome(usuarioLogado);
        } else {
            cardLayout.show(cardPanel, "login");
        }
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        FlatLightLaf.setup();

        UIManager.put("Button.arc", 20);
        UIManager.put("TextComponent.arc", 10);
        UIManager.put("Component.arc", 10);

        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
