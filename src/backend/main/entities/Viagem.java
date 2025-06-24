package backend.main.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Viagem implements Serializable {
    private static int contador;
    private int id;
    private String lugarDePartida;
    private String lugarDeChegada;
    private LocalDate dataChegada;
    private LocalDate dataTermino;
    private List<Deslocamento> deslocamentos = new ArrayList<>();
    private List<Hospedagem> hospedagens = new ArrayList<>();
    private double saldo;
    private String companhia;
    private List<Atividade> atividades = new ArrayList<>();
    private String emailViajante;

    public Viagem() {
        Viagem.contador++;
        this.id = Viagem.contador;
    }

    public Viagem(String lugarDePartida, String lugarDeChegada, List<Deslocamento> deslocamentos, List<Hospedagem> hospedagens, double saldo, String companhia, List<Atividade> atividades, String emailViajante, LocalDate dataChegada, LocalDate dataTermino) {
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
        this.dataChegada = dataChegada;
        this.dataTermino = dataTermino;
    }

    public String getEmailViajante() {
        return emailViajante;
    }

    public void setEmailViajante(String emailViajante) {
        this.emailViajante = emailViajante;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getLugarDePartida() {
        return lugarDePartida;
    }

    public void setLugarDePartida(String lugarDePartida) {
        this.lugarDePartida = lugarDePartida;
    }

    public String getLugarDeChegada() {
        return lugarDeChegada;
    }

    public double getSaldo() {
        return saldo;
    }

    public LocalDate getDataChegada() {
        return dataChegada;
    }

    public LocalDate getDataTermino() {
        return dataTermino;
    }

    public void setLugarDeChegada(String lugarDeChegada) {
        this.lugarDeChegada = lugarDeChegada;
    }

    public List<Deslocamento> getDeslocamentos() {
        return deslocamentos;
    }

    public void setDeslocamentos(List<Deslocamento> deslocamentos) {
        this.deslocamentos = deslocamentos;
    }

    public List<Hospedagem> getHospedagens() {
        return hospedagens;
    }

    public void setHospedagens(List<Hospedagem> hospedagens) {
        this.hospedagens = hospedagens;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getCompanhia() {
        return companhia;
    }

    public void setCompanhia(String companhia) {
        this.companhia = companhia;
    }

    public List<Atividade> getAtividades() {
        return atividades;
    }

    public void setAtividades(List<Atividade> atividades) {
        this.atividades = atividades;
    }

    public void setDataChegada(LocalDate dataChegada) {
        this.dataChegada = dataChegada;
    }

    public void setDataTermino(LocalDate dataTermino) {
        this.dataTermino = dataTermino;
    }

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

    public int calcularDiasDeViagem() {
        if (this.dataChegada != null && this.dataTermino != null) {
            return (int) ChronoUnit.DAYS.between(this.dataChegada, this.dataTermino);
        }
        return 0;
    }
}
