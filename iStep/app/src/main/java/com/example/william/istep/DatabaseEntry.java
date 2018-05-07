package com.example.william.istep;

/**
 * Created by William on 28/04/2017.
 */

public class DatabaseEntry {

    private int id;
    private int steps;
    private int date;

    public DatabaseEntry(){

    }

    public DatabaseEntry(int id, int steps){
        this.id = id;
        this.steps = steps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSteps() {
        return steps;
    }

    public int getDate(){
        return this.date;
    }


    public void setSteps(int steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "DatabaseEntry{" +
                "id=" + id +
                ", steps=" + steps +
                '}';
    }
}
