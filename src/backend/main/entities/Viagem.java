package backend.main.entities;

import backend.main.repositories.ViagemRepository;
import backend.main.services.ViagemService;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma Viagem que o usuário Viajante fez e deseja registrar.
 * Cada viagem possui um id, lugar de partida, lugar de chegada, data de chegada e termino da viagem,
 * uma lista de hospedagens {@link Hospedagem}, uma lista de deslocamentos {@link Deslocamento}, saldo da viagem,
 * pessoa que fez companhia para o usuário, uma lista de atividades {@link Atividade} e o email do viajante que fez a viagem
 * */
public class Viagem implements Serializable {
    @Serial
    private static final long serialVersionUID = -2543523212611914182L;

    private static int contador;
    private int id;
    private String lugarDePartida;
    private String lugarDeChegada;
    private LocalDate dataDeInicio;
    private LocalDate dataTermino;
    private List<Deslocamento> deslocamentos = new ArrayList<>();
    private List<Hospedagem> hospedagens = new ArrayList<>();
    private double saldo;
    private String companhia;
    private List<Atividade> atividades = new ArrayList<>();
    private String emailViajante;

    /**
     * Construtor que não recebe parâmetros utilizado para criação de uma Viagem
     * Utilizado para facilitação nos testes unitários
     * Caso o contador de id seja igual a 0, é buscado o maior id armazenado no arquivo e incrementado
     * mais 1 ao valor buscado. Com isso, id's repetidos após a reinicialização do programa são evitados
     * */
    public Viagem() {
        if (Viagem.contador == 0){
            ViagemRepository viagemRepository = new ViagemRepository("viagem.ser");
            ViagemService viagemService = new ViagemService(viagemRepository);
            Viagem.contador = viagemService.buscarMaiorId();
        }
        Viagem.contador++;
        this.id = Viagem.contador;
    }

    /**
     * Construtor utilizado para instanciar uma Viagem.
     * Caso o contador de id seja igual a 0, é buscado o maior id armazenado no arquivo e incrementado
     * mais 1 ao valor buscado. Com isso, id's repetidos após a reinicialização do programa são evitados
     * @param lugarDePartida Lugar de partida da viagem
     * @param lugarDeChegada Lugar de chegada da viagem
     * @param deslocamentos Lista de deslocamentos {@link Deslocamento} que ocorreram na viagem
     * @param hospedagens Lista de hospedagens {@link Hospedagem} que ocorreram na viagem
     * @param saldo Saldo pré-estipulado para gasto na viagem
     * @param companhia Pessoa que foi juntamente com o usuário viajante para a viagem
     * @param atividades Lista de atividades {@link Atividade} que foram feitas na viagem
     * @param emailViajante Email do viajante que registrou essa viagem no sistema
     * @param dataDeInicio Data de início da viagem
     * @param dataTermino Data em que a viagem foi encerrada
     * */
    public Viagem(String lugarDePartida, String lugarDeChegada, List<Deslocamento> deslocamentos, List<Hospedagem> hospedagens, double saldo, String companhia, List<Atividade> atividades, String emailViajante, LocalDate dataDeInicio, LocalDate dataTermino) {
        if (Viagem.contador == 0){
            ViagemRepository viagemRepository = new ViagemRepository("viagem.ser");
            ViagemService viagemService = new ViagemService(viagemRepository);
            Viagem.contador = viagemService.buscarMaiorId();
        }
        Viagem.contador++;
        this.id = Viagem.contador;
        this.lugarDePartida = lugarDePartida;
        this.lugarDeChegada = lugarDeChegada;
        this.deslocamentos = deslocamentos;
        this.hospedagens = hospedagens;
        this.saldo = saldo;
        this.companhia = companhia;
        this.atividades = atividades;
        this.emailViajante = emailViajante;
        this.dataDeInicio = dataDeInicio;
        this.dataTermino = dataTermino;
    }

    /**
     * Busca e retorna o email do viajante que registrou a viagem
     * @return Email do viajante que registrou a viagem
     * */
    public String getEmailViajante() {
        return emailViajante;
    }

    /**
     * Define o email do viajante que registrou a viagem
     * @param emailViajante Email do viajante que registrou a viagem
     * */
    public void setEmailViajante(String emailViajante) {
        this.emailViajante = emailViajante;
    }

    /**
     * Busca e retorna o id da viagem
     * @return Id da viagem
     * */
    public int getId() {
        return id;
    }

    /**
     * Define o id da viagem.
     * Utilizado apenas em testes unitários
     * @param id Id da viagem
     * */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Busca e retorna o lugar de partida da viagem
     * @return Lugar de partida da viagem
     * */
    public String getLugarDePartida() {
        return lugarDePartida;
    }

    /**
     * Define o lugar de partida da viagem
     * @param lugarDePartida Lugar de partida da viagem
     * */
    public void setLugarDePartida(String lugarDePartida) {
        this.lugarDePartida = lugarDePartida;
    }

