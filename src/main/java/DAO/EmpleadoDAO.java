/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DataClasses.Empleado;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 *
 * @author OrdSon
 */
public class EmpleadoDAO extends DAO {

    String insert = "INSERT INTO cliente VALUES (?,?)";
    long id = 0;

    public EmpleadoDAO(Empleado empleado) {
        try ( PreparedStatement ps
                = conexion.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            
        } catch (Exception e) {
        }

    }

}
