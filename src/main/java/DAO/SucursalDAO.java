/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DataClasses.Sucursal;
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
public class SucursalDAO extends DAO {

    private final String insert = "INSERT INTO sucursal (nombre, direccion) VALUES (?,?)";
    private final String selectAll = "SELECT * FROM sucursal WHERE nombre != 'Bodega'";
    private final String alter = "UPDATE sucursal SET nombre = ?, direccion = ? WHERE id = ?";
    private final String delete = "DELETE FROM sucursal WHERE id = ?";
    private final String searchByNit = "SELECT * FROM cliente WHERE id = ?";
    private final String searchByName = "SELECT * FROM sucursal WHERE nombre LIKE ?";
    private final String searchByNitAndName = "SELECT * FROM cliente WHERE nit LIKE ? AND nombre LIKE ?";

    public SucursalDAO() {

    }

    public void insert(Sucursal sucursal) {
        try ( PreparedStatement ps = conexion.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            if (sucursal.nombre().equalsIgnoreCase("bodega")) {
                JOptionPane.showMessageDialog(null, "No ingresar una segunda bodega");
                return;
            }
            ps.setString(1, sucursal.nombre());
            ps.setString(2, sucursal.direccion());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Sucursal añadida con éxito");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void delete(Sucursal sucursal) {
        try ( PreparedStatement ps = conexion.prepareStatement(delete, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, sucursal.id());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Sucursal: "+sucursal.nombre()+" \nID: "+sucursal.id()+"\nEliminada con éxito");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public LinkedList<Sucursal> selectAll() {
        LinkedList<Sucursal> sucursales = new LinkedList<>();
        try ( Statement st = conexion.createStatement();  ResultSet rs = st.executeQuery(selectAll)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String direccion = rs.getString("direccion");
                if (!nombre.isBlank() && !direccion.isBlank()) {
                    sucursales.add(new Sucursal(id, nombre, direccion));
                }
            }
            return sucursales;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public void update(Sucursal sucursal) {
        try ( PreparedStatement ps = conexion.prepareStatement(alter)) {
            ps.setString(1, sucursal.nombre());
            ps.setString(2, sucursal.direccion());
            ps.setInt(3, sucursal.id());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Edicion exitosa para ID: " + sucursal.id()
                    + "\n Nombre modificado: " + sucursal.nombre() + " \n Dirección modificada: " + sucursal.direccion());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());

        }
    }

    public DefaultTableModel getTableModel(LinkedList<Sucursal> sucursales) {
        Object header[] = new Object[]{"ID", "NOMBRE","DIRECCIÓN"};
        DefaultTableModel model = new DefaultTableModel(header, 0);
        for (Sucursal sucursal: sucursales) {
            model.addRow(new Object[]{sucursal.id(), sucursal.nombre(), sucursal.direccion()});
        }
        return model;
    }
/*
    public LinkedList<Cliente> search(Cliente cliente) {
        if (cliente == null || (cliente.nombre() == null && cliente.nit() == null)) {
            return null;
        }
        LinkedList<Cliente> clientes = new LinkedList<>();
        if (cliente.nit().isBlank() && !cliente.nombre().isBlank()) {
            try ( PreparedStatement st = conexion.prepareStatement(searchByName);) {
                st.setString(1, "%" + cliente.nombre() + "%");
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    String nit = rs.getString("nit");
                    String nombre = rs.getString("nombre");
                    if (nit != null || nombre != null) {
                        clientes.add(new Cliente(nit, nombre));
                    }
                }
                return clientes;
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        } else if (cliente.nombre().isBlank() && !cliente.nit().isBlank()) {
            try ( PreparedStatement st = conexion.prepareStatement(searchByNit);) {
                st.setString(1, "%" + cliente.nit() + "%");
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    String nit = rs.getString("nit");
                    String nombre = rs.getString("nombre");
                    if (nit != null || nombre != null) {
                        clientes.add(new Cliente(nit, nombre));
                    }
                }
                return clientes;
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        } else if (!cliente.nombre().isBlank() && !cliente.nit().isBlank()) {
            try ( PreparedStatement st = conexion.prepareStatement(searchByNitAndName);) {
                st.setString(1, "%" + cliente.nit() + "%");
                st.setString(2, "%" + cliente.nombre() + "%");
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    String nit = rs.getString("nit");
                    String nombre = rs.getString("nombre");
                    if (nit != null || nombre != null) {
                        clientes.add(new Cliente(nit, nombre));
                    }
                }
                return clientes;
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
        return null;
    }
*/

}
