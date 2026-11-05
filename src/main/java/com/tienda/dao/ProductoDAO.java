package com.tienda.dao;

import com.tienda.conexion.ConexionDB;
import com.tienda.model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductoDAO {

    //Agregar Producto

    public boolean insertar(Producto producto) {
        String sql = "insert into productos (nombre, precio, stock) values (?, ?, ?)";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setBigDecimal(2, producto.getPrecio());
            ps.setInt(3, producto.getStock());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }

    //Consulta por ID
    public Producto obtenerPorId(int idProducto) {
        String sql = "select id_producto, nombre, precio, stock from productos where id_producto = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                            rs.getInt("id_producto"),
                            rs.getString("nombre"),
                            rs.getBigDecimal("precio"),
                            rs.getInt("stock")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar producto: " + e.getMessage());
        }
        return null;
    }

    //Actualizar Stock
    public boolean actualizarStock(int idProducto, int nuevoStock) {
        String sql = "update productos set stock = ? where id_producto = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, nuevoStock);
            ps.setInt(2, idProducto);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar " + e.getMessage());
            return false;
        }
    }

}

