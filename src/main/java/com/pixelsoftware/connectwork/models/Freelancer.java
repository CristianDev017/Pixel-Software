package com.pixelsoftware.connectwork.models;

import java.math.BigDecimal;

public class Freelancer {
    private int idUsuario;
    private String biografia;
    private String experiencia;
    private BigDecimal tarifaHora;
    private BigDecimal calificacionPromedio;
    private int totalCalificaciones;
    private BigDecimal saldo;

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }
    public String getExperiencia() { return experiencia; }
    public void setExperiencia(String experiencia) { this.experiencia = experiencia; }
    public BigDecimal getTarifaHora() { return tarifaHora; }
    public void setTarifaHora(BigDecimal tarifaHora) { this.tarifaHora = tarifaHora; }
    public BigDecimal getCalificacionPromedio() { return calificacionPromedio; }
    public void setCalificacionPromedio(BigDecimal calificacionPromedio) { this.calificacionPromedio = calificacionPromedio; }
    public int getTotalCalificaciones() { return totalCalificaciones; }
    public void setTotalCalificaciones(int totalCalificaciones) { this.totalCalificaciones = totalCalificaciones; }
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
}