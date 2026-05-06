package com.pixelsoftware.connectwork.services;

import com.pixelsoftware.connectwork.dao.*;
import com.pixelsoftware.connectwork.exceptions.AppException;
import com.pixelsoftware.connectwork.models.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CalificacionService {

    private final CalificacionDAO calificacionDAO = new CalificacionDAO();
    private final ContratoDAO     contratoDAO     = new ContratoDAO();
    private final PropuestaDAO    propuestaDAO    = new PropuestaDAO();
    private final ProyectoDAO     proyectoDAO     = new ProyectoDAO();
    private final FreelancerDAO   freelancerDAO   = new FreelancerDAO();

    public void calificar(int idContrato, int idCliente, int estrellas, String comentario) throws AppException {
        if (estrellas < 1 || estrellas > 5)
            throw new AppException(400, "La calificación debe ser entre 1 y 5 estrellas");

        Contrato contrato = contratoDAO.findById(idContrato);
        if (contrato == null) throw new AppException(404, "Contrato no encontrado");

        Propuesta propuesta = propuestaDAO.findById(contrato.getIdPropuesta());
        Proyecto proyecto = proyectoDAO.findById(propuesta.getIdProyecto());

        if (proyecto.getIdCliente() != idCliente)
            throw new AppException(403, "No tienes permiso para calificar este contrato");

        if (calificacionDAO.findByContrato(idContrato) != null)
            throw new AppException(400, "Este contrato ya fue calificado");

        Calificacion c = new Calificacion();
        c.setIdContrato(idContrato);
        c.setIdFreelancer(propuesta.getIdFreelancer());
        c.setIdCliente(idCliente);
        c.setEstrellas(estrellas);
        c.setComentario(comentario);
        calificacionDAO.insertar(c);

        // Actualizar promedio del freelancer
        Freelancer f = freelancerDAO.findById(propuesta.getIdFreelancer());
        int total = f.getTotalCalificaciones() + 1;
        BigDecimal promedio = f.getCalificacionPromedio()
                .multiply(BigDecimal.valueOf(f.getTotalCalificaciones()))
                .add(BigDecimal.valueOf(estrellas))
                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
        freelancerDAO.actualizarCalificacion(propuesta.getIdFreelancer(), promedio, total);
    }
}