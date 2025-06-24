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

/**
 * Classe responsável por se comunicar diretamente com os arquivos em que estão salvos os Viajantes do sistema.
 * Métodos da classe são responsáveis por fazer operações CRUD (Create, Read, Update and Delete) da entidade Viajante
 * que serão usadas por toda a aplicação e chamados pela camada service.
 * */
public class ViajanteRepository {

    private String caminhoArquivo;

    /**
     * Construtor que recebe o caminho do arquivo onde os viajantes serão salvos, normalmente são arquivos com extensão .ser.
     * @param caminhoArquivo Caminho do arquivo em que viajantes serão salvos
     * */
    public ViajanteRepository(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    /**
     * Método responsável por salvar um viajante no arquivo
     * @param v Viajante que será salvo
     * @return Valor booleano confirmando se viajante foi salvo com sucesso (true) ou não (false)
     * */
    public boolean salvarViajante(Viajante v){
        List<Viajante> viajantes = buscarTodosViajantes();
        viajantes.add(v);
        return salvarViajantes(viajantes);
    }

    /**
     * Método responsável por salvar uma lista de viajantes no arquivo
     * @param viajantes Lista de viajantes a ser salva no arquivo
     * @return Valor booleano confirmando se viajantes foram salvos com sucesso (true) ou não (false)
     * */
    public boolean salvarViajantes(List<Viajante> viajantes){
        Path path = Paths.get(caminhoArquivo);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(path))) {
            objectOutputStream.writeObject(viajantes);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Método responsável por editar um viajante presente no arquivo de acordo com o email dele passado como parâmetro
     * É buscado o viajante com email passado e seu nome e senha são atualizados.
     *
     * @param email Email do viajante a ser atualizado
     * @param nome Novo nome do viajante que será salvo
     * @param senha Nova senha do viajante que será salva
     *
     * @return Valor booleano confirmando se o viajante foi editado com sucesso (true) ou não (false)
     * */
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

    /**
     * Método responsável por buscar um viajante salvo no arquivo de acordo com o email passado como parâmetro
     *
     * @param email Email do viajante que deve ser buscado no arquivo
     *
     * @return Viajante que foi buscado no arquivo
     * */
    public Viajante buscarViajantePorEmail(String email){
        List<Viajante> viajantes = buscarTodosViajantes();

        List<Viajante> viajante = viajantes.stream().filter(v -> v.getEmail().equals(email)).toList();

        if (viajante.isEmpty()){
            return null;
        }

        return viajante.getFirst();
    }

    /**
     * Método responsável por buscar o maior id de viajante salvo em arquivo até o momento
     *
     * @return Maior id de viajante salvo no arquivo até o momento
     * */
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

    /**
     * Método responsável por buscar todos os viajantes presentes no arquivo até o momento.
     * Método auxiliar utilizado por outros e por isso é private.
     *
     * @return Lista de viajantes salvos no arquivo até o momento
     * */
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
