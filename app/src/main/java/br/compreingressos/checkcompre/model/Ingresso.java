package br.compreingressos.checkcompre.model;

/**
 * Created by edicarlosbarbosa on 24/05/15.
 */
public class Ingresso {

    private String tipoBilhete;
    private int quantidade;
    private String valor;
    private String total;

    public Ingresso(String tipoBilhete, int quantidade, String valor, String total){
        this.tipoBilhete = tipoBilhete;
        this.quantidade = quantidade;
        this.valor = valor;
        this.total = total;
    }

    public String getTipoBilhete() {
        return tipoBilhete;
    }

    public void setTipoBilhete(String tipoBilhete) {
        this.tipoBilhete = tipoBilhete;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return this.tipoBilhete;
    }
}
