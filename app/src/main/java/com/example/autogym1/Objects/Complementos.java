package com.example.autogym1.Objects;

public class Complementos {

    private int id_complemento;
    private String nombre_complemento;
    private String imagen_complemento;

    public Complementos() {
    }

    public Complementos(int id_complemento, String nombre_complemento) {
        this.id_complemento = id_complemento;
        this.nombre_complemento = nombre_complemento;
    }

    public Complementos(int id_complemento, String nombre_complemento, String imagen_complemento) {
        this.id_complemento = id_complemento;
        this.nombre_complemento = nombre_complemento;
        this.imagen_complemento = imagen_complemento;
    }

    public int getId_complemento() {
        return id_complemento;
    }

    public void setId_complemento(int id_complemento) {
        this.id_complemento = id_complemento;
    }

    public String getNombre_complemento() {
        return nombre_complemento;
    }

    public void setNombre_complemento(String nombre_complemento) {
        this.nombre_complemento = nombre_complemento;
    }

    public String getImagen_complemento() {
        return imagen_complemento;
    }

    public void setImagen_complemento(String imagen_complemento) {
        this.imagen_complemento = imagen_complemento;
    }

    @Override
    public String toString() {
        return nombre_complemento;
    }

    
}
