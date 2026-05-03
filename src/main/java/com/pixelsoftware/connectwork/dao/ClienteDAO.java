package com.pixelsoftware.connectwork.dao;

import com.pixelsoftware.connectwork.config.DatabaseConnection;
import com.pixelsoftware.connectwork.models.Cliente;

import java.sql.*;
import java.math.BigDecimal;

public class ClienteDAO {

    public void insertar(int idUsuario) {
        String sql = "INSERT INTO clientes (id_usuario) VALUES (?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error en insertar cliente: " + e);
        }
    }

    public Cliente findById(int idUsuario) {
        String sql = "SELECT * FROM clientes WHERE id_usuario = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.out.println("Error en findById cliente: " + e);
        }
        return null;
    }

    public boolean completarPerfil(int idUsuario, String descripcion, String sector, String sitioWeb) {
        String sql = "UPDATE clientes SET descripcion = ?, sector = ?, sitio_web = ? WHERE id_usuario = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, descripcion);
            ps.setString(2, sector);
            ps.setString(3, sitioWeb);
            ps.setInt(4, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en completarPerfil cliente: " + e);
        }
        return false;
    }

    public boolean actualizarSaldo(int idUsuario, BigDecimal nuevoSaldo) {
        String sql = "UPDATE clientes SET saldo = ? WHERE id_usuario = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBigDecimal(1, nuevoSaldo);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en actualizarSaldo cliente: " + e);
        }
        return false;
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setIdUsuario(rs.getInt("id_usuario"));
        c.setDescripcion(rs.getString("descripcion"));
        c.setSector(rs.getString("sector"));
        c.setSitioWeb(rs.getString("sitio_web"));
        c.setSaldo(rs.getBigDecimal("saldo"));
        return c;
    }
}