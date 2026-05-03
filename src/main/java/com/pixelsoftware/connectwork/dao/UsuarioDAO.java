package com.pixelsoftware.connectwork.dao;

import com.pixelsoftware.connectwork.config.DatabaseConnection;
import com.pixelsoftware.connectwork.models.Usuario;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class UsuarioDAO {

    public Usuario findByUsername(String username) {
        String sql = "SELECT * FROM usuarios WHERE username = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.out.println("Error en findByUsername: " + e);
        }
        return null;
    }

    public Usuario findById(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.out.println("Error en findById: " + e);
        }
        return null;
    }

    public int insertar(Usuario u) {
        String sql = "INSERT INTO usuarios (nombre, username, password, email, telefono, direccion, cui, fecha_nac, rol, activo, perfil_completo) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getUsername());
            ps.setString(3, u.getPassword());
            ps.setString(4, u.getEmail());
            ps.setString(5, u.getTelefono());
            ps.setString(6, u.getDireccion());
            ps.setString(7, u.getCui());
            ps.setDate(8, Date.valueOf(u.getFechaNac()));
            ps.setString(9, u.getRol());
            ps.setBoolean(10, u.isActivo());
            ps.setBoolean(11, u.isPerfilCompleto());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error en insertar usuario: " + e);
        }
        return -1;
    }

    public boolean actualizarEstado(int id, boolean activo) {
        String sql = "UPDATE usuarios SET activo = ? WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, activo);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en actualizarEstado: " + e);
        }
        return false;
    }

    public boolean actualizarPerfilCompleto(int id) {
        String sql = "UPDATE usuarios SET perfil_completo = TRUE WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en actualizarPerfilCompleto: " + e);
        }
        return false;
    }

    public java.util.List<Usuario> findByRol(String rol) {
        java.util.List<Usuario> lista = new java.util.ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE rol = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, rol);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("Error en findByRol: " + e);
        }
        return lista;
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNombre(rs.getString("nombre"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setEmail(rs.getString("email"));
        u.setTelefono(rs.getString("telefono"));
        u.setDireccion(rs.getString("direccion"));
        u.setCui(rs.getString("cui"));
        u.setFechaNac(rs.getDate("fecha_nac").toLocalDate());
        u.setRol(rs.getString("rol"));
        u.setActivo(rs.getBoolean("activo"));
        u.setPerfilCompleto(rs.getBoolean("perfil_completo"));
        u.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
        return u;
    }
}