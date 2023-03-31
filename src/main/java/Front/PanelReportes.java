/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Front;

import DAO.ReportesDAO;
import Utilities.TableModeler;

/**
 *
 * @author OrdSon
 */
public class PanelReportes extends javax.swing.JPanel implements updater{

    ReportesDAO reportesDAO = new ReportesDAO();
    TableModeler tm = new TableModeler();

    /**
     * Creates new form PanelReportes
     */
    public PanelReportes() {
        initComponents();
        fillTables();
    }

    private void fillTables() {
        productosMasVendidosTable.setModel(tm.getBestSellingProductsModel(reportesDAO.productosMasVendidos()));
        clientesMasGananciaTable.setModel(tm.getCxWithMoreGains(reportesDAO.clientesMasGanancia()));
        sucursalesMasVentasTable.setModel(tm.getPrefferedShopsModel(reportesDAO.sucursalesConMasVentas(1)));
        sucursalesMasGananciasTable.setModel(tm.getPrefferedShopsModel(reportesDAO.sucursalesConMasVentas(2)));
        empleadosMasVentasTable.setModel(tm.getEmployeesModel(reportesDAO.empleadosMasVentas(1)));
        empleadosMasGananaciasTable.setModel(tm.getEmployeesModel(reportesDAO.empleadosMasVentas(2)));
        productosMasGananciaTable.setModel(tm.getProductGainsModel(reportesDAO.productosMasGanancia()));
        top5VentasTable.setModel(tm.getProductShopModel(reportesDAO.productosSucursal(1)));
        top5GananciasTable.setModel(tm.getProductShopModel(reportesDAO.productosSucursal(2)));

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        productosMasVendidosTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        clientesMasGananciaTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        sucursalesMasVentasTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        sucursalesMasGananciasTable = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        empleadosMasVentasTable = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        empleadosMasGananaciasTable = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        productosMasGananciaTable = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        top5GananciasTable = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        top5VentasTable = new javax.swing.JTable();

        setMinimumSize(new java.awt.Dimension(625, 625));
        setPreferredSize(new java.awt.Dimension(625, 625));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        productosMasVendidosTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(productosMasVendidosTable);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        tabPane.addTab("Productos más vendidos", jPanel1);

        jPanel2.setLayout(new java.awt.BorderLayout());

        clientesMasGananciaTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(clientesMasGananciaTable);

        jPanel2.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        tabPane.addTab("Ganancia por cliente", jPanel2);

        jPanel3.setLayout(new java.awt.BorderLayout());

        sucursalesMasVentasTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(sucursalesMasVentasTable);

        jPanel3.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        tabPane.addTab("Sucursales con más ventas", jPanel3);

        jPanel4.setLayout(new java.awt.BorderLayout());

        sucursalesMasGananciasTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(sucursalesMasGananciasTable);

        jPanel4.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        tabPane.addTab("Sucursales con más ganancias", jPanel4);

        jPanel5.setLayout(new java.awt.BorderLayout());

        empleadosMasVentasTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(empleadosMasVentasTable);

        jPanel5.add(jScrollPane5, java.awt.BorderLayout.CENTER);

        tabPane.addTab("Empleados con más ventas", jPanel5);

        jPanel6.setLayout(new java.awt.BorderLayout());

        empleadosMasGananaciasTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane6.setViewportView(empleadosMasGananaciasTable);

        jPanel6.add(jScrollPane6, java.awt.BorderLayout.CENTER);

        tabPane.addTab("Empleados con más ganancias", jPanel6);

        jPanel7.setLayout(new java.awt.BorderLayout());

        productosMasGananciaTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane7.setViewportView(productosMasGananciaTable);

        jPanel7.add(jScrollPane7, java.awt.BorderLayout.CENTER);

        tabPane.addTab("Productos con más ganancias", jPanel7);

        jPanel8.setLayout(new java.awt.BorderLayout());

        top5GananciasTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane8.setViewportView(top5GananciasTable);

        jPanel8.add(jScrollPane8, java.awt.BorderLayout.CENTER);

        tabPane.addTab("Top 5 Ganancias", jPanel8);

        jPanel9.setLayout(new java.awt.BorderLayout());

        top5VentasTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane9.setViewportView(top5VentasTable);

        jPanel9.add(jScrollPane9, java.awt.BorderLayout.CENTER);

        tabPane.addTab("Top 5 Ventas", jPanel9);

        add(tabPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable clientesMasGananciaTable;
    private javax.swing.JTable empleadosMasGananaciasTable;
    private javax.swing.JTable empleadosMasVentasTable;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable productosMasGananciaTable;
    private javax.swing.JTable productosMasVendidosTable;
    private javax.swing.JTable sucursalesMasGananciasTable;
    private javax.swing.JTable sucursalesMasVentasTable;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JTable top5GananciasTable;
    private javax.swing.JTable top5VentasTable;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update() {
        fillTables();
    }
}
