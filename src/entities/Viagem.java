package entities;

import java.util.List;

public class Viagem {
    private static int contador;
    private int id;
    private String lugarDePartida;
    private String lugarDeChegada;
    private List<Deslocamento> deslocamentos;
    private List<Hospedagem> hospedagens;
    private double saldo;
    private int diasPercorridos;
    private String companhia;
    private List<Atividade> atividades;

    public Viagem() {
        Viagem.contador++;
        this.id = Viagem.contador;
    }

    public Viagem(String lugarDePartida, String lugarDeChegada, List<Deslocamento> deslocamentos, List<Hospedagem> hospedagens, double saldo, int diasPercorridos, String companhia, List<Atividade> atividades) {
        Viagem.contador++;
        this.id = Viagem.contador;
        this.lugarDePartida = lugarDePartida;
        this.lugarDeChegada = lugarDeChegada;
        this.deslocamentos = deslocamentos;
        this.hospedagens = hospedagens;
        this.saldo = saldo;
        this.diasPercorridos = diasPercorridos;
        this.companhia = companhia;
        this.atividades = atividades;
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

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public int getDiasPercorridos() {
        return diasPercorridos;
    }

    public void setDiasPercorridos(int diasPercorridos) {
        this.diasPercorridos = diasPercorridos;
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
}
