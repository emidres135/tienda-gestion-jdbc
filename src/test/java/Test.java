import com.tienda.conexion.ConexionDB;
import com.tienda.dao.PedidoDAO;
import com.tienda.dao.ProductoDAO;
import com.tienda.model.Pedido;
import com.tienda.model.Producto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class Test {

    public static void main(String[] args){
        System.out.println("Prueba 1 Conexion -------------------------");
        try (Connection con = ConexionDB.getConexion()){
            if (con != null && !con.isClosed()){
                System.out.println("Conexion correcta");
            }
        }catch (Exception e){
            System.err.println("Falle conexion "+e.getMessage());
            return;
        }

        ProductoDAO productoDAO = new ProductoDAO();
        PedidoDAO pedidoDAO = new PedidoDAO();

        System.out.println("\n Prueba 2 Agregar producto");
        Producto nuevoProd = new Producto(20, new BigDecimal("79.99"), "Teclado Mecanico");
        boolean insertado = productoDAO.insertar(nuevoProd);
        System.out.println(insertado ? "Producto insertado" : "Error al insaertar");

        System.out.println("\n Prueba 3 Consulta por id");
        Producto prodConsultado = productoDAO.obtenerPorId(1);
        if (prodConsultado != null){
            System.out.println("Producto encontrado "+prodConsultado.getNombre()+
                               " | Precio "+prodConsultado.getPrecio() +
                               " | Stock: "+prodConsultado.getStock());
        }else {
            System.err.println("No se encontro el producto");
            return;
        }

        System.out.println("\n Prueba 4 actualizar stock");
        boolean stockModificado = productoDAO.actualizarStock(1, 50);
        Producto prodActualizado = productoDAO.obtenerPorId(1);
        System.out.println(stockModificado && prodActualizado.getStock() == 50 ? "Stock actualizado a 50" : "Error");

        System.out.println("\n Prueba 5 Reducir stock");
        //Pedimos 5 unidades
        Pedido pedidoValido = new Pedido(LocalDate.now(), 5, 1);
        boolean pedidoOK = pedidoDAO.insertarPedido(pedidoValido);
        Producto prodTrasPedido = productoDAO.obtenerPorId(1);
        if (pedidoOK && prodTrasPedido.getStock() == 45){
            System.out.println("Pedido reducido "+prodTrasPedido.getStock());
        }else{
            System.err.println("Error en la reduccion");
        }

        System.out.println("\n Prueba 6 Pedido inexistente");
        Pedido pedidoErroneo = new Pedido(LocalDate.now(), 2, 9999);
        boolean debeFallar = pedidoDAO.insertarPedido(pedidoErroneo);
        if (!debeFallar){
            System.out.println("Excepcion foreing key");
        }else{
            System.err.println("Deberia haber fallado");
        }

        System.out.println("\n Prueba 7 Listar pedidos");
        List<Pedido> pedidos = pedidoDAO.listarPedidos();
        for (Pedido p : pedidos) {
            System.out.println("-> Pedido #" + p.getIdPedido()
                    + " | Fecha: " + p.getFechaPedido()
                    + " | Cantidad: " + p.getCantidad()
                    + " | Producto: " + p.getNombreProducto()
                    + " (ID Ref: " + p.getIdProducto() + ")");
        }
        System.out.println("Comporbaciones finalizadas");
    }
}
