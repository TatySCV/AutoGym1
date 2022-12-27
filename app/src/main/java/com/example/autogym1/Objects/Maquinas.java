package com.example.autogym1.Objects;

public class Maquinas {

    private int id_maquina;
    private String nombre_maquina;
    private String musculo;
    private String descripción_maquina;
    private int imagen_maquina;

    public Maquinas() {
    }

    public Maquinas(int id_maquina, String nombre_maquina) {
        this.id_maquina = id_maquina;
        this.nombre_maquina = nombre_maquina;
    }

    public Maquinas(int id_maquina, String nombre_maquina, String musculo) {
        this.id_maquina = id_maquina;
        this.nombre_maquina = nombre_maquina;
        this.musculo = musculo;
    }

    public Maquinas(int id_maquina, String nombre_maquina, int imagen_maquina) {
        this.id_maquina = id_maquina;
        this.nombre_maquina = nombre_maquina;
        this.imagen_maquina = imagen_maquina;
    }

    public Maquinas(int id_maquina, String nombre_maquina, String descripción_maquina, int imagen_maquina) {
        this.id_maquina = id_maquina;
        this.nombre_maquina = nombre_maquina;
        this.descripción_maquina = descripción_maquina;
        this.imagen_maquina = imagen_maquina;
    }

    public int getId_maquina() {
        return id_maquina;
    }

    public void setId_maquina(int id_maquina) {
        this.id_maquina = id_maquina;
    }

    public String getNombre_maquina() {
        return nombre_maquina;
    }

    public void setNombre_maquina(String nombre_maquina) {
        this.nombre_maquina = nombre_maquina;
    }

    public String getDescripción_maquina() {
        return descripción_maquina;
    }

    public void setDescripción_maquina(String descripción_maquina) {
        this.descripción_maquina = descripción_maquina;
    }

    public int getImagen_maquina() {
        return imagen_maquina;
    }

    public void setImagen_maquina(int imagen_maquina) {
        this.imagen_maquina = imagen_maquina;
    }

    public String getMusculo() {
        return musculo;
    }

    public void setMusculo(String musculo) {
        this.musculo = musculo;
    }

    @Override
    public String toString() {
        return nombre_maquina;
    }
}
