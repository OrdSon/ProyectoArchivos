/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package Main;

import Front.Login;

/**
 *
 * @author OrdSon
 */
public class Postgres {

    public static void main(String[] args) {
        Conexion conexion = new Conexion();
        conexion.startConnection();
        System.out.println("Hello World!");
        Login log = new Login();
        log.setVisible(true);
    }
}
