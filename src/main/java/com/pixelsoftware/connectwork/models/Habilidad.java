package com.pixelsoftware.connectwork.models;

public class Habilidad {
    private int id;
    private int idCategoria;
    private String nombre;
    private boolean activa;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
}