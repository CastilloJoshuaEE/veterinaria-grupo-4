package com.mycompany.veterinaria.grupo4.view.servicio;

import com.formdev.flatlaf.FlatClientProperties;
import com.mycompany.veterinaria.grupo4.view.swing.Button;
import com.mycompany.veterinaria.grupo4.view.swing.MyTextField;
import com.mycompany.veterinaria.grupo4.view.swing.table.Table;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class PnlServicio extends javax.swing.JPanel {

    public PnlServicio() {
        initComponents();
        setOpaque(true);
        putClientProperty(FlatClientProperties.STYLE, "background:$Panel.background");
        setBorder(new EmptyBorder(5, 5, 5, 5));
        
        pnlBg.setOpaque(true);
        pnlBg.putClientProperty(FlatClientProperties.STYLE, "background:$Panel.background");
        pnlBg.setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    public Button getBtnBuscar() {
        return btnBuscar;
    }

    public Button getBtnNuevo() {
        return btnNuevo;
    }

    public Table getTblServicio() {
        return tblServicio;
    }

    public MyTextField getTxtBusqueda() {
        return txtBusqueda;
    }

    public JScrollPane getScrollPane() {
        return jScrollPane1;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        pnlBg = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtBusqueda = new com.mycompany.veterinaria.grupo4.view.swing.MyTextField();
        btnNuevo = new com.mycompany.veterinaria.grupo4.view.swing.Button();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblServicio = new com.mycompany.veterinaria.grupo4.view.swing.table.Table();
        btnBuscar = new com.mycompany.veterinaria.grupo4.view.swing.Button();

        setPreferredSize(new java.awt.Dimension(806, 460));
        setLayout(new java.awt.BorderLayout());

        pnlBg.setPreferredSize(new java.awt.Dimension(806, 460));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Servicios");

        txtBusqueda.setText("myTextField1");
        txtBusqueda.setHint("Buscar por nombre...");

        btnNuevo.setBackground(new java.awt.Color(255, 178, 39));
        btnNuevo.setForeground(new java.awt.Color(255, 255, 255));
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/add-one.png")));
        btnNuevo.setText("Nuevo");
        btnNuevo.setToolTipText("");

        tblServicio.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Nombre", "Precio", "Duración", "Estado", "Acción"}
        ) {
            @Override public boolean isCellEditable(int row, int col) { return col == 5; }
        });
        jScrollPane1.setViewportView(tblServicio);

        btnBuscar.setBackground(new java.awt.Color(255, 178, 39));
        btnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/search.png")));
        btnBuscar.setText("Buscar");
        btnBuscar.setToolTipText("");

        javax.swing.GroupLayout pnlBgLayout = new javax.swing.GroupLayout(pnlBg);
        pnlBg.setLayout(pnlBgLayout);
        pnlBgLayout.setHorizontalGroup(
            pnlBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBgLayout.createSequentialGroup()
                .addGap(362, 362, 362)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnlBgLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(pnlBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE)
                    .addGroup(pnlBgLayout.createSequentialGroup()
                        .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );
        pnlBgLayout.setVerticalGroup(
            pnlBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBgLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );

        add(pnlBg, java.awt.BorderLayout.CENTER);
    }

    private com.mycompany.veterinaria.grupo4.view.swing.Button btnBuscar;
    private com.mycompany.veterinaria.grupo4.view.swing.Button btnNuevo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlBg;
    private com.mycompany.veterinaria.grupo4.view.swing.table.Table tblServicio;
    private com.mycompany.veterinaria.grupo4.view.swing.MyTextField txtBusqueda;
}