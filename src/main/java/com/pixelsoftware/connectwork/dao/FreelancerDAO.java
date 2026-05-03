package com.pixelsoftware.connectwork.dao;

import com.pixelsoftware.connectwork.config.DatabaseConnection;
import com.pixelsoftware.connectwork.models.Freelancer;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FreelancerDAO {

    public void insertar(int idUsuario) {
        String sql = "INSERT INTO freelancers (id_usuario) VALUES (?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error en insertar freelancer: " + e);
        }
    }

    public Freelancer findById(int idUsuario) {
        String sql = "SELECT * FROM freelancers WHERE id_usuario = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.out.println("Error en findById freelancer: " + e);
        }
        return null;
    }

    public boolean completarPerfil(int idUsuario, String biografia, String experiencia, BigDecimal tarifaHora) {
        String sql = "UPDATE freelancers SET biografia = ?, experiencia = ?, tarifa_hora = ? WHERE id_usuario = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, biografia);
            ps.setString(2, experiencia);
            ps.setBigDecimal(3, tarifaHora);
            ps.setInt(4, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en completarPerfil freelancer: " + e);
        }
        return false;
    }

    public boolean actualizarSaldo(int idUsuario, BigDecimal nuevoSaldo) {
        String sql = "UPDATE freelancers SET saldo = ? WHERE id_usuario = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBigDecimal(1, nuevoSaldo);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en actualizarSaldo freelancer: " + e);
        }
        return false;
    }

    public boolean actualizarCalificacion(int idUsuario, BigDecimal promedio, int total) {
        String sql = "UPDATE freelancers SET calificacion_promedio = ?, total_calificaciones = ? WHERE id_usuario = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBigDecimal(1, promedio);
            ps.setInt(2, total);
            ps.setInt(3, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en actualizarCalificacion: " + e);
        }
        return false;
    }

    public void agregarHabilidad(int idFreelancer, int idHabilidad) {
        String sql = "INSERT IGNORE INTO freelancer_habilidades (id_freelancer, id_habilidad) VALUES (?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFreelancer);
            ps.setInt(2, idHabilidad);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error en agregarHabilidad: " + e);
        }
    }

    public List<Integer> getHabilidades(int idFreelancer) {
        List<Integer> lista = new ArrayList<>();
        String sql = "SELECT id_habilidad FROM freelancer_habilidades WHERE id_freelancer = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFreelancer);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(rs.getInt("id_habilidad"));
        } catch (SQLException e) {
            System.out.println("Error en getHabilidades: " + e);
        }
        return lista;
    }

    private Freelancer mapear(ResultSet rs) throws SQLException {
        Freelancer f = new Freelancer();
        f.setIdUsuario(rs.getInt("id_usuario"));
        f.setBiografia(rs.getString("biografia"));
        f.setExperiencia(rs.getString("experiencia"));
        f.setTarifaHora(rs.getBigDecimal("tarifa_hora"));
        f.setCalificacionPromedio(rs.getBigDecimal("calificacion_promedio"));
        f.setTotalCalificaciones(rs.getInt("total_calificaciones"));
        f.setSaldo(rs.getBigDecimal("saldo"));
        return f;
    }
}