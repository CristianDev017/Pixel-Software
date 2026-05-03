package com.pixelsoftware.connectwork.models;

import java.math.BigDecimal;

public class Cliente {
    private int idUsuario;
    private String descripcion;
    private String sector;
    private String sitioWeb;
    private BigDecimal saldo;

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }
    public String getSitioWeb() { return sitioWeb; }
    public void setSitioWeb(String sitioWeb) { this.sitioWeb = sitioWeb; }
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
}