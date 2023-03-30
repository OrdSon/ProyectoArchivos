/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataClasses;

import java.util.Date;

/**
 *
 * @author OrdSon
 */
public record Venta(int id, String nit, int idSucursal, int idEmpleado, Date fecha, double descuento, double subTotal, double total) {
    
}
