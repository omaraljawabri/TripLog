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

    public static List<Viagem> buscarTodasViagens(){
        Path path = Paths.get(CAMINHO_ARQUIVO);
        if (!Files.exists(path)){
            return new ArrayList<>();
        }
        try (ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(path))) {
            List<Viagem> viagens = (List<Viagem>) objectInputStream.readObject();
            for (Viagem viagem : viagens){
                System.out.println(viagem);
            }
            return viagens;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao ler viagens");
        }
    }
}
