package com.pixelsoftware.connectwork.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Contrato {
    private int id;
    private int idPropuesta;
    private int idComision;
    private BigDecimal montoContrato;
    private BigDecimal montoBloqueado;
    private String motivoCancel;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdPropuesta() { return idPropuesta; }
    public void setIdPropuesta(int idPropuesta) { this.idPropuesta = idPropuesta; }
    public int getIdComision() { return idComision; }
    public void setIdComision(int idComision) { this.idComision = idComision; }
    public BigDecimal getMontoContrato() { return montoContrato; }
    public void setMontoContrato(BigDecimal montoContrato) { this.montoContrato = montoContrato; }
    public BigDecimal getMontoBloqueado() { return montoBloqueado; }
    public void setMontoBloqueado(BigDecimal montoBloqueado) { this.montoBloqueado = montoBloqueado; }
    public String getMotivoCancel() { return motivoCancel; }
    public void setMotivoCancel(String motivoCancel) { this.motivoCancel = motivoCancel; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }
}