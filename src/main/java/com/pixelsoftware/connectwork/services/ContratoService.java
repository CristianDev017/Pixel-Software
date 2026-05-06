package com.pixelsoftware.connectwork.services;

import com.pixelsoftware.connectwork.dao.*;
import com.pixelsoftware.connectwork.exceptions.AppException;
import com.pixelsoftware.connectwork.models.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ContratoService {

    private final ContratoDAO   contratoDAO   = new ContratoDAO();
    private final EntregaDAO    entregaDAO    = new EntregaDAO();
    private final ProyectoDAO   proyectoDAO   = new ProyectoDAO();
    private final PropuestaDAO  propuestaDAO  = new PropuestaDAO();
    private final ClienteDAO    clienteDAO    = new ClienteDAO();
    private final FreelancerDAO freelancerDAO = new FreelancerDAO();
    private final ComisionDAO   comisionDAO   = new ComisionDAO();

    public Entrega subirEntrega(int idContrato, int idFreelancer,
                                String descripcion, String archivosUrl) throws AppException {
        Contrato contrato = contratoDAO.findById(idContrato);
        if (contrato == null) throw new AppException(404, "Contrato no encontrado");

        Propuesta propuesta = propuestaDAO.findById(contrato.getIdPropuesta());
        if (propuesta.getIdFreelancer() != idFreelancer)
            throw new AppException(403, "No tienes permiso sobre este contrato");

        Entrega e = new Entrega();
        e.setIdContrato(idContrato);
        e.setDescripcion(descripcion);
        e.setArchivosUrl(archivosUrl);
        int id = entregaDAO.insertar(e);
        if (id == -1) throw new AppException(500, "Error al subir entrega");

        Proyecto proyecto = proyectoDAO.findById(
                propuestaDAO.findById(contrato.getIdPropuesta()).getIdProyecto());
        proyectoDAO.actualizarEstado(proyecto.getId(), "ENTREGA_PENDIENTE");

        return entregaDAO.findByContrato(idContrato).get(0);
    }

    public void aprobarEntrega(int idEntrega, int idContrato, int idCliente) throws AppException {
        Contrato contrato = contratoDAO.findById(idContrato);
        if (contrato == null) throw new AppException(404, "Contrato no encontrado");

        Propuesta propuesta = propuestaDAO.findById(contrato.getIdPropuesta());
        Proyecto proyecto = proyectoDAO.findById(propuesta.getIdProyecto());

        if (proyecto.getIdCliente() != idCliente)
            throw new AppException(403, "No tienes permiso sobre este contrato");

        entregaDAO.aprobar(idEntrega);

        // Calcular pago al freelancer
        Comision comision = comisionDAO.findById(contrato.getIdComision());
        BigDecimal porcentaje = comision.getPorcentaje()
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal montoComision = contrato.getMontoContrato().multiply(porcentaje);
        BigDecimal montoFreelancer = contrato.getMontoContrato().subtract(montoComision);

        // Acreditar al freelancer
        Freelancer freelancer = freelancerDAO.findById(propuesta.getIdFreelancer());
        freelancerDAO.actualizarSaldo(propuesta.getIdFreelancer(),
                freelancer.getSaldo().add(montoFreelancer));

        // Completar contrato y proyecto
        contratoDAO.completar(idContrato);
        proyectoDAO.actualizarEstado(proyecto.getId(), "COMPLETADO");
    }

    public void rechazarEntrega(int idEntrega, int idContrato, int idCliente, String motivo) throws AppException {
        Contrato contrato = contratoDAO.findById(idContrato);
        if (contrato == null) throw new AppException(404, "Contrato no encontrado");

        Propuesta propuesta = propuestaDAO.findById(contrato.getIdPropuesta());
        Proyecto proyecto = proyectoDAO.findById(propuesta.getIdProyecto());

        if (proyecto.getIdCliente() != idCliente)
            throw new AppException(403, "No tienes permiso sobre este contrato");

        entregaDAO.rechazar(idEntrega, motivo);
        proyectoDAO.actualizarEstado(proyecto.getId(), "EN_PROGRESO");
    }

    public void cancelarContrato(int idContrato, int idCliente, String motivo) throws AppException {
        Contrato contrato = contratoDAO.findById(idContrato);
        if (contrato == null) throw new AppException(404, "Contrato no encontrado");

        Propuesta propuesta = propuestaDAO.findById(contrato.getIdPropuesta());
        Proyecto proyecto = proyectoDAO.findById(propuesta.getIdProyecto());

        if (proyecto.getIdCliente() != idCliente)
            throw new AppException(403, "No tienes permiso para cancelar este contrato");

        // Devolver saldo al cliente
        Cliente cliente = clienteDAO.findById(idCliente);
        clienteDAO.actualizarSaldo(idCliente,
                cliente.getSaldo().add(contrato.getMontoBloqueado()));

        contratoDAO.cancelar(idContrato, motivo);
        proyectoDAO.actualizarEstado(proyecto.getId(), "CANCELADO");
    }
}