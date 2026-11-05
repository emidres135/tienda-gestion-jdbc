package com.tienda.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    private int idPedido;
    private LocalDate fechaPedido;
    private int cantidad;
    private int idProducto;

    //Campo para amacenar nombre con JOIN
    private String nombreProducto;

    public Pedido(LocalDate fechaPedido, int cantidad, int idProducto) {
        this.fechaPedido = fechaPedido;
        this.cantidad = cantidad;
        this.idProducto = idProducto;
    }
}
