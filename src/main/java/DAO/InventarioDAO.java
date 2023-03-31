/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DataClasses.Inventario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author OrdSon
 */
public class InventarioDAO extends DAO {

    private final String insert = "INSERT INTO inventario VALUES (?,?,?)";

    private final String selectAll = """
                                     SELECT i.id_producto as id, p.nombre as nombre,p.marca as marca, 
                                     i.id_sucursal as id_sucursal, s.nombre as sucursal, i.cantidad FROM 
                                     inventario AS i INNER JOIN producto AS p ON p.id = i.id_producto 
                                     INNER JOIN sucursal AS s ON s.id = i.id_sucursal  """;
    private final String selectWhere = """
                                     SELECT i.id_producto as id, p.nombre as nombre,p.marca as marca, 
                                     i.id_sucursal as id_sucursal, s.nombre as sucursal, i.cantidad FROM 
                                     inventario AS i INNER JOIN producto AS p ON p.id = i.id_producto 
                                     INNER JOIN sucursal AS s ON s.id = i.id_sucursal  WHERE i.id_sucursal = ?""";
    
    private final String selectCantidad = " SELECT cantidad FROM inventario WHERE id_sucursal = ? and id_producto = ?;";
    private final String alter = "UPDATE inventario SET cantidad = ? WHERE id_producto = ? AND id_sucursal = ?";

    public InventarioDAO() {
    }

    public void insert(Inventario inventario) {
        LinkedList<Inventario> cuenta = select(inventario);
        String mensaje = "Productos ingresados con éxito";
        if (inventario.Cantidad() < 0) {
            mensaje = "Productos retirados con éxito";
        }
        if (!cuenta.isEmpty()) {
            Inventario temp = cuenta.getFirst();
            int total = temp.Cantidad() + inventario.Cantidad();
            Inventario nuevo = new Inventario(temp.idProducto(), temp.nombreProducto(),
                    temp.marcaProducto(), temp.idSucursal(), temp.nombreSucursal(), total);
            update(nuevo, mensaje);
            return;
        }
        if (cuenta.isEmpty()) {
            if (inventario.Cantidad() <= 0) {
                return;
            }
            try ( PreparedStatement ps = conexion.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, inventario.idProducto());
                ps.setInt(2, inventario.idSucursal());
                ps.setInt(3, inventario.Cantidad());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, mensaje);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());

            }
        } else {
            //update(cuenta);
        }
    }

    public LinkedList<Inventario> selectAll() {
        LinkedList<Inventario> productos = new LinkedList<>();
        try ( Statement st = conexion.createStatement();  ResultSet rs = st.executeQuery(selectAll)) {
            while (rs.next()) {
                int idProducto = rs.getInt("id");
                String nombreProducto = rs.getString("nombre");
                String marcaProducto = rs.getString("marca");
                int idSucursal = rs.getInt("id_sucursal");
                String nombreSucursal = rs.getString("sucursal");
                int cantidad = rs.getInt("cantidad");
                Inventario nuevo = new Inventario(idProducto, nombreProducto, marcaProducto, idSucursal, nombreSucursal, cantidad);
                productos.add(nuevo);
            }
            return productos;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public LinkedList<Inventario> selectAll(int sucursal) {
        LinkedList<Inventario> productos = new LinkedList<>();
        try ( PreparedStatement ps = conexion.prepareStatement(selectWhere)){
            ps.setInt(1, sucursal);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int idProducto = rs.getInt("id");
                String nombreProducto = rs.getString("nombre");
                String marcaProducto = rs.getString("marca");
                int idSucursal = rs.getInt("id_sucursal");
                String nombreSucursal = rs.getString("sucursal");
                int cantidad = rs.getInt("cantidad");
                Inventario nuevo = new Inventario(idProducto, nombreProducto, marcaProducto, idSucursal, nombreSucursal, cantidad);
                productos.add(nuevo);
            }
            return productos;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }
    public int selectCantidad(int idSucursal, int idProducto){
        try (PreparedStatement ps = conexion.prepareStatement(selectCantidad)){
            ps.setInt(1, idSucursal);
            ps.setInt(2, idProducto);
            ResultSet rs = ps.executeQuery();
            int cantidad = rs.getInt("cantidad");
            return cantidad;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return -1;
        }
    }
    public void update(Inventario inventario, String mensaje) {
        try ( PreparedStatement ps = conexion.prepareStatement(alter)) {
            ps.setInt(1, inventario.Cantidad());
            ps.setInt(2, inventario.idProducto());
            ps.setInt(3, inventario.idSucursal());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, mensaje);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());

        }
    }

    public DefaultTableModel getTableModel(LinkedList<Inventario> inventarios) {
        Object header[] = new Object[]{"ID", "NOMBRE", "MARCA", "ID SUCURSAL", "CANTIDAD"};
        DefaultTableModel model = new DefaultTableModel(header, 0);
        for (Inventario slot : inventarios) {
            model.addRow(new Object[]{slot.idProducto(), slot.nombreProducto(), slot.marcaProducto(), slot.idSucursal(), slot.Cantidad()});
        }
        return model;
    }

    public LinkedList<Inventario> select(Inventario inventario) {
        StringBuilder query = new StringBuilder(selectAll);
        query.append(" WHERE ");
        boolean idProducto = false;
        boolean nombre = false;
        boolean marca = false;
        if (inventario.idProducto() != 0) {
            query.append(" p.id = ? ");
            idProducto = true;
        }
        if (!inventario.nombreProducto().isBlank()) {
            if (idProducto) {
                query.append(" AND ");
            }
            query.append("p.nombre = ? ");
            nombre = true;
        }
        if (!inventario.marcaProducto().isBlank()) {
            if (idProducto || nombre) {
                query.append(" AND ");
            }
            query.append(" p.marca = ? ");
            marca = true;
        }
        if (idProducto || nombre || marca) {
            query.append(" AND ");
        }

        query.append(" id_sucursal = ? ");

        int cuenta = 1;
        LinkedList<Inventario> productos = new LinkedList<>();
        try ( PreparedStatement ps = conexion.prepareStatement(query.toString())) {
            if (idProducto) {
                ps.setInt(cuenta++, inventario.idProducto());
            }
            if (nombre) {
                ps.setString(cuenta++, inventario.nombreProducto());
            }
            if (marca) {
                ps.setString(cuenta++, inventario.marcaProducto());
            }
            ps.setInt(cuenta++, inventario.idSucursal());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int idProduct = rs.getInt("id");
                String nombreProducto = rs.getString("nombre");
                String marcaProducto = rs.getString("marca");
                int idSucursal = rs.getInt("id_sucursal");
                String nombreSucursal = rs.getString("sucursal");
                int cantidad = rs.getInt("cantidad");
                Inventario nuevo = new Inventario(idProduct, nombreProducto, marcaProducto, idSucursal, nombreSucursal, cantidad);
                productos.add(nuevo);
            }
            return productos;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }

}
