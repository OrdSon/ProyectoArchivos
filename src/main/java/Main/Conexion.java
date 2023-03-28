/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author OrdSon
 */
public class Conexion {

    public static Connection dbconnection;
    //"jdbc:postgresql://localhost:PUERTO/ nombreBaseDatos"
    String url = "jdbc:postgresql://localhost:5432/proyecto1";
    //usuario de la base de datos
    String user = "postgres";
    //contraseña del usuario
    String password = "uber";

    public boolean startConnection() {
        if (dbconnection != null) {
            System.out.println("CONEXION PREVIAMENTE ESTABLECIDA AUN ACTIVA");
            return false;
        }
        try {
            dbconnection = DriverManager.getConnection(url, user, password);
            System.out.println("CONEXION ACTIVADA CON EXITO");

            return true;
        } catch (SQLException e) {
            System.err.println("ERROR DE CONEXION CON BASE DE DATOS: " + e.getMessage());
            return false;
        }
    }
}
