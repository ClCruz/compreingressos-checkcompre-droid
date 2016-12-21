package br.compreingressos.checkcompre.model;

/**
 * Created by Edicarlos on 08/03/2015.
 */
public class Horario {

    public String id;
    public String value;

    public Horario(String id, String value){
        this.id = id;
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
