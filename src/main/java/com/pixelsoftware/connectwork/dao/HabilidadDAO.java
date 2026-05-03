package com.pixelsoftware.connectwork.dao;

import com.pixelsoftware.connectwork.config.DatabaseConnection;
import com.pixelsoftware.connectwork.models.Habilidad;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HabilidadDAO {

    public List<Habilidad> findByCategoria(int idCategoria) {
        List<Habilidad> lista = new ArrayList<>();
        String sql = "SELECT * FROM habilidades WHERE id_categoria = ? AND activa = TRUE";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("Error en findByCategoria: " + e);
        }
        return lista;
    }

    public List<Habilidad> findAll() {
        List<Habilidad> lista = new ArrayList<>();
        String sql = "SELECT * FROM habilidades WHERE activa = TRUE";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("Error en findAll habilidades: " + e);
        }
        return lista;
    }

    public Habilidad findById(int id) {
        String sql = "SELECT * FROM habilidades WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.out.println("Error en findById habilidad: " + e);
        }
        return null;
    }

    public int insertar(int idCategoria, String nombre) {
        String sql = "INSERT INTO habilidades (id_categoria, nombre) VALUES (?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idCategoria);
            ps.setString(2, nombre);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error en insertar habilidad: " + e);
        }
        return -1;
    }

    public boolean cambiarEstado(int id, boolean activa) {
        String sql = "UPDATE habilidades SET activa = ? WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, activa);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en cambiarEstado habilidad: " + e);
        }
        return false;
    }

    private Habilidad mapear(ResultSet rs) throws SQLException {
        Habilidad h = new Habilidad();
        h.setId(rs.getInt("id"));
        h.setIdCategoria(rs.getInt("id_categoria"));
        h.setNombre(rs.getString("nombre"));
        h.setActiva(rs.getBoolean("activa"));
        return h;
    }
}