    /**
     * Busca e retorna o lugar de chegada da viagem
     * @return Lugar de chegada da viagem
     * */
    public String getLugarDeChegada() {
        return lugarDeChegada;
    }

    /**
     * Busca e retorna o saldo pré-estipulado da viagem
     * @return Saldo pré-estipulado da viagem
     * */
    public double getSaldo() {
        return saldo;
    }

    /**
     * Busca e retorna a data de início da viagem
     * @return Data de início da viagem
     * */
    public LocalDate getDataDeInicio() {
        return dataDeInicio;
    }

    /**
     * Busca e retorna a data de término da viagem
     * @return Data de término da viagem
     * */
    public LocalDate getDataTermino() {
        return dataTermino;
    }

    /**
     * Define o lugar de chegada da viagem (destino)
     * @param lugarDeChegada Lugar de chegada da viagem (destino)
     * */
    public void setLugarDeChegada(String lugarDeChegada) {
        this.lugarDeChegada = lugarDeChegada;
    }

    /**
     * Busca e retorna os deslocamentos que ocorreram durante a viagem
     * @return Lista de deslocamentos {@link Deslocamento}
     * */
    public List<Deslocamento> getDeslocamentos() {
        return deslocamentos;
    }

    /**
     * Define os deslocamentos que aconteceram na viagem
     * @param deslocamentos Deslocamentos que ocorreram na viagem {@link Deslocamento}
     * */
    public void setDeslocamentos(List<Deslocamento> deslocamentos) {
        this.deslocamentos = deslocamentos;
    }

    /**
     * Busca e retorna as hospedagens que ocorreram na viagem
     * @return Lista de hospedagens {@link Hospedagem}
     * */
    public List<Hospedagem> getHospedagens() {
        return hospedagens;
    }

    /**
     * Define as hospedagens que ocorreram durante a viagem
     * @param hospedagens Hospedagens que ocorreram na viagem {@link Hospedagem}
     * */
    public void setHospedagens(List<Hospedagem> hospedagens) {
        this.hospedagens = hospedagens;
    }

    /**
     * Define o saldo que será utilizado durante a viagem
     * @param saldo Saldo para uso na viagem
     * */
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    /**
     * Busca e retorna a companhia que foi junto com o viajante para a viagem
     * @return Companhia que esteve na viagem junto ao viajante
     * */
    public String getCompanhia() {
        return companhia;
    }

    /**
     * Define a companhia que foi junto com o viajante para a viagem
     * @param companhia Companhia que esteve com o viajante na viagem
     * */
    public void setCompanhia(String companhia) {
        this.companhia = companhia;
    }

    /**
     * Busca e retorna as atividades feitas durante a viagem
     * @return Lista de atividades {@link Atividade}
     * */
    public List<Atividade> getAtividades() {
        return atividades;
    }

    /**
     * Define as ativisades que foram feitas durante a viagem
     * @param atividades Lista de atividades feitas na viagem {@link Atividade}
     * */
    public void setAtividades(List<Atividade> atividades) {
        this.atividades = atividades;
    }

    /**
     * Define a data de inicio da viagem
     * @param dataDeInicio Data de inicio da viagem
     * */
    public void setDataDeInicio(LocalDate dataDeInicio) {
        this.dataDeInicio = dataDeInicio;
    }

    /**
     * Define a data de término da viagem
     * @param dataTermino Data de término da viagem
     * */
    public void setDataTermino(LocalDate dataTermino) {
        this.dataTermino = dataTermino;
    }

    /**
     * Método responsável por calcular o gasto total da viagem, incluido gastos de deslocamento,
     * hospedagem e das atividades.
     * @return Gasto total da viagem
     * */
    public double calcularTotalGastos(){
        double gastos = 0;
        for (Deslocamento deslocamento : deslocamentos){
            gastos += deslocamento.getCusto();
        }

        for (Hospedagem hospedagem : hospedagens){
            gastos += hospedagem.calculaTotal();
        }

        for (Atividade atividade : atividades){
            gastos += atividade.getGasto();
        }

        return gastos;
    }

    /**
     * Método responsável por calcular a duração da viagem, pegando o intervalo de dias entre a dataDeInicio e dataDeTermino
     * @return Dias de duração da viagem
     * */
    public int calcularDiasDeViagem() {
        if (this.dataDeInicio != null && this.dataTermino != null) {
            return (int) ChronoUnit.DAYS.between(this.dataDeInicio, this.dataTermino);
        }
        return 0;
    }

    /**
     * Método estático responsável por resetar o contador utilizado para atribuir o id ao objeto.
     * Uso somente em cenários de teste para facilitação dos testes unitários
     * */
    public static void resetarContador(){
        Viagem.contador = 0;
    }
}
