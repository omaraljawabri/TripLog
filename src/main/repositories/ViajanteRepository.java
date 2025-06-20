package main.repositories;

import main.entities.Viajante;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ViajanteRepository {

    private String caminhoArquivo;

    public ViajanteRepository(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public boolean salvarViajante(Viajante v){
        Path path = Paths.get(caminhoArquivo);
        List<Viajante> viajantes = buscarTodosViajantes();
        viajantes.add(v);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(path))) {
            objectOutputStream.writeObject(viajantes);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Viajante buscarViajantePorEmail(String email){
        List<Viajante> viajantes = buscarTodosViajantes();
        System.out.println(viajantes.get(0).getEmail());

        List<Viajante> viajante = viajantes.stream().filter(v -> v.getEmail().equals(email)).toList();

        if (viajante.isEmpty()){
            return null;
        }

        return viajante.getFirst();
    }

    private List<Viajante> buscarTodosViajantes(){
        Path path = Paths.get(caminhoArquivo);
        if (!Files.exists(path)){
            return new ArrayList<>();
        }
        try (ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(path))) {
            List<Viajante> viajantes = (List<Viajante>) objectInputStream.readObject();
            return viajantes;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao ler viajantes");
        }
    }
}
