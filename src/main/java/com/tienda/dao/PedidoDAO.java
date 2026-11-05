package com.tienda.dao;

import com.tienda.conexion.ConexionDB;
import com.tienda.model.Pedido;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    //Agregar Pedido y reducir stock
    public boolean insertarPedido(Pedido pedido){
        String sqlInsertarPedido = "insert into pedidos (fecha_pedido, cantidad, id_producto) values (?, ?, ?)";
        String sqlUpdateStock = "update productos set stock = stock - ? where id_producto = ? and stock >= ?";

        Connection con = null;
        try{
            con = ConexionDB.getConexion();
            con.setAutoCommit(false);

            //Registro pedido
            try (PreparedStatement psPedido = con.prepareStatement(sqlInsertarPedido)){
                psPedido.setDate(1, Date.valueOf(pedido.getFechaPedido()));
                psPedido.setInt(2, pedido.getCantidad());
                psPedido.setInt(3, pedido.getIdProducto());
                psPedido.executeUpdate();
            }

            //Reducir stock
            try (PreparedStatement psStock = con.prepareStatement(sqlUpdateStock)){
                psStock.setInt(1, pedido.getCantidad());
                psStock.setInt(2, pedido.getIdProducto());
                psStock.setInt(3, pedido.getCantidad());

                int filasAfectadas = psStock.executeUpdate();
                if (filasAfectadas == 0){
                    System.err.println("No hay suficiente stock");
                    con.rollback();
                    return false;
                }
            }
            con.commit();
            return true;
        }catch (SQLException e){
            //Manejo de excepciones
            if (e.getErrorCode() == 1452){
                System.err.println("Aviso en el producto con su ID "+ pedido.getIdProducto() + "no existe");
            }else{
                System.err.println("Error en el proceso del pedido "+e.getMessage());
            }

            if (con != null){
                try {
                    con.rollback();
                }catch (SQLException ex){
                    ex.printStackTrace();
                }
            }
            return false;
        }finally {
            if (con != null){
                try {
                    con.setAutoCommit(true);
                    con.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    //Listado de los productos usando JOIN
    public List<Pedido> listarPedidos(){
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT p.id_pedidos, p.fecha_pedido, p.cantidad, p.id_producto, pr.nombre " +
                "FROM pedidos p " +
                "INNER JOIN productos pr ON p.id_producto = pr.id_producto " +
                "ORDER BY p.id_pedidos ASC";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){

            while (rs.next()){
                Pedido pedido = new Pedido(
                        rs.getInt("id_pedidos"),
                        rs.getDate("fecha_pedido").toLocalDate(),
                        rs.getInt("cantidad"),
                        rs.getInt("id_producto"),
                        rs.getString("nombre")
                );
                lista.add(pedido);
            }
        }catch (SQLException e){
            System.err.println("Error al listar pedidos "+e.getMessage());
        }
        return lista;
    }

}
