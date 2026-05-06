package com.pixelsoftware.connectwork.services;

import com.pixelsoftware.connectwork.dao.ClienteDAO;
import com.pixelsoftware.connectwork.dao.FreelancerDAO;
import com.pixelsoftware.connectwork.dao.UsuarioDAO;
import com.pixelsoftware.connectwork.exceptions.AppException;
import com.pixelsoftware.connectwork.models.Usuario;
import com.pixelsoftware.connectwork.utils.JwtUtil;
import com.pixelsoftware.connectwork.utils.PasswordUtil;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AuthService {

    private final UsuarioDAO usuarioDAO     = new UsuarioDAO();
    private final ClienteDAO clienteDAO     = new ClienteDAO();
    private final FreelancerDAO freelancerDAO = new FreelancerDAO();

    public Map<String, Object> login(String username, String password) throws AppException {
        Usuario u = usuarioDAO.findByUsername(username);
        if (u == null) throw new AppException(401, "Usuario o contraseña incorrectos");
        if (!u.isActivo()) throw new AppException(403, "Tu cuenta está desactivada");
        if (!PasswordUtil.checkPassword(password, u.getPassword()))
            throw new AppException(401, "Usuario o contraseña incorrectos");

        String token = JwtUtil.generateToken(u.getId(), u.getUsername(), u.getRol());

        Map<String, Object> resp = new HashMap<>();
        resp.put("token", token);
        resp.put("role", u.getRol());
        resp.put("profileComplete", u.isPerfilCompleto());
        return resp;
    }

    public Map<String, Object> register(String nombre, String username, String password,
                                        String email, String telefono, String direccion,
                                        String cui, String birthDate, String rol) throws AppException {
        if (usuarioDAO.findByUsername(username) != null)
            throw new AppException(400, "El username ya está en uso");

        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setUsername(username);
        u.setPassword(PasswordUtil.hashPassword(password));
        u.setEmail(email);
        u.setTelefono(telefono);
        u.setDireccion(direccion);
        u.setCui(cui);
        u.setFechaNac(LocalDate.parse(birthDate));
        u.setRol(rol);
        u.setActivo(true);
        u.setPerfilCompleto(false);

        int id = usuarioDAO.insertar(u);
        if (id == -1) throw new AppException(500, "Error al registrar usuario");

        if ("CLIENTE".equals(rol)) {
            clienteDAO.insertar(id);
        } else if ("FREELANCER".equals(rol)) {
            freelancerDAO.insertar(id);
        }

        String token = JwtUtil.generateToken(id, username, rol);
        Map<String, Object> resp = new HashMap<>();
        resp.put("token", token);
        resp.put("role", rol);
        resp.put("profileComplete", false);
        return resp;
    }
}