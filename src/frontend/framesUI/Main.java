package frontend.framesUI;

import backend.main.entities.Viagem;
import backend.main.entities.Viajante;
import backend.main.entities.Deslocamento;
import backend.main.entities.Hospedagem;
import backend.main.entities.Atividade;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Criando listas vazias com tipo explícito
            List<Deslocamento> deslocamentosVazios = new ArrayList<>();
            List<Hospedagem> hospedagensVazias = new ArrayList<>();
            List<Atividade> atividadesVazias = new ArrayList<>();

            // Criação de dados de exemplo da classe real Viagem com listas não nulas
            List<Viagem> viagens = new ArrayList<>();

            viagens.add(new Viagem(
                    "Brasília",
                    "Rio de Janeiro",
                    deslocamentosVazios,  // CORRETO: deslocamentos primeiro
                    hospedagensVazias,    // CORRETO: hospedagens depois
                    1500.50,
                    "Gol Linhas Aéreas",
                    atividadesVazias,
                    "exemplo@dominio.com",
                    LocalDate.of(2023, 6, 10),
                    LocalDate.of(2023, 6, 15)
            ));
            viagens.add(new Viagem(
                    "Brasília",
                    "São Paulo",
                    deslocamentosVazios,
                    hospedagensVazias,
                    800.00,
                    "Latam Airlines",
                    atividadesVazias,
                    "exemplo@dominio.com",
                    LocalDate.of(2023, 7, 5),
                    LocalDate.of(2023, 7, 8)
            ));
            viagens.add(new Viagem(
                    "Brasília",
                    "Salvador",
                    deslocamentosVazios,
                    hospedagensVazias,
                    2300.75,
                    "Azul Linhas Aéreas",
                    atividadesVazias,
                    "exemplo@dominio.com",
                    LocalDate.of(2023, 8, 1),
                    LocalDate.of(2023, 8, 8)
            ));
            viagens.add(new Viagem(
                    "Brasília",
                    "Fortaleza",
                    deslocamentosVazios,
                    hospedagensVazias,
                    1200.20,
                    "Gol Linhas Aéreas",
                    atividadesVazias,
                    "exemplo@dominio.com",
                    LocalDate.of(2023, 9, 10),
                    LocalDate.of(2023, 9, 14)
            ));

            // Cria um viajante fake para testes
            Viajante viajanteTeste = new Viajante();
            viajanteTeste.setEmail("exemplo@dominio.com");
            viajanteTeste.setNome("Usuário de Teste");
            viajanteTeste.setViagens(viagens);  // Assumindo que exista esse setter

            // Consumidor para ver detalhes
            Consumer<Viagem> verDetalhes = v -> JOptionPane.showMessageDialog(null,
                    "Detalhes da viagem:\nDestino: " + v.getLugarDeChegada() +
                            "\nCompanhia: " + v.getCompanhia() +
                            "\nValor gasto: R$ " + String.format("%.2f", v.getSaldo()) +
                            "\nData chegada: " + v.getDataChegada() +
                            "\nData término: " + v.getDataTermino(),
                    "Detalhes", JOptionPane.INFORMATION_MESSAGE);

            // Consumidor para excluir (a remoção já ocorre dentro da tela, aqui só avisamos)
            Consumer<Viagem> excluir = v -> JOptionPane.showMessageDialog(null,
                    "Viagem para " + v.getLugarDeChegada() + " excluída.",
                    "Excluir", JOptionPane.WARNING_MESSAGE);

            // Listener para voltar (fecha a janela)
            ActionListener voltar = e -> {
                JOptionPane.showMessageDialog(null, "Voltando ao perfil...");
                // Aqui poderia fechar janela ou trocar de tela
            };

            // Listener para adicionar viagem (exemplo simples)
            ActionListener adicionar = e -> JOptionPane.showMessageDialog(null, "Botão Adicionar Viagem clicado!");

            // Cria o frame da lista usando o viajanteTeste
            ListaDeViagensFrame listaDeViagensFrame = new ListaDeViagensFrame(
                    viajanteTeste,
                    viagens,
                    verDetalhes,
                    excluir,
                    voltar,
                    adicionar
            );

            // Criar a janela principal
            JFrame frame = new JFrame("Lista de Viagens");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(listaDeViagensFrame.getPanel());
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
