/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

import DataClasses.ClienteGanancia;
import DataClasses.ElementoLista;
import DataClasses.EmpleadoGanancia;
import DataClasses.ProductoGanancia;
import DataClasses.ProductoSucursal;
import DataClasses.SucursalGanancia;
import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author OrdSon
 */
public class TableModeler {

    public DefaultTableModel getCxWithMoreGains(LinkedList<ClienteGanancia> clientes) {
        Object header[] = new Object[]{"NIT", "NOMBRE", "TOTAL"};
        DefaultTableModel model = new DefaultTableModel(header, 0);
        for (ClienteGanancia cx : clientes) {
            model.addRow(new Object[]{cx.nit(), cx.nombre(), cx.ganancia()});
        }
        return model;
    }

    public DefaultTableModel getBestSellingProductsModel(LinkedList<ElementoLista> elementos) {
        Object header[] = new Object[]{"ID PRODUCTO", "PRODUCTO", "MARCA", " CANTIDAD", "PRECIO", "SUBTOTAL"};
        DefaultTableModel model = new DefaultTableModel(header, 0);
        for (ElementoLista elemento : elementos) {
            model.addRow(new Object[]{elemento.idProducto(), elemento.nombre(), elemento.marca(), elemento.cantidad(), elemento.precio(), elemento.subtotal()});
        }
        return model;
    }

    public DefaultTableModel getPrefferedShopsModel(LinkedList<SucursalGanancia> sucursales) {
        Object header[] = new Object[]{"ID", "NOMBRE", "DIRECCION", "VENTAS", "TOTAL"};
        DefaultTableModel model = new DefaultTableModel(header, 0);
        for (SucursalGanancia s : sucursales) {
            model.addRow(new Object[]{s.id(), s.nombre(), s.direccion(), s.cantidad(), s.total()});
        }
        return model;
    }

    public DefaultTableModel getEmployeesModel(LinkedList<EmpleadoGanancia> empleados) {
        Object header[] = new Object[]{"ID", "NOMBRE", "SUCURSAL", "NOMBRE SUCURSAL", "VENTAS", "TOTAL"};
        DefaultTableModel model = new DefaultTableModel(header, 0);
        for (EmpleadoGanancia e : empleados) {
            model.addRow(new Object[]{e.id(), e.nombre(), e.idSucursal(), e.nombreSucursal(), e.cantidadVentas(), e.Total()});
        }
        return model;
    }

    public DefaultTableModel getProductGainsModel(LinkedList<ProductoGanancia> productos) {
        Object header[] = new Object[]{"ID", "PRODUCTO", "MARCA", "VENTAS", "TOTAL"};
        DefaultTableModel model = new DefaultTableModel(header, 0);
        for (ProductoGanancia p: productos) {
            model.addRow(new Object[]{p.id(),p.nombre(),p.marca(),p.ventas(),p.total()});
        }
        return model;
    }
    public DefaultTableModel getProductShopModel(LinkedList<ProductoSucursal> productos) {
        Object header[] = new Object[]{"ID", "PRODUCTO", "MARCA","ID_SUCURSAL","NOMBRE SUCURSAL", "VENTAS", "TOTAL"};
        DefaultTableModel model = new DefaultTableModel(header, 0);
        for (ProductoSucursal p: productos) {
            model.addRow(new Object[]{p.id(),p.producto(),p.marca(),p.idSucursal(),p.nombreSucursal(),p.cantidad(),p.total()});
        }
        return model;
    }
}
