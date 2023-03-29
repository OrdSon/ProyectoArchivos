/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataClasses;
/**
 *
 * @author OrdSon
 * @param ID, NOMBRE, DPI, FECHA, SUCURSAL, TIPO
 */
import java.util.Date;
public record Empleado (int id, String nombre, long dpi, Date fecha, int sucursal, String tipo, Date nacimiento){
    
}
