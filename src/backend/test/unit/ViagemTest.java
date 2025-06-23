package backend.test.unit;

import backend.main.entities.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ViagemTest {

    @Test
    void calcularTotalGastos_RetornaGastoTotalDaViagem_QuandoHouveremGastos() {
        Viagem viagem = criarViagem1();

        assertEquals(1710, viagem.calcularTotalGastos());
    }

    @Test
    void calcularTotalGastos_RetornaZero_QuandoNaoHouveremGastosRegistrados(){
        Viagem viagem = new Viagem();

        assertEquals(0, viagem.calcularTotalGastos());
    }

    private Viagem criarViagem1(){
        Viagem viagem1 = new Viagem();
        viagem1.setLugarDePartida("Rio de Janeiro");
        viagem1.setLugarDeChegada("Salvador");
        viagem1.setCompanhia("João");

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
        viagem1.setDiasPercorridos(4);
        viagem1.setEmailViajante("fulano@example.com");
        return viagem1;
    }
}