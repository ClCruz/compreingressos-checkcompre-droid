package br.compreingressos.checkcompre.model;

/**
 * Created by Edicarlos on 07/03/2015.
 */
public class Local {

    public String id = "";
    public String name = "";

    public Local(String id, String name){
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
