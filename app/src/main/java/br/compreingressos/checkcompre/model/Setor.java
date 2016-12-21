package br.compreingressos.checkcompre.model;

/**
 * Created by edicarlosbarbosa on 22/08/16.
 */

public class Setor {

    public String id;
    public String name;

    public Setor (String id, String name){
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
