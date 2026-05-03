package com.pixelsoftware.connectwork.dao;

import com.pixelsoftware.connectwork.config.DatabaseConnection;
import com.pixelsoftware.connectwork.models.Propuesta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropuestaDAO {

    public int insertar(Propuesta p) {
        String sql = "INSERT INTO propuestas (id_proyecto, id_freelancer, monto, plazo_dias, carta) VALUES (?,?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getIdProyecto());
            ps.setInt(2, p.getIdFreelancer());
            ps.setBigDecimal(3, p.getMonto());
            ps.setInt(4, p.getPlazoDias());
            ps.setString(5, p.getCarta());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error en insertar propuesta: " + e);
        }
        return -1;
    }

    public Propuesta findById(int id) {
        String sql = "SELECT * FROM propuestas WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.out.println("Error en findById propuesta: " + e);
        }
        return null;
    }

    public List<Propuesta> findByProyecto(int idProyecto) {
        List<Propuesta> lista = new ArrayList<>();
        String sql = "SELECT * FROM propuestas WHERE id_proyecto = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProyecto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("Error en findByProyecto: " + e);
        }
        return lista;
    }

    public List<Propuesta> findByFreelancer(int idFreelancer) {
        List<Propuesta> lista = new ArrayList<>();
        String sql = "SELECT * FROM propuestas WHERE id_freelancer = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFreelancer);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("Error en findByFreelancer: " + e);
        }
        return lista;
    }

    public boolean actualizarEstado(int id, String estado) {
        String sql = "UPDATE propuestas SET estado = ? WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en actualizarEstado propuesta: " + e);
        }
        return false;
    }

    public boolean existePropuesta(int idProyecto, int idFreelancer) {
        String sql = "SELECT id FROM propuestas WHERE id_proyecto = ? AND id_freelancer = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProyecto);
            ps.setInt(2, idFreelancer);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.out.println("Error en existePropuesta: " + e);
        }
        return false;
    }

    private Propuesta mapear(ResultSet rs) throws SQLException {
        Propuesta p = new Propuesta();
        p.setId(rs.getInt("id"));
        p.setIdProyecto(rs.getInt("id_proyecto"));
        p.setIdFreelancer(rs.getInt("id_freelancer"));
        p.setMonto(rs.getBigDecimal("monto"));
        p.setPlazoDias(rs.getInt("plazo_dias"));
        p.setCarta(rs.getString("carta"));
        p.setEstado(rs.getString("estado"));
        p.setFechaEnvio(rs.getTimestamp("fecha_envio").toLocalDateTime());
        return p;
    }
}