package backend.main.repositories;

import backend.main.entities.Viajante;
import backend.main.exceptions.ErroInternoException;

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
        List<Viajante> viajantes = buscarTodosViajantes();
        viajantes.add(v);
        return salvarViajantes(viajantes);
    }

    public boolean salvarViajantes(List<Viajante> viajantes){
        Path path = Paths.get(caminhoArquivo);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(path))) {
            objectOutputStream.writeObject(viajantes);
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    public boolean editarViajantePorEmail(String email, String nome, String senha){
        List<Viajante> viajantes = buscarTodosViajantes();
        for (Viajante viajante : viajantes){
            if (viajante.getEmail().equals(email)){
                viajante.setNome(nome != null ? nome : viajante.getNome());
                viajante.setSenha(senha != null ? senha : viajante.getSenha());
                break;
            }
        }
        return salvarViajantes(viajantes);
    }

    public Viajante buscarViajantePorEmail(String email){
        List<Viajante> viajantes = buscarTodosViajantes();

        List<Viajante> viajante = viajantes.stream().filter(v -> v.getEmail().equals(email)).toList();

        if (viajante.isEmpty()){
            return null;
        }

        return viajante.getFirst();
    }

    public int buscarMaiorId(){
        List<Viajante> viajantes = buscarTodosViajantes();
        int maiorId = 0;

        for (Viajante viajante : viajantes){
            if (viajante.getId() > maiorId){
                maiorId = viajante.getId();
            }
        }

        return maiorId;
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
            throw new ErroInternoException("Erro ao ler viajantes");
        }
    }
}
