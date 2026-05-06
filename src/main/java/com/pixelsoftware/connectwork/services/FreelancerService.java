package com.pixelsoftware.connectwork.services;

import com.pixelsoftware.connectwork.dao.FreelancerDAO;
import com.pixelsoftware.connectwork.dao.HabilidadDAO;
import com.pixelsoftware.connectwork.dao.UsuarioDAO;
import com.pixelsoftware.connectwork.exceptions.AppException;
import com.pixelsoftware.connectwork.models.Freelancer;

import java.math.BigDecimal;
import java.util.List;

public class FreelancerService {

    private final FreelancerDAO freelancerDAO = new FreelancerDAO();
    private final UsuarioDAO    usuarioDAO    = new UsuarioDAO();
    private final HabilidadDAO  habilidadDAO  = new HabilidadDAO();

    public Freelancer getPerfil(int idUsuario) throws AppException {
        Freelancer f = freelancerDAO.findById(idUsuario);
        if (f == null) throw new AppException(404, "Freelancer no encontrado");
        return f;
    }

    public void completarPerfil(int idUsuario, String biografia, String experiencia,
                                BigDecimal tarifaHora, List<Integer> habilidades) throws AppException {
        if (biografia == null || biografia.isBlank())
            throw new AppException(400, "La biografía es requerida");
        if (experiencia == null || experiencia.isBlank())
            throw new AppException(400, "El nivel de experiencia es requerido");
        if (habilidades == null || habilidades.isEmpty())
            throw new AppException(400, "Debe seleccionar al menos una habilidad");

        freelancerDAO.completarPerfil(idUsuario, biografia, experiencia, tarifaHora);
        for (int idHabilidad : habilidades) {
            freelancerDAO.agregarHabilidad(idUsuario, idHabilidad);
        }
        usuarioDAO.actualizarPerfilCompleto(idUsuario);
    }

    public List<Integer> getHabilidades(int idUsuario) {
        return freelancerDAO.getHabilidades(idUsuario);
    }
}