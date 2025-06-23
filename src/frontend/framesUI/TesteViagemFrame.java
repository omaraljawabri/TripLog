package frontend.framesUI;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class TesteViagemFrame {
    public static void main(String[] args) {
        // Aplica o tema FlatLaf
        FlatLightLaf.setup();

        SwingUtilities.invokeLater(() -> {
            // Cria a instância da tela de cadastro de viagem
            CadastroViagemFrame cadastroViagemFrame = new CadastroViagemFrame();

            // Cria uma janela para mostrar o painel da tela
            JFrame frame = new JFrame("Cadastro de Viagem – TravelPOO");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Adiciona scroll para responsividade e overflow
            JScrollPane scrollPane = new JScrollPane(
                    cadastroViagemFrame.getPanel(),
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
            );
            scrollPane.getVerticalScrollBar().setUnitIncrement(16); // rolagem suave

            frame.setContentPane(scrollPane);
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null); // centraliza na tela
            frame.setVisible(true);
        });
    }
}