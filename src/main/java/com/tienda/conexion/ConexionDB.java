package com.tienda.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    //Configuracion XAMPP
    private static final String URL = "jdbc:mysql://localhost:3306/tiendaDB";
    private static final String USER = "root";
    private static final String PASSWORD = "";


    /**
     * Encargado de establecer la conexion con la base de datos
     * @return Conexion JDBC
     * @throws SQLException
     */
    public static Connection getConexion() throws SQLException{
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
