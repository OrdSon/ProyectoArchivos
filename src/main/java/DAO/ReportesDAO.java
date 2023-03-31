/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DataClasses.ClienteGanancia;
import DataClasses.ElementoLista;
import DataClasses.EmpleadoGanancia;
import DataClasses.ProductoGanancia;
import DataClasses.ProductoSucursal;
import DataClasses.SucursalGanancia;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 *
 * @author OrdSon
 */
public class ReportesDAO extends DAO {

    private final String productosMasVendidos = """
                                                SELECT l.id_producto AS id_producto, p.nombre AS nombre, p.marca AS marca, 
                                                COUNT(*) AS cantidad,p.precio, SUM(p.precio) AS subtotal 
                                                FROM lista_venta AS l 
                                                INNER JOIN producto AS p ON l.id_producto = p.id 
                                                GROUP BY l.id_producto, p.nombre, p.marca, p.precio ORDER BY cantidad DESC LIMIT 10""";
    private final String clientesConMasGanancia = """
                                               SELECT c.nit AS nit, 
                                                      c.nombre AS nombre, 
                                                      SUM(v.total) AS total 
                                               FROM cliente AS c 
                                               INNER JOIN venta AS v 
                                               ON c.nit = v.id_cliente 
                                               GROUP BY c.nit, c.nombre
                                               ORDER BY total DESC LIMIT 11;""";
    private final String sucursalesConMasVentas = """
                                               SELECT s.id AS id, 
                                                      s.nombre AS nombre, 
                                                      s.direccion AS direccion, 
                                                      COUNT(*) AS ventas, 
                                                      SUM(v.total) AS total 
                                               FROM sucursal AS s 
                                               INNER JOIN venta AS v 
                                               ON s.id = v.id_sucursal 
                                               GROUP BY s.id, s.nombre, s.direccion 
                                               ORDER BY ventas DESC 
                                               LIMIT 3;""";

    private final String sucursalesConMasGanancia = """
                                                    SELECT s.id AS id, 
                                                           s.nombre AS nombre, 
                                                           s.direccion AS direccion, 
                                                           COUNT(*) AS ventas, 
                                                           SUM(v.total) AS total 
                                                    FROM sucursal AS s 
                                                    INNER JOIN venta AS v 
                                                    ON s.id = v.id_sucursal 
                                                    GROUP BY s.id, s.nombre, s.direccion 
                                                    ORDER BY total DESC 
                                                    LIMIT 3;""";
    private final String empleadosConMasVentas = """
                                                 SELECT e.id AS id, 
                                                        e.nombre AS nombre,
                                                        e.sucursal AS sucursal,
                                                        s.nombre AS nombre_sucursal,
                                                        COUNT(*) as cantidad,
                                                        SUM(v.total) AS total
                                                        FROM empleado AS e 
                                                        INNER JOIN sucursal AS s
                                                        ON e.sucursal = s.id
                                                 INNER JOIN venta AS v
                                                 ON v.id_empleado = e.id
                                                 GROUP BY e.id, e.nombre, e.sucursal, s.nombre
                                                 ORDER BY cantidad DESC LIMIT 3;""";
    private final String empleadosConMasGanancias = """
                                                    SELECT e.id AS id, 
                                                           e.nombre AS nombre,
                                                           e.sucursal AS sucursal,
                                                           s.nombre AS nombre_sucursal,
                                                           COUNT(*) AS cantidad,
                                                           SUM(v.total) AS total
                                                           FROM empleado AS e 
                                                           INNER JOIN sucursal AS s
                                                           ON e.sucursal = s.id
                                                    INNER JOIN venta AS v
                                                    ON v.id_empleado = e.id
                                                    GROUP BY e.id, e.nombre, e.sucursal, s.nombre
                                                    ORDER BY total DESC LIMIT 3;""";
    private final String productosMasGanancia = """
                                                SELECT p.id AS id,
                                                       p.nombre AS producto,
                                                       p.marca AS marca,
                                                       COUNT(*) AS cantidad,
                                                       SUM(p.precio) AS total
                                                FROM producto AS p
                                                INNER JOIN lista_venta AS l ON p.id = l.id_producto
                                                GROUP BY p.id, p.nombre, p.marca ORDER BY total DESC LIMIT 10;""";
    
