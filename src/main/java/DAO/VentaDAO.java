/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DataClasses.ElementoLista;
import DataClasses.ProductoVenta;
import DataClasses.Venta;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author OrdSon
 */
public class VentaDAO extends DAO {
    
    InventarioDAO inventarioDAO  = new InventarioDAO();
    private final String insert = "INSERT INTO venta (id_cliente, id_sucursal, id_empleado, descuento, subtotal, total) VALUES (?,?,?,?,?,?) RETURNING id";
    private final String insertLista = "INSERT INTO lista_venta (id_venta, id_producto, cantidad) VALUES (?,?,?);";
    private final String selectAll = "SELECT * FROM venta";
    private final String selectAllFromLista = """
                                              SELECT l.id_venta AS venta, l.id_producto AS id_producto, p.nombre AS nombre, p.marca AS marca, p.precio AS precio,
                                              COUNT(*) AS cantidad, SUM(p.precio) AS subtotal 
                                              FROM lista_venta AS l 
                                              INNER JOIN producto AS p ON l.id_producto = p.id 
                                              WHERE l.id_venta = ? 
                                              GROUP BY l.id_venta, l.id_producto, p.nombre, p.marca,p.precio;""";

    private final String select = "SELECT * FROM cliente WHERE nit = ?";
    private final String alter = "UPDATE cliente SET nombre = ? WHERE nit = ?";
    private final String searchByNit = "SELECT * FROM cliente WHERE nit LIKE ?";
    private final String searchByName = "SELECT * FROM cliente WHERE nombre LIKE ?";
    private final String searchByNitAndName = "SELECT * FROM cliente WHERE nit LIKE ? AND nombre LIKE ?";

    public VentaDAO() {
    }

    public void insert(Venta venta, LinkedList<ProductoVenta> productos) {
        try ( PreparedStatement ps = conexion.prepareStatement(insert)) {
            ps.setString(1, venta.nit());
            ps.setInt(2, venta.idSucursal());
            ps.setInt(3, venta.idEmpleado());
            ps.setDouble(4, venta.descuento());
            ps.setDouble(5, venta.subTotal());
            ps.setDouble(6, venta.total());
            ResultSet rs = ps.executeQuery();
            //SELECCIONA EL ULTIMO ELEMENTO AÑADIDO
            int id = 0;
            while (rs.next()) {
                id = rs.getInt("id");
            }
            insertListaVenta(productos, id);
            JOptionPane.showMessageDialog(null, "Venta añadida con éxito");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    //PENDING
    public boolean validarLista(LinkedList<ProductoVenta> productos){
        LinkedList <ProductoVenta> temp = new LinkedList<>();
        for (ProductoVenta producto : productos) {
            int index = isInList(temp, producto.getProducto().id());
            if (index>=0) {
                temp.get(index).setCantidad(temp.get(index).getCantidad()+1);
            }else{
                temp.add(producto);
            }
        }
        return false;
    }
    private int isInList(LinkedList<ProductoVenta> productos, int id){
        int cuenta = -1;
        for (ProductoVenta producto : productos) {
            cuenta++;
            if (id == producto.getProducto().id()) {
                return cuenta;
            }
        }
        return cuenta;
    }
    public void insertListaVenta(LinkedList<ProductoVenta> productos, int idVenta) throws SQLException {
        PreparedStatement ps = conexion.prepareStatement(insertLista);
        for (ProductoVenta producto : productos) {
            ps.setInt(1, idVenta);
            ps.setInt(2, producto.getProducto().id());
            ps.setInt(3, 1);
            ps.addBatch();
        }
        ps.executeBatch();

    }

    public LinkedList<Venta> selectAll() {
        LinkedList<Venta> ventas = new LinkedList<>();
        try ( Statement st = conexion.createStatement();  ResultSet rs = st.executeQuery(selectAll)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nit = rs.getString("id_cliente");
                int idSucursal = rs.getInt("id_sucursal");
                int idEmpleado = rs.getInt("id_empleado");
                Date fecha = rs.getDate("fecha");
                double descuento = rs.getDouble("descuento");
                double subtotal = rs.getDouble("subtotal");
                double total = rs.getDouble("total");

                Venta nueva = new Venta(id, nit, idSucursal, idEmpleado, fecha, descuento, subtotal, total);
                ventas.add(nueva);
            }
            return ventas;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public LinkedList<ElementoLista> selectAllFromLista(int id) {
        LinkedList<ElementoLista> elementos = new LinkedList<>();
        try ( PreparedStatement ps = conexion.prepareStatement(selectAllFromLista)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                int idVenta = rs.getInt("venta");
                int idProducto = rs.getInt("id_producto");
                String nombre = rs.getString("nombre");
                String marca = rs.getString("marca");
                int cantidad = rs.getInt("cantidad");
                double subtotal = rs.getDouble("subtotal");
                double precio = rs.getDouble("precio");

                ElementoLista nuevo = new ElementoLista(idVenta, idProducto, nombre, marca, cantidad, precio,subtotal);
                elementos.add(nuevo);
            }
            return elementos;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public DefaultTableModel getTableModel(LinkedList<Venta> ventas) {
        Object header[] = new Object[]{"ID", "NIT", "SUCURSAL", "EMPLEADO", "FECHA", "DESCUENTO", "SUBTOTAL", "TOTAL"};
        DefaultTableModel model = new DefaultTableModel(header, 0);
        for (Venta sale : ventas) {
            model.addRow(new Object[]{sale.id(), sale.nit(), sale.idSucursal(), sale.idEmpleado(), sale.fecha(), sale.descuento(), sale.subTotal(), sale.total()});
        }
        return model;
    }

    public DefaultTableModel getTableModelLista(LinkedList<ElementoLista> elementos) {
        Object header[] = new Object[]{"VENTA", "ID PRODUCTO", "PRODUCTO", "MARCA", " CANTIDAD","PRECIO", "SUBTOTAL"};
        DefaultTableModel model = new DefaultTableModel(header, 0);
        for (ElementoLista elemento : elementos) {
            model.addRow(new Object[]{elemento.idVenta(), elemento.idProducto(), elemento.nombre(), elemento.marca(),elemento.cantidad(),elemento.precio(), elemento.subtotal()});
        }
        return model;
    }

    /*
    public Cliente select(String nit) {
        try ( PreparedStatement ps = conexion.prepareStatement(select)) {
            ps.setString(1, nit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String nitResultado = rs.getString("nit");
                String nombre = rs.getString("nombre");
                Cliente nuevo = new Cliente(nitResultado, nombre);
                return nuevo;
            }
            return null;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public void update(Cliente cliente) {
        try ( PreparedStatement ps = conexion.prepareStatement(alter)) {
            ps.setString(1, cliente.nombre());
            ps.setString(2, cliente.nit());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Edicion exitosa para NIT: " + cliente.nit() + "\n Nombre modificado: " + cliente.nombre());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());

        }
    }
     */
}
