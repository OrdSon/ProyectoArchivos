/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataClasses;

/**
 *
 * @author OrdSon
 */

/*
SELECT l.id_venta AS venta, l.id_producto AS id_producto, p.nombre AS nombre, p.marca AS marca, 
                                              COUNT(*) AS cantidad, SUM(p.precio) AS subtotal
*/
public record ElementoLista (int idVenta, int idProducto, String nombre, String marca, int cantidad,double precio, double subtotal) {
    
}
