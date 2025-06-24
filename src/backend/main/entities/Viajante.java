package backend.main.entities;

import backend.main.repositories.ViajanteRepository;
import backend.main.services.ViajanteService;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um usuário do sistema, no caso um Viajante, responsável por registrar sua viagens.
 * Cada Viajante possui um id, nome, email, senha e uma lista de viagens {@link Viagem}
 * */
public class Viajante implements Serializable {
    @Serial
    private static final long serialVersionUID = -6772402613425968371L;

    private static int contador;
    private int id;
    private String nome;
    private String email;
    private String senha;
    private List<Viagem> viagens = new ArrayList<>();

    /**
     * Construtor que não recebe parâmetro utilizado para criação de um Viajante
     * Utilizado para facilitação dos testes unitários
     * Caso o contador de id seja igual a 0, é buscado o maior id armazenado no arquivo e incrementado
     * mais 1 ao valor buscado. Com isso, id's repetidos após a reinicialização do programa são evitados
     * */
    public Viajante() {
        if (Viajante.contador == 0){
            ViajanteRepository viajanteRepository = new ViajanteRepository("viajante.ser");
            ViajanteService viajanteService = new ViajanteService(viajanteRepository);
            Viajante.contador = viajanteService.buscarMaiorId();
        }
        Viajante.contador++;
        this.id = Viajante.contador;
    }

    /**
     * Construtor utilizado para instanciar um Viajante
     * Caso o contador de id seja igual a 0, é buscado o maior id armazenado no arquivo e incrementado
     * mais 1 ao valor buscado. Com isso, id's repetidos após a reinicialização do programa são evitados
     * @param nome Nome do viajante
     * @param senha Senha do viajante
     * @param email Email do viajante
     * */
    public Viajante(String nome, String senha, String email) {
        if (Viajante.contador == 0){
            ViajanteRepository viajanteRepository = new ViajanteRepository("viajante.ser");
            ViajanteService viajanteService = new ViajanteService(viajanteRepository);
            Viajante.contador = viajanteService.buscarMaiorId();
        }
        Viajante.contador++;
        this.id = Viajante.contador;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    /**
     * Busca e retorna o id do viajante
     * @return Id do viajante
     * */
    public int getId() {
        return id;
    }

    /**
     * Busca e retorna o email do viajante
     * @return Email do viajante
     * */
    public String getEmail() {
        return email;
    }

    /**
     * Busca e retorna o nome do viajante
     * @return Nome do viajante
     * */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do viajante
     * @param nome Nome do viajante
     * */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Busca e retorna a senha do viajante, esse método só é utilizado após criptografia da senha com SHA-256
     * feito no {@link backend.main.utils.SenhaUtil}
     * @return Senha do viajante
     * */
    public String getSenha() {
        return senha;
    }

    /**
     * Define a senha do viajante
     * @param senha Senha do viajante
     * */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * Busca e retorna as viagens do viajante
     * @return Lista de viagens {@link Viagem}
     * */
    public List<Viagem> getViagens() {
        return viagens;
    }

    /**
     * Define as viagens do viajante
     * @param viagens Lista de viagens {@link Viagem}
     * */
    public void setViagens(List<Viagem> viagens) {
        this.viagens = viagens;
    }

    /**
     * Método estático responsável por resetar o contador utilizado para atribuir o id ao objeto.
     * Uso somente em cenários de teste para facilitação dos testes unitários
     * */
    public static void resetarContador(){
        Viajante.contador = 0;
    }
}
