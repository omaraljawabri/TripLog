package frontend.framesUI;

import frontend.framesUI.DetalhesViagemFrame;
import frontend.framesUI.DetalhesViagemFrame.AtividadeDados;
import frontend.framesUI.DetalhesViagemFrame.DeslocamentoDados;
import frontend.framesUI.DetalhesViagemFrame.HospedagemDados;
import backend.main.entities.Viajante;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class TesteDetalhe {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Dados fictícios
            String partida = "Goiânia";
            String chegada = "Rio de Janeiro";
            String saldo = "3000.00";
            String gasto = "2100.00";
            String dataChegada = "12/07/2025";
            String dataSaida = "17/07/2025";
            String diasViagem = "5";
            String companhia = "Ninguem";

            List<HospedagemDados> hospedagens = Arrays.asList(
                    new HospedagemDados("Hotel Copacabana", "3", "250.00"),
                    new HospedagemDados("Pousada Mar Azul", "2", "180.00")
            );

            List<AtividadeDados> atividades = Arrays.asList(
                    new AtividadeDados("Evento", "Congresso de TI", "12/07/2025", "09:00", "150.00", "Inovação", "", "teste", "test", "21"),
                    new AtividadeDados("Passeio", "Cristo Redentor", "13/07/2025", "14:00", "120.00", "RJ", "Cristo", "test", "test", "120"),
                    new AtividadeDados("Restaurante", "Churrascaria Top", "14/07/2025", "20:00", "200.00", "", "", "Top Grill", "Brasileira", "Picanha")
            );

            List<DeslocamentoDados> deslocamentos = Arrays.asList(
                    new DeslocamentoDados("Táxi", "45.00"),
                    new DeslocamentoDados("Uber", "35.00")
            );

            // Criação do frame
            Viajante viajanteFicticio = new Viajante(); // se necessário, preencha campos do Viajante
            DetalhesViagemFrame frame = new DetalhesViagemFrame(viajanteFicticio);

            frame.carregarDadosViagem(
                    partida,
                    chegada,
                    saldo,
                    gasto,
                    dataChegada,
                    dataSaida,
                    diasViagem,
                    companhia,
                    hospedagens,
                    atividades,
                    deslocamentos
            );

            JFrame janela = new JFrame("Teste Detalhes da Viagem");
            janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            janela.setSize(1000, 700);
            janela.setLocationRelativeTo(null);
            janela.setContentPane(frame.getPanel());
            janela.setVisible(true);
        });
    }
}
