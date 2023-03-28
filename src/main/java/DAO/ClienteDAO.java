/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DataClasses.Cliente;
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
public class ClienteDAO extends DAO {

    private final String insert = "INSERT INTO cliente VALUES (?,?)";
    private final String selectAll = "SELECT * FROM cliente";
    private final String alter = "UPDATE cliente SET nombre = ? WHERE nit = ?";
    private final String searchByNit = "SELECT * FROM cliente WHERE nit LIKE ?";
    private final String searchByName = "SELECT * FROM cliente WHERE nombre LIKE ?";
    private final String searchByNitAndName = "SELECT * FROM cliente WHERE nit LIKE ? AND nombre LIKE ?";

    public ClienteDAO() {
    }

    public void insert(Cliente cliente) {
        try ( PreparedStatement ps = conexion.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cliente.nit());
            ps.setString(2, cliente.nombre());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cliente añadido con éxito");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public LinkedList<Cliente> selectAll() {
        LinkedList<Cliente> clientes = new LinkedList<>();
        try ( Statement st = conexion.createStatement();  ResultSet rs = st.executeQuery(selectAll)) {
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

    public DefaultTableModel getTableModel(LinkedList<Cliente> clientes) {
        Object header[] = new Object[]{"NIT", "NOMBRE"};
        DefaultTableModel model = new DefaultTableModel(header, 0);
        for (Cliente cliente : clientes) {
            model.addRow(new Object[]{cliente.nit(), cliente.nombre()});
        }
        return model;
    }

    public LinkedList<Cliente> search(Cliente cliente) {
        if (cliente == null || (cliente.nombre() == null && cliente.nit() == null)) {
            return null;
        }
        LinkedList<Cliente> clientes = new LinkedList<>();
        if (cliente.nit().isBlank()&& !cliente.nombre().isBlank()) {
            try ( PreparedStatement st = conexion.prepareStatement(searchByName);) {
                st.setString(1, "%"+cliente.nombre()+"%");
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
        } else if (cliente.nombre().isBlank()&& !cliente.nit().isBlank()) {
            try ( PreparedStatement st = conexion.prepareStatement(searchByNit);) {
                st.setString(1, "%"+cliente.nit()+"%");
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
                st.setString(1, "%"+cliente.nit()+"%");
                st.setString(2, "%"+cliente.nombre()+"%");
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

}
