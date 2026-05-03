package com.pixelsoftware.connectwork.dao;

import com.pixelsoftware.connectwork.config.DatabaseConnection;
import com.pixelsoftware.connectwork.models.Proyecto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProyectoDAO {

    public int insertar(Proyecto p) {
        String sql = "INSERT INTO proyectos (id_cliente, id_categoria, titulo, descripcion, presupuesto_max, fecha_limite) VALUES (?,?,?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getIdCliente());
            ps.setInt(2, p.getIdCategoria());
            ps.setString(3, p.getTitulo());
            ps.setString(4, p.getDescripcion());
            ps.setBigDecimal(5, p.getPresupuestoMax());
            ps.setDate(6, Date.valueOf(p.getFechaLimite()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error en insertar proyecto: " + e);
        }
        return -1;
    }

    public Proyecto findById(int id) {
        String sql = "SELECT * FROM proyectos WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.out.println("Error en findById proyecto: " + e);
        }
        return null;
    }

    public List<Proyecto> findByCliente(int idCliente) {
        List<Proyecto> lista = new ArrayList<>();
        String sql = "SELECT * FROM proyectos WHERE id_cliente = ? ORDER BY fecha_publicacion DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("Error en findByCliente: " + e);
        }
        return lista;
    }

    public List<Proyecto> findAbiertos() {
        List<Proyecto> lista = new ArrayList<>();
        String sql = "SELECT * FROM proyectos WHERE estado = 'ABIERTO' ORDER BY fecha_publicacion DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("Error en findAbiertos: " + e);
        }
        return lista;
    }

    public boolean actualizarEstado(int id, String estado) {
        String sql = "UPDATE proyectos SET estado = ? WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en actualizarEstado proyecto: " + e);
        }
        return false;
    }

    public boolean actualizar(Proyecto p) {
        String sql = "UPDATE proyectos SET titulo = ?, descripcion = ?, id_categoria = ?, presupuesto_max = ?, fecha_limite = ? WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getTitulo());
            ps.setString(2, p.getDescripcion());
            ps.setInt(3, p.getIdCategoria());
            ps.setBigDecimal(4, p.getPresupuestoMax());
            ps.setDate(5, Date.valueOf(p.getFechaLimite()));
            ps.setInt(6, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en actualizar proyecto: " + e);
        }
        return false;
    }

    public void agregarHabilidad(int idProyecto, int idHabilidad) {
        String sql = "INSERT IGNORE INTO proyecto_habilidades (id_proyecto, id_habilidad) VALUES (?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProyecto);
            ps.setInt(2, idHabilidad);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error en agregarHabilidad proyecto: " + e);
        }
    }

    public List<Integer> getHabilidades(int idProyecto) {
        List<Integer> lista = new ArrayList<>();
        String sql = "SELECT id_habilidad FROM proyecto_habilidades WHERE id_proyecto = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProyecto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(rs.getInt("id_habilidad"));
        } catch (SQLException e) {
            System.out.println("Error en getHabilidades proyecto: " + e);
        }
        return lista;
    }

    private Proyecto mapear(ResultSet rs) throws SQLException {
        Proyecto p = new Proyecto();
        p.setId(rs.getInt("id"));
        p.setIdCliente(rs.getInt("id_cliente"));
        p.setIdCategoria(rs.getInt("id_categoria"));
        p.setTitulo(rs.getString("titulo"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPresupuestoMax(rs.getBigDecimal("presupuesto_max"));
        p.setFechaLimite(rs.getDate("fecha_limite").toLocalDate());
        p.setEstado(rs.getString("estado"));
        p.setFechaPublicacion(rs.getTimestamp("fecha_publicacion").toLocalDateTime());
        return p;
    }
}