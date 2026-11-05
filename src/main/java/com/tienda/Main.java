package com.tienda;

import com.tienda.dao.PedidoDAO;
import com.tienda.dao.ProductoDAO;
import com.tienda.model.Pedido;
import com.tienda.model.Producto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ProductoDAO productoDAO = new ProductoDAO();
    private static final PedidoDAO pedidoDAO = new PedidoDAO();

    public static void main(String[] args) {
        int opcion = 0;
        do {
            mostrarMenu();
            try {
                System.out.print("Seleccione una opción: ");
                opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1 -> insertarProducto();
                    case 2 -> consultarProductoPorId();
                    case 3 -> actualizarStockProducto();
                    case 4 -> insertarPedido();
                    case 5 -> listarPedidos();
                    case 6 -> System.out.println("\nSaliendo de la aplicación...");
                    default -> System.out.println("Opción no válida. Introduzca un número del 1 al 6.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, introduzca un valor numérico válido.");
            }
            System.out.println();
        } while (opcion != 6);
    }
    private static void mostrarMenu() {
        System.out.println("GESTIÓN DE INVENTARIO Y PEDIDOS");
        System.out.println(" ");
        System.out.println("1. Insertar producto");
        System.out.println("2. Consultar producto por ID");
        System.out.println("3. Actualizar stock de producto");
        System.out.println("4. Insertar pedido");
        System.out.println("5. Listado de pedidos");
        System.out.println("6. Salir");
    }
    // 1. Insertar producto
    private static void insertarProducto() {
        System.out.println("\n INSERTAR PRODUCTO ---");
        System.out.print("Nombre del producto: ");
        String nombre = scanner.nextLine();

        System.out.print("Precio (ej. 19.99): ");
        BigDecimal precio = new BigDecimal(scanner.nextLine());

        System.out.print("Stock inicial: ");
        int stock = Integer.parseInt(scanner.nextLine());

        Producto producto = new Producto(stock, precio, nombre);
        if (productoDAO.insertar(producto)) {
            System.out.println("Producto insertado correctamente.");
        } else {
            System.out.println("No se pudo insertar el producto.");
        }
    }

    // 2. Consultar producto por ID
    private static void consultarProductoPorId() {
        System.out.println("\n CONSULTAR PRODUCTO POR ID");
        System.out.print("Introduzca el ID del producto:");
        int id = Integer.parseInt(scanner.nextLine());

        Producto producto = productoDAO.obtenerPorId(id);
        if (producto != null) {
            System.out.println("Producto encontrado:");
            System.out.println("ID: "+ producto.getIdProducto());
            System.out.println("Nombre: "+ producto.getNombre());
            System.out.println("Precio: "+ producto.getPrecio()+ " €");
            System.out.println("Stock actual: "+ producto.getStock());
        } else {
            System.out.println("No existe ningún producto con el ID "+ id);
        }
    }
    // 3. Actualizar stock de producto
    private static void actualizarStockProducto() {
        System.out.println("\n ACTUALIZAR STOCK");
        System.out.print("Introduzca el ID del producto:");
        int id = Integer.parseInt(scanner.nextLine());

        Producto producto = productoDAO.obtenerPorId(id);
        if (producto == null) {
            System.out.println("El producto con ID "+ id+ " no existe.");
            return;
        }

        System.out.println("Stock actual: "+ producto.getStock());
        System.out.print("Nuevo stock:");
        int nuevoStock = Integer.parseInt(scanner.nextLine());

        if (productoDAO.actualizarStock(id, nuevoStock)) {
            System.out.println("Stock actualizado con éxito.");
        } else {
            System.out.println("Error al actualizar el stock.");
        }
    }

    // 4. Insertar pedido
    private static void insertarPedido() {
        System.out.println("\n INSERTAR PEDIDO");
        System.out.print("Introduzca el ID del producto a pedir:");
        int idProducto = Integer.parseInt(scanner.nextLine());

        System.out.print("Cantidad de unidades:");
        int cantidad = Integer.parseInt(scanner.nextLine());

        // La fecha del pedido se asigna automáticamente al día actual
        Pedido pedido = new Pedido(LocalDate.now(), cantidad, idProducto);

        if (pedidoDAO.insertarPedido(pedido)) {
            System.out.println("Pedido registrado correctamente y stock descontado.");
        } else {
            System.out.println("No se pudo completar el pedido.");
        }
    }

    // 5. Listado de pedidos (JOIN)
    private static void listarPedidos() {
        System.out.println("\n LISTADO DE PEDIDOS ");
        List<Pedido> pedidos = pedidoDAO.listarPedidos();

        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados en el sistema.");
            return;
        }

        System.out.printf("%-10s %-12s %-10s %-12s %-20s%n", "ID PEDIDO", "FECHA", "CANTIDAD", "ID PRODUCTO", "NOMBRE PRODUCTO");
        System.out.println("----------------------------------------------------------------------");
        for (Pedido p : pedidos) {
            System.out.printf("%-10d %-12s %-10d %-12d %-20s%n",
                    p.getIdPedido(),
                    p.getFechaPedido(),
                    p.getCantidad(),
                    p.getIdProducto(),
                    p.getNombreProducto());
        }
    }

}
