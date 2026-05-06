package com.pixelsoftware.connectwork.services;

import com.pixelsoftware.connectwork.dao.*;
import com.pixelsoftware.connectwork.exceptions.AppException;
import com.pixelsoftware.connectwork.models.*;

import java.math.BigDecimal;
import java.util.List;

public class PropuestaService {

    private final PropuestaDAO  propuestaDAO  = new PropuestaDAO();
    private final ProyectoDAO   proyectoDAO   = new ProyectoDAO();
    private final ContratoDAO   contratoDAO   = new ContratoDAO();
    private final ClienteDAO    clienteDAO    = new ClienteDAO();
    private final ComisionDAO   comisionDAO   = new ComisionDAO();

    public Propuesta enviar(int idFreelancer, int idProyecto, BigDecimal monto,
                            int plazoDias, String carta) throws AppException {
        Proyecto proyecto = proyectoDAO.findById(idProyecto);
        if (proyecto == null) throw new AppException(404, "Proyecto no encontrado");
        if (!"ABIERTO".equals(proyecto.getEstado()))
            throw new AppException(400, "El proyecto no está abierto");
        if (propuestaDAO.existePropuesta(idProyecto, idFreelancer))
            throw new AppException(400, "Ya enviaste una propuesta a este proyecto");
        if (monto.compareTo(proyecto.getPresupuestoMax()) > 0)
            throw new AppException(400, "El monto no puede superar el presupuesto máximo");

        Propuesta p = new Propuesta();
        p.setIdProyecto(idProyecto);
        p.setIdFreelancer(idFreelancer);
        p.setMonto(monto);
        p.setPlazoDias(plazoDias);
        p.setCarta(carta);

        int id = propuestaDAO.insertar(p);
        if (id == -1) throw new AppException(500, "Error al enviar propuesta");
        return propuestaDAO.findById(id);
    }

    public List<Propuesta> listarPorProyecto(int idProyecto) {
        return propuestaDAO.findByProyecto(idProyecto);
    }

    public List<Propuesta> listarPorFreelancer(int idFreelancer) {
        return propuestaDAO.findByFreelancer(idFreelancer);
    }

    public Contrato aceptar(int idPropuesta, int idCliente) throws AppException {
        Propuesta propuesta = propuestaDAO.findById(idPropuesta);
        if (propuesta == null) throw new AppException(404, "Propuesta no encontrada");

        Proyecto proyecto = proyectoDAO.findById(propuesta.getIdProyecto());
        if (proyecto.getIdCliente() != idCliente)
            throw new AppException(403, "No tienes permiso para aceptar esta propuesta");

        Cliente cliente = clienteDAO.findById(idCliente);
        if (cliente.getSaldo().compareTo(propuesta.getMonto()) < 0)
            throw new AppException(400, "Saldo insuficiente para aceptar la propuesta");

        Comision comision = comisionDAO.findVigente();

        // Bloquear saldo
        clienteDAO.actualizarSaldo(idCliente, cliente.getSaldo().subtract(propuesta.getMonto()));

        // Crear contrato
        Contrato contrato = new Contrato();
        contrato.setIdPropuesta(idPropuesta);
        contrato.setIdComision(comision.getId());
        contrato.setMontoContrato(propuesta.getMonto());
        contrato.setMontoBloqueado(propuesta.getMonto());
        int idContrato = contratoDAO.insertar(contrato);
        if (idContrato == -1) throw new AppException(500, "Error al crear contrato");

        // Actualizar estados
        propuestaDAO.actualizarEstado(idPropuesta, "ACEPTADA");
        proyectoDAO.actualizarEstado(propuesta.getIdProyecto(), "EN_PROGRESO");

        return contratoDAO.findById(idContrato);
    }

    public void rechazar(int idPropuesta) throws AppException {
        Propuesta p = propuestaDAO.findById(idPropuesta);
        if (p == null) throw new AppException(404, "Propuesta no encontrada");
        propuestaDAO.actualizarEstado(idPropuesta, "RECHAZADA");
    }

    public void retirar(int idPropuesta, int idFreelancer) throws AppException {
        Propuesta p = propuestaDAO.findById(idPropuesta);
        if (p == null) throw new AppException(404, "Propuesta no encontrada");
        if (p.getIdFreelancer() != idFreelancer)
            throw new AppException(403, "No tienes permiso para retirar esta propuesta");
        if (!"PENDIENTE".equals(p.getEstado()))
            throw new AppException(400, "Solo puedes retirar propuestas pendientes");
        propuestaDAO.actualizarEstado(idPropuesta, "RETIRADA");
    }
}