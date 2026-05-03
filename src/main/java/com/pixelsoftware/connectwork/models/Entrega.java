package com.pixelsoftware.connectwork.models;

import java.time.LocalDateTime;

public class Entrega {
    private int id;
    private int idContrato;
    private String descripcion;
    private String archivosUrl;
    private String estado;
    private String motivoRechazo;
    private LocalDateTime fechaEntrega;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdContrato() { return idContrato; }
    public void setIdContrato(int idContrato) { this.idContrato = idContrato; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getArchivosUrl() { return archivosUrl; }
    public void setArchivosUrl(String archivosUrl) { this.archivosUrl = archivosUrl; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getMotivoRechazo() { return motivoRechazo; }
    public void setMotivoRechazo(String motivoRechazo) { this.motivoRechazo = motivoRechazo; }
    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDateTime fechaEntrega) { this.fechaEntrega = fechaEntrega; }
}