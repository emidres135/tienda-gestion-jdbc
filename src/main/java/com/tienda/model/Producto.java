package com.tienda.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    private int idProducto;
    private String nombre;
    private BigDecimal precio;
    private int stock;

    //Constructor sin id

    public Producto(int stock, BigDecimal precio, String nombre) {
        this.stock = stock;
        this.precio = precio;
        this.nombre = nombre;
    }
}
