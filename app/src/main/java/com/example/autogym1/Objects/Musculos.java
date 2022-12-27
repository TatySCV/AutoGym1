package com.example.autogym1.Objects;

public class Musculos {

    private int id_musculo;
    private String nombre_musculo;

    public Musculos(int id_musculo, String nombre_musculo) {
        this.id_musculo = id_musculo;
        this.nombre_musculo = nombre_musculo;
    }

    public Musculos() {
    }

    public int getId_musculo() {
        return id_musculo;
    }

    public void setId_musculo(int id_musculo) {
        this.id_musculo = id_musculo;
    }

    public String getNombre_musculo() {
        return nombre_musculo;
    }

    public void setNombre_musculo(String nombre_musculo) {
        this.nombre_musculo = nombre_musculo;
    }

    @Override
    public String toString() {
        return nombre_musculo;
    }
}
