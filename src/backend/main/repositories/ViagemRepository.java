package backend.main.repositories;

import backend.main.entities.Viagem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Viagem> buscarViagensPorEmailViajante(String email){
        Path path = Paths.get(caminhoArquivo);
        if (!Files.exists(path)){
            return new ArrayList<>();
        }
        try (ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(path))) {
            List<Viagem> viagens = (List<Viagem>) objectInputStream.readObject();
            return viagens.stream().filter(v -> v.getEmailViajante().equals(email)).toList();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao ler viagens");
        }
    }

    public List<Viagem> buscarViagensFiltradas(String emailViajante, String destino, String companhia, Double gasto) {
        List<Viagem> viagens = buscarViagensPorEmailViajante(emailViajante);

        return viagens.stream()
                .filter(v -> {
                    String chegada = v.getLugarDeChegada();
                    return destino == null || (chegada != null && chegada.toLowerCase().startsWith(destino.toLowerCase()));
                })
                .filter(v -> {
                    Double saldo = v.getSaldo();
                    return gasto == null || (saldo != null && saldo >= gasto);
                })
                .filter(v -> {
                    String comp = v.getCompanhia();
                    return companhia == null || (comp != null && comp.toLowerCase().startsWith(companhia.toLowerCase()));
                })
                .collect(Collectors.toList());
    }

    public boolean removerViagemPorId(int id, String emailViajante){
        List<Viagem> viagens = buscarTodasViagens();
        boolean isRemovido = viagens.removeIf(v -> v.getId() == id && v.getEmailViajante().equals(emailViajante));
        if (isRemovido){
            return salvarViagens(viagens);
        } else{
            return false;
        }
    }

    public boolean editarViagemPorId(int id, String emailViajante, Viagem viagemAtualizada){
        List<Viagem> viagens = buscarTodasViagens();
        for (int i = 0; i < viagens.size(); i++) {
            if (viagens.get(i).getId() == id && viagens.get(i).getEmailViajante().equals(emailViajante)){
                viagemAtualizada.setId(id);
                viagens.set(i, viagemAtualizada);
                return salvarViagens(viagens);
            }
        }
        return false;
    }

    public int buscarMaiorIdPorEmailViajante(String emailViajante){
        List<Viagem> viagens = buscarViagensPorEmailViajante(emailViajante);

        int maiorId = 0;
        for (Viagem viagem : viagens){
            if (viagem.getId() > maiorId){
                maiorId = viagem.getId();
            }
        }
        return maiorId;
    }
}
