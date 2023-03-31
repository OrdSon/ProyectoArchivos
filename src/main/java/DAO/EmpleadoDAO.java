/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DataClasses.Empleado;
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
public class EmpleadoDAO extends DAO {

    private final String insert = "INSERT INTO empleado (nombre, dpi, nacimiento, sucursal, tipo) VALUES (?,?,?,?,?)"; //DONE
    private final String selectAll = "SELECT * FROM empleado";//DONE
    private final String select = "SELECT * FROM empleado WHERE id = ?";//DONE
    private final String alter = "UPDATE empleado SET nombre = ?, sucursal = ?, tipo = ? WHERE id = ?";//needs test
    private final String searchByNit = "SELECT * FROM cliente WHERE nit LIKE ?"; // for later
    private final String searchByName = "SELECT * FROM cliente WHERE nombre LIKE ?";//for later
    private final String searchByNitAndName = "SELECT * FROM cliente WHERE nit LIKE ? AND nombre LIKE ?"; //for later
    private final String delete = "DELETE * FROM empleado WHERE id = ?";

    public EmpleadoDAO() {
    }

    public void insert(Empleado empleado) {
        try ( PreparedStatement ps = conexion.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, empleado.nombre());
            ps.setLong(2, empleado.dpi());
            ps.setDate(3, new java.sql.Date(empleado.nacimiento().getTime()));
            ps.setInt(4, empleado.sucursal());
            ps.setString(5, empleado.tipo());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Empleado añadido con éxito");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public Empleado select(int idEmpleado) {
        try ( PreparedStatement st = conexion.prepareStatement(select)) {
            st.setInt(1, idEmpleado);
            ResultSet rs = st.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                long dpi = rs.getLong("dpi");
                Date nacimiento = rs.getDate("nacimiento");
                int sucursal = rs.getInt("sucursal");
                String tipo = rs.getString("tipo");
                Date fecha = rs.getDate("fecha_contratacion");
                Empleado nuevo = new Empleado(id, nombre, dpi, fecha, sucursal, tipo, nacimiento);
                //System.out.println(nuevo.toString());
                return nuevo;
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
        return null;
    }

    public LinkedList<Empleado> selectAll() {
        LinkedList<Empleado> empleados = new LinkedList<>();
        try ( Statement st = conexion.createStatement();  ResultSet rs = st.executeQuery(selectAll)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                long dpi = rs.getLong("dpi");
                Date nacimiento = rs.getDate("nacimiento");
                int sucursal = rs.getInt("sucursal");
                String tipo = rs.getString("tipo");
                Date fecha = rs.getDate("fecha_contratacion");
                Empleado nuevo = new Empleado(id, nombre, dpi, fecha, sucursal, tipo, nacimiento);
                empleados.add(nuevo);
                //System.out.println(nuevo.toString());
            }
            return empleados;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public void update(Empleado empleado) {
        try ( PreparedStatement ps = conexion.prepareStatement(alter)) {
            ps.setString(1, empleado.nombre());
            ps.setInt(2, empleado.sucursal());
            ps.setString(3, empleado.tipo());
            ps.setInt(4, empleado.id());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Edicion exitosa para id: " + empleado.id() + "\n Datos:"
                    + "\nNombre: " + empleado.nombre()
                    + "\nID sucursal: " + empleado.sucursal()
                    + "\nTipo: " + empleado.tipo());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());

        }
    }

    public DefaultTableModel getTableModel(LinkedList<Empleado> empleados) {
        Object header[] = new Object[]{"id", "sucursal", "nombre", "dpi", "nacimiento", "tipo", "fecha contratación"};
        DefaultTableModel model = new DefaultTableModel(header, 0);
        for (Empleado empleado : empleados) {
            model.addRow(new Object[]{empleado.id(), empleado.sucursal(), empleado.nombre(), empleado.dpi(), empleado.nacimiento(), empleado.tipo(), empleado.fecha()});
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
