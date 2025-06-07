package repositories;

import entities.Viagem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ViagemRepository {

    private static final String CAMINHO_ARQUIVO = "Viagem.ser";

    public static boolean salvarViagem(Viagem v){
        Path path = Paths.get(CAMINHO_ARQUIVO);
        List<Viagem> viagens = buscarTodasViagens();
        viagens.add(v);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(path))) {
            objectOutputStream.writeObject(viagens);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean salvarViagens(List<Viagem> viagens){
        Path path = Paths.get(CAMINHO_ARQUIVO);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(path))) {
            objectOutputStream.writeObject(viagens);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static List<Viagem> buscarTodasViagens(){
        Path path = Paths.get(CAMINHO_ARQUIVO);
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

    public static Viagem buscarViagemPorId(int id){
        List<Viagem> viagens = buscarTodasViagens();
        List<Viagem> viagem = viagens.stream().filter(v -> v.getId() == id).toList();
        if (viagem.isEmpty()){
            return null;
        }
        return viagem.getFirst();
    }

    public static boolean removerViagemPorId(int id){
        List<Viagem> viagens = buscarTodasViagens();
        boolean isRemovido = viagens.removeIf(v -> v.getId() == id);
        if (isRemovido){
            return salvarViagens(viagens);
        } else{
            return false;
        }
    }

    public static boolean editarViagemPorId(int id, Viagem viagemAtualizada){
        List<Viagem> viagens = buscarTodasViagens();
        for (int i = 0; i < viagens.size(); i++) {
            if (viagens.get(i).getId() == id){
                viagemAtualizada.setId(id);
                viagens.set(i, viagemAtualizada);
                return salvarViagens(viagens);
            }
        }
        return false;
    }
}