    private final String top5ProductosVentas = """
                                               SELECT id, producto, marca, id_sucursal, nombre_sucursal, cantidad, total
                                               FROM (
                                                 SELECT p.id AS id,
                                                        p.nombre AS producto,
                                                        p.marca AS marca,
                                                        s.id AS id_sucursal,
                                                        s.nombre AS nombre_sucursal,
                                                        COUNT(*) AS cantidad,
                                                        SUM(p.precio) AS total,
                                                        ROW_NUMBER() OVER (PARTITION BY s.id ORDER BY COUNT(*) DESC) AS rn
                                                 FROM producto AS p
                                                 INNER JOIN lista_venta AS l ON p.id = l.id_producto
                                                 INNER JOIN venta AS v ON v.id = l.id_venta 
                                                 INNER JOIN sucursal AS s ON v.id_sucursal = s.id
                                                 GROUP BY p.id, p.nombre, p.marca, s.id, s.nombre 
                                               ) AS subquery
                                               WHERE rn <= 5;""";
    private final String top5ProductosGanancias = """
                                                  SELECT id, producto, marca, id_sucursal, nombre_sucursal, cantidad, total
                                                  FROM (
                                                    SELECT p.id AS id,
                                                           p.nombre AS producto,
                                                           p.marca AS marca,
                                                           s.id AS id_sucursal,
                                                           s.nombre AS nombre_sucursal,
                                                           COUNT(*) AS cantidad,
                                                           SUM(p.precio) AS total,
                                                           ROW_NUMBER() OVER (PARTITION BY s.id ORDER BY SUM(p.precio) DESC) AS rn
                                                    FROM producto AS p
                                                    INNER JOIN lista_venta AS l ON p.id = l.id_producto
                                                    INNER JOIN venta AS v ON v.id = l.id_venta 
                                                    INNER JOIN sucursal AS s ON v.id_sucursal = s.id
                                                    GROUP BY p.id, p.nombre, p.marca, s.id, s.nombre 
                                                  ) AS subquery
                                                  WHERE rn <= 5;""";

    public ReportesDAO() {
    }

    public LinkedList<ElementoLista> productosMasVendidos() {
        LinkedList<ElementoLista> elementos = new LinkedList<>();
        try ( PreparedStatement ps = conexion.prepareStatement(productosMasVendidos)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                int idProducto = rs.getInt("id_producto");
                String nombre = rs.getString("nombre");
                String marca = rs.getString("marca");
                int cantidad = rs.getInt("cantidad");
                double subtotal = rs.getDouble("subtotal");
                double precio = rs.getDouble("precio");

                ElementoLista nuevo = new ElementoLista(0, idProducto, nombre, marca, cantidad, precio, subtotal);
                elementos.add(nuevo);
            }
            return elementos;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public LinkedList<ClienteGanancia> clientesMasGanancia() {
        LinkedList<ClienteGanancia> clientes = new LinkedList<>();
        try ( PreparedStatement ps = conexion.prepareStatement(clientesConMasGanancia)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String nit = rs.getString("nit");
                String nombre = rs.getString("nombre");
                double total = rs.getDouble("total");

                ClienteGanancia nuevo = new ClienteGanancia(nit, nombre, total);
                clientes.add(nuevo);
            }
            return clientes;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public LinkedList<SucursalGanancia> sucursalesConMasVentas(int select) {
        String query = "";
        switch (select) {
            case 1 ->
                query = sucursalesConMasVentas;
            case 2 ->
                query = sucursalesConMasGanancia;
            default -> {
            }
        }
        LinkedList<SucursalGanancia> sucursales = new LinkedList<>();
        try ( PreparedStatement ps = conexion.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String direccion = rs.getString("direccion");
                int ventas = rs.getInt("ventas");
                double total = rs.getDouble("total");
                SucursalGanancia nueva = new SucursalGanancia(id, nombre, direccion, ventas, total);
                sucursales.add(nueva);
            }
            return sucursales;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public LinkedList<EmpleadoGanancia> empleadosMasVentas(int select) {
        String query = "";
        switch (select) {
            case 1 ->
                query = empleadosConMasVentas;
            case 2 ->
                query = empleadosConMasGanancias;
            default -> {
            }
        }
        LinkedList<EmpleadoGanancia> empleados = new LinkedList<>();
        try ( PreparedStatement ps = conexion.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                int idSucursal = rs.getInt("sucursal");
                String nombreSucursal = rs.getString("nombre_sucursal");
                int ventas = rs.getInt("cantidad");
                double total = rs.getDouble("total");
                EmpleadoGanancia nuevo = new EmpleadoGanancia(id, nombre, idSucursal, nombreSucursal, ventas, total);
                empleados.add(nuevo);
            }
            return empleados;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public LinkedList<ProductoGanancia> productosMasGanancia() {
        LinkedList<ProductoGanancia> productos = new LinkedList<>();
        try ( PreparedStatement ps = conexion.prepareStatement(productosMasGanancia)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("producto");
                String marca = rs.getString("marca");
                int ventas = rs.getInt("cantidad");
                double total = rs.getDouble("total");
                ProductoGanancia nuevo = new ProductoGanancia(id, nombre, marca, ventas, total);
                productos.add(nuevo);
            }
            return productos;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }
    
    public LinkedList<ProductoSucursal>  productosSucursal(int select) {
        String query = "";
        switch (select) {
            case 1 ->
                query = top5ProductosVentas;
            case 2 ->
                query = top5ProductosGanancias;
            default -> {
            }
        }
        LinkedList<ProductoSucursal> productos = new LinkedList<>();
        try ( PreparedStatement ps = conexion.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int idSucursal = rs.getInt("id_sucursal");
                int cantidad = rs.getInt("cantidad");
                String producto = rs.getString("producto");
                String nombreSucursal = rs.getString("nombre_sucursal");
                String marca= rs.getString("marca");
                double total = rs.getDouble("total");
                ProductoSucursal nuevo = new ProductoSucursal(id, producto, marca, idSucursal, nombreSucursal, cantidad, total);
                productos.add(nuevo);
            }
            return productos;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }
}
