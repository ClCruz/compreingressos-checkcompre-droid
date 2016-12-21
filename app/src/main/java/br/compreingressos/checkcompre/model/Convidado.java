package br.compreingressos.checkcompre.model;

import java.io.Serializable;

/**
 * Created by edicarlosbarbosa on 16/11/15.
 */
public class Convidado implements Serializable {

    private String id;
    private String nome;
    private String cpf;
    private String email;
    private String celular;
    private String qtdeLugares;
    private String convidadoPor;
    private String tipoConvite;
    private String confirmado;

    public Convidado(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getQtdeLugares() {
        return qtdeLugares;
    }

    public void setQtdeLugares(String qtdeLugares) {
        this.qtdeLugares = qtdeLugares;
    }

    public String getConvidadoPor() {
        return convidadoPor;
    }

    public void setConvidadoPor(String convidadoPor) {
        this.convidadoPor = convidadoPor;
    }

    public String getTipoConvite() {
        return tipoConvite;
    }

    public void setTipoConvite(String tipoConvite) {
        this.tipoConvite = tipoConvite;
    }

    public String getConfirmado() {
        return confirmado;
    }

    public void setConfirmado(String confirmado) {
        this.confirmado = confirmado;
    }
}
