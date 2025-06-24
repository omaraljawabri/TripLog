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

/**
 * Classe responsável por se comunicar diretamente com os arquivos em que estão salvas as Viagens do sistema.
 * Métodos da classe são responsáveis por fazer operações CRUD (Create, Read, Update and Delete) da entidade Viagem
 * que serão usadas por toda a aplicação e chamados pela camada service.
 * */
public class ViagemRepository {

    private String caminhoArquivo;

    /**
     * Construtor que recebe o caminho do arquivo onde as viagens serão salvas, normalmente são arquivos com extensão .ser.
     * @param caminhoArquivo Caminho do arquivo em que viagens serão salvas
     * */
    public ViagemRepository(String caminhoArquivo){
        this.caminhoArquivo = caminhoArquivo;
    }

    /**
     * Método responsável por salvar uma viagem no arquivo
     * @param v Viagem a ser salva
     * @return Valor booleano informando se a viagem foi salva com sucesso (true) ou não (false)
     * */
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

    /**
     * Método responsável por salvar uma lista de viagens no arquivo
     * @param viagens Lista de viagens a ser salva
     * @return Valor booleano informando se a viagem foi salva com sucesso (true) ou não (false)
     * */
    public boolean salvarViagens(List<Viagem> viagens){
        Path path = Paths.get(caminhoArquivo);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(path))) {
            objectOutputStream.writeObject(viagens);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Método responsável por buscar todas as viagens salvas no arquivo.
     * @return Lista de viagem {@link Viagem}
     * */
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

    /**
     * Método responsável por buscar todas as viagens presentes no arquivo que possuam o atributo emailViajante igual
     * ao passado como parâmetro.
     * @param email Email do viajante a ser pesquisado nas viagens
     * @return Lista de viagens com email viajante passado {@link Viagem}
     * */
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

    /**
     * Método responsável por buscar todas as viagens presentes no arquivo de acordo com filtros passados como parâmetro e
     * do email do viajante também passado como parâmetro.
     * @param emailViajante Email do viajante a que pertencem as viagens
     * @param destino Destino por qual as viagens devem ser filtradas
     * @param companhia Companhia por qual as viagens devem ser filtradas
     * @param gasto Valor mínimo gasto que as viagens devem ter tido
     *
     * @return Lista de viagens que passaram no filtro {@link Viagem}
     * */
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

    /**
     * Método responsável por remover uma viagem do arquivo de acordo com o id da viagem passado como parâmetro.
     * É checado também se a viagem a ser removida pertence ao viajante com emailViajante passado.
     *
     * @param id Id da viagem a ser excluída
     * @param emailViajante Email do viajante que possui a viagem
     *
     * @return Valor booleano confirmando se viagem foi removida com sucesso (true) ou não (false)
     * */
    public boolean removerViagemPorId(int id, String emailViajante){
        List<Viagem> viagens = buscarTodasViagens();
        boolean isRemovido = viagens.removeIf(v -> v.getId() == id && v.getEmailViajante().equals(emailViajante));
        if (isRemovido){
            return salvarViagens(viagens);
        } else{
            return false;
        }
    }

    /**
     * Método responsável por atualizar uma viagem no arquivo de acordo com o id da viagem passado como parâmetro.
     * É checado também se a viagem a ser removida pertence ao viajante com emailViajante passado.
     *
     * @param id Id da viagem a ser editada.
     * @param emailViajante Email do viajante que possui a viagem.
     * @param viagemAtualizada Dados da viagem que devem substituir os antigos dados
     *
     * @return Valor booleano confirmando se a viagem foi removida com sucesso (true) ou não (false)
     * */
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

    /**
     * Método responsável por buscar o maior id de viagem presente no arquivo
     * @return Maior id de viagem presente no arquivo
     * */
    public int buscarMaiorId(){
        List<Viagem> viagens = buscarTodasViagens();

        int maiorId = 0;
        for (Viagem viagem : viagens){
            if (viagem.getId() > maiorId){
                maiorId = viagem.getId();
            }
        }
        return maiorId;
    }
}
