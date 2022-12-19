package com.example.autogym1.Objects;

import java.util.ArrayList;

public class Ejercicios {

    private int id_ejercicio;
    private String nombre_ejercicio;
    private ArrayList<Maquinas> maquinas;
    private ArrayList<Complementos> complementos;

    public Ejercicios() {
    }

    public Ejercicios(int id_ejercicio, String nombre_ejercicio) {
        this.id_ejercicio = id_ejercicio;
        this.nombre_ejercicio = nombre_ejercicio;
    }

    public Ejercicios(int id_ejercicio, String nombre_ejercicio, ArrayList<Maquinas> maquinas, ArrayList<Complementos> complementos) {
        this.id_ejercicio = id_ejercicio;
        this.nombre_ejercicio = nombre_ejercicio;
        this.maquinas = maquinas;
        this.complementos = complementos;
    }

    public int getId_ejercicio() {
        return id_ejercicio;
    }

    public void setId_ejercicio(int id_ejercicio) {
        this.id_ejercicio = id_ejercicio;
    }

    public String getNombre_ejercicio() {
        return nombre_ejercicio;
    }

    public void setNombre_ejercicio(String nombre_ejercicio) {
        this.nombre_ejercicio = nombre_ejercicio;
    }

    public ArrayList<Maquinas> getMaquinas() {
        return maquinas;
    }

    public void setMaquinas(ArrayList<Maquinas> maquinas) {
        this.maquinas = maquinas;
    }

    public ArrayList<Complementos> getComplementos() {
        return complementos;
    }

    public void setComplementos(ArrayList<Complementos> complementos) {
        this.complementos = complementos;
    }

    @Override
    public String toString() {
        return id_ejercicio+nombre_ejercicio+maquinas+complementos;
    }
}
