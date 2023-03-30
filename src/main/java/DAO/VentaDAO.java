/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

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

    private final String insert = "INSERT INTO venta (id_cliente, id_sucursal, id_empleado, descuento, subtotal, total) VALUES (?,?,?,?,?,?)";
    private final String selectAll = "SELECT * FROM venta";
    private final String select = "SELECT * FROM cliente WHERE nit = ?";
    private final String alter = "UPDATE cliente SET nombre = ? WHERE nit = ?";
    private final String searchByNit = "SELECT * FROM cliente WHERE nit LIKE ?";
    private final String searchByName = "SELECT * FROM cliente WHERE nombre LIKE ?";
    private final String searchByNitAndName = "SELECT * FROM cliente WHERE nit LIKE ? AND nombre LIKE ?";

    public VentaDAO() {
    }

    public void insert(Venta venta) {
        try ( PreparedStatement ps = conexion.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, venta.nit());
            ps.setInt(2, venta.idSucursal());
            ps.setInt(3, venta.idEmpleado());
            ps.setDouble(4, venta.descuento());
            ps.setDouble(5, venta.subTotal());
            ps.setDouble(6, venta.total());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Venta añadida con éxito");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
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

    public DefaultTableModel getTableModel(LinkedList<Venta> ventas) {
        Object header[] = new Object[]{"ID", "NIT","SUCURSAL","EMPLEADO", "FECHA", "DESCUENTO", "SUBTOTAL", "TOTAL"};
        DefaultTableModel model = new DefaultTableModel(header, 0);
        for (Venta sale:ventas) {
            model.addRow(new Object[]{sale.id(),sale.nit(),sale.idSucursal(),sale.idEmpleado(),sale.fecha(),sale.descuento(),sale.subTotal(),sale.total()});
        }
        return model;
    }

}
