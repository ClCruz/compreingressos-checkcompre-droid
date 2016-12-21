package br.compreingressos.checkcompre.model;

/**
 * Created by edicarlosbarbosa on 17/10/16.
 */

public class Assinatura {

    public String id;
    public String value;

    public Assinatura(String id, String value){
        this.id = id;
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
