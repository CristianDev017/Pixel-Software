package com.pixelsoftware.connectwork.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Propuesta {
    private int id;
    private int idProyecto;
    private int idFreelancer;
    private BigDecimal monto;
    private int plazoDias;
    private String carta;
    private String estado;
    private LocalDateTime fechaEnvio;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdProyecto() { return idProyecto; }
    public void setIdProyecto(int idProyecto) { this.idProyecto = idProyecto; }
    public int getIdFreelancer() { return idFreelancer; }
    public void setIdFreelancer(int idFreelancer) { this.idFreelancer = idFreelancer; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public int getPlazoDias() { return plazoDias; }
    public void setPlazoDias(int plazoDias) { this.plazoDias = plazoDias; }
    public String getCarta() { return carta; }
    public void setCarta(String carta) { this.carta = carta; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }
}