/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataClasses;

import java.util.LinkedList;

/**
 *
 * @author OrdSon
 */
public class SucursalComboItem {

    protected Sucursal sucursal;

    public SucursalComboItem(Sucursal sucursal) {
        this.sucursal = sucursal;
    }
    public SucursalComboItem() {
    }

    @Override
    public String toString() {
        return sucursal.nombre();
    }

    public LinkedList<SucursalComboItem> wrapList(LinkedList<Sucursal> sucursales) {
        LinkedList<SucursalComboItem> items = new LinkedList<>();
        for (Sucursal temp : sucursales) {
            items.add(new SucursalComboItem(temp));
        }
        return items;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public int getSucursalId() {
        return sucursal.id();
    }

}
