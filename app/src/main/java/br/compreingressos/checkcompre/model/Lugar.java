package br.compreingressos.checkcompre.model;

import java.io.Serializable;

/**
 * Created by edicarlosbarbosa on 25/05/15.
 */
public class Lugar implements Serializable {

    private String cliente;
    private String lugar;
    private String telefone;
    private String documento;
    private String cpf;
    private String dtEntrada;
    private String setor;
    private String sala;
    private String tipoBilhete;
    private String valor;

    public Lugar(String cliente, String lugar){
        this.cliente = cliente;
        this.lugar = lugar;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDtEntrada() {
        return dtEntrada;
    }

    public void setDtEntrada(String dtEntrada) {
        this.dtEntrada = dtEntrada;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public String getTipoBilhete() {
        return tipoBilhete;
    }

    public void setTipoBilhete(String tipoBilhete) {
        this.tipoBilhete = tipoBilhete;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
