package br.compreingressos.checkcompre.model;

/**
 * Created by Edicarlos on 08/03/2015.
 */
public class Evento {
    public String id;
    public String name;

    public Evento(String id, String name){
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
