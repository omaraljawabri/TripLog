package backend.test.unit;

import backend.main.entities.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ViagemTest {

    @Test
    @DisplayName("calcularTotalGastos deve retornar os gastos totais da viagem quando houverem gastos na viagem")
    void calcularTotalGastos_RetornaGastoTotalDaViagem_QuandoHouveremGastos() {
        Viagem viagem = criarViagem1();

        assertEquals(1710, viagem.calcularTotalGastos());
    }

    @Test
    @DisplayName("calcularTotalGastos deve retornar o valor zero quando não houverem gastos registrados na viagem")
    void calcularTotalGastos_RetornaZero_QuandoNaoHouveremGastosRegistrados(){
        Viagem viagem = new Viagem();

        assertEquals(0, viagem.calcularTotalGastos());
    }

    @Test
    @DisplayName("calcularDiasDeViagem deve retornar um inteiro com os didas de viagem quando o dia de chegada e termino da viagem não são null")
    void calcularDiasDeViagem_RetornaDiasDeViagem_QuandoDiaDeChegadaETerminoDaViagemNaoSaoNull(){
        Viagem viagem = criarViagem1();
        int diasDeViagem = viagem.calcularDiasDeViagem();

        assertEquals(7, diasDeViagem);
    }

    @Test
    @DisplayName("calcularDiasDeViagem deve retornar o valor zero quando dia da chega e/ou termino da viagem for null")
    void calcularDiasDeViagem_RetornaZero_QuandoDiaDeChegadaEOuTerminoForNull(){
        Viagem viagem = new Viagem();

        int diasDeViagem = viagem.calcularDiasDeViagem();
        assertEquals(0, diasDeViagem);
    }

    private Viagem criarViagem1(){
        Viagem viagem1 = new Viagem();
        viagem1.setLugarDePartida("Rio de Janeiro");
        viagem1.setLugarDeChegada("Salvador");
        viagem1.setCompanhia("João");
        viagem1.setDataChegada(LocalDate.of(2025, 6, 10));
        viagem1.setDataTermino(LocalDate.of(2025, 6, 17));

        viagem1.setDeslocamentos(List.of(
                new Deslocamento("Avião", 800.0),
                new Deslocamento("Táxi", 50.0)
        ));

        viagem1.setHospedagens(List.of(
                new Hospedagem("Hotel Sol Bahia", 3, 200.0)
        ));

        List<Atividade> atividades1 = new ArrayList<>();
        atividades1.add(new Evento("Festival de Verão",
                150.0,
                LocalDateTime.of(2025, 6, 10, 18, 0),
                "Música"));
        atividades1.add(new Passeio("Pelourinho Tour",
                30.0,
                LocalDateTime.of(2025, 6, 11, 10, 0),
                "Centro Histórico"));
        atividades1.add(new Restaurante("Almoço na Yemanjá",
                80.0,
                LocalDateTime.of(2025, 6, 11, 13, 0),
                "Restaurante Yemanjá", "Baiana", "Moqueca"));

        viagem1.setAtividades(atividades1);
        viagem1.setSaldo(1000.0);
        viagem1.setEmailViajante("fulano@example.com");
        return viagem1;
    }
}