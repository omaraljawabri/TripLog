package frontend.framesUI;

import backend.main.entities.Viajante;
import frontend.framesUI.ProfileUserFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Simulando um usuário logado (você pode puxar do AuthService ou algo real no futuro)
        Viajante viajanteSimulado = new Viajante("João da Silva", "senha123", "joao@email.com");

        // Criando a janela principal
        JFrame frame = new JFrame("Perfil do Usuário");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null); // centraliza a janela

        // Criando a tela de perfil
        ProfileUserFrame telaPerfil = new ProfileUserFrame(viajanteSimulado);

        // Configurando o listener do botão "Voltar para Início" para trocar para HomeFrame
        telaPerfil.setBtnInicioListener(frame, viajanteSimulado);

        // Configurar listener do botão Logout (só um exemplo simples)
        telaPerfil.getBtnLogout().addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Logout efetuado!");
            // Aqui você pode fechar a aplicação ou retornar para a tela de login
            System.exit(0);
        });

        // Adiciona o painel de perfil na janela
        frame.setContentPane(telaPerfil.getPanel());
        frame.setVisible(true);
    }
}
