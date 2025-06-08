package main.repositories;

import main.entities.Viagem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ViagemRepository {

    private String caminhoArquivo;

    public ViagemRepository(String caminhoArquivo){
        this.caminhoArquivo = caminhoArquivo;
    }

    public boolean salvarViagem(Viagem v){
        Path path = Paths.get(caminhoArquivo);
        List<Viagem> viagens = buscarTodasViagens();
        viagens.add(v);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(path))) {
            objectOutputStream.writeObject(viagens);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean salvarViagens(List<Viagem> viagens){
        Path path = Paths.get(caminhoArquivo);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(path))) {
            objectOutputStream.writeObject(viagens);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public List<Viagem> buscarTodasViagens(){
        Path path = Paths.get(caminhoArquivo);
        if (!Files.exists(path)){
            return new ArrayList<>();
        }
        try (ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(path))) {
            List<Viagem> viagens = (List<Viagem>) objectInputStream.readObject();
            return viagens;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao ler viagens");
        }
    }

    public List<Viagem> buscarViagensPorIdViajante(int idViajante){
        Path path = Paths.get(caminhoArquivo);
        if (!Files.exists(path)){
            return new ArrayList<>();
        }
        try (ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(path))) {
            List<Viagem> viagens = (List<Viagem>) objectInputStream.readObject();
            return viagens.stream().filter(v -> v.getIdViajante() == idViajante).toList();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao ler viagens");
        }
    }

    public Viagem buscarViagemPorId(int id, int idViajante){
        List<Viagem> viagens = buscarViagensPorIdViajante(idViajante);
        List<Viagem> viagem = viagens.stream().filter(v -> v.getId() == id).toList();
        if (viagem.isEmpty()){
            return null;
        }
        return viagem.getFirst();
    }

    public boolean removerViagemPorId(int id, int idViajante){
        List<Viagem> viagens = buscarTodasViagens();
        boolean isRemovido = viagens.removeIf(v -> v.getId() == id && v.getIdViajante() == idViajante);
        if (isRemovido){
            return salvarViagens(viagens);
        } else{
            return false;
        }
    }

    public boolean editarViagemPorId(int id, int idViajante, Viagem viagemAtualizada){
        List<Viagem> viagens = buscarTodasViagens();
        for (int i = 0; i < viagens.size(); i++) {
            if (viagens.get(i).getId() == id && viagens.get(i).getIdViajante() == idViajante){
                viagemAtualizada.setId(id);
                viagens.set(i, viagemAtualizada);
                return salvarViagens(viagens);
            }
        }
        return false;
    }
}
