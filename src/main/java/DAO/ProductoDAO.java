/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DataClasses.Producto;
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
public class ProductoDAO extends DAO{
     private final String insert = "INSERT INTO producto (nombre, marca, precio) VALUES (?,?,?)";//DONE
    private final String selectAll = "SELECT * FROM producto";//DONE
    private final String select = "SELECT * FROM producto WHERE id = ?";//
    private final String alter = "UPDATE producto SET nombre = ?, marca = ?, precio = ? WHERE id = ?";//pending
    private final String delete = "DELETE * FROM producto WHERE id = ?";

    public ProductoDAO() {
    }

    public void insert(Producto producto) {
        try ( PreparedStatement ps = conexion.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, producto.nombre());
            ps.setString(2, producto.marca());
            ps.setDouble(3, producto.precio());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Producto añadido con éxito");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public LinkedList<Producto> selectAll() {
        LinkedList<Producto> productos = new LinkedList<>();
        try ( Statement st = conexion.createStatement();  ResultSet rs = st.executeQuery(selectAll)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String marca = rs.getString("marca");
                double precio = rs.getDouble("precio");
                Producto nuevo = new Producto(id, nombre, marca, precio);
                productos.add(nuevo);
                System.out.println(nuevo.toString());
            }
            return productos;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }
    
    public Producto select(int idProducto) {
        try ( PreparedStatement ps = conexion.prepareStatement(select)) {
            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String marca = rs.getString("marca");
                double precio = rs.getDouble("precio");
                Producto nuevo = new Producto(id, nombre, marca, precio);
                System.out.println(nuevo.toString());
                return nuevo;
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
         return null;
    }

    public void update(Producto producto) {
        try ( PreparedStatement ps = conexion.prepareStatement(alter)) {
            ps.setString(1, producto.nombre());
            ps.setString(2, producto.marca());
            ps.setDouble(3, producto.precio());
            ps.setInt(4, producto.id());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Edicion exitosa para id: " +producto.id()+ "\n Datos:" +
                    "\nNombre: "+producto.nombre()+
                    "\n Marca"+producto.marca()+
                    "\nPrecio"+producto.precio());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());

        }
    }

    public DefaultTableModel getTableModel(LinkedList<Producto> productos) {
        Object header[] = new Object[]{"ID","MARCA","PRECIO","NOMBRE"};
        DefaultTableModel model = new DefaultTableModel(header, 0);
        for (Producto producto: productos) {
            model.addRow(new Object[]{producto.id(),producto.marca(),producto.precio(),producto.nombre()});
        }
        return model;
    }
}
