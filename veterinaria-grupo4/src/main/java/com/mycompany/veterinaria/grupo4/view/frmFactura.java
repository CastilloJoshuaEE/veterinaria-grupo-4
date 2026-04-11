/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.grupo4.view;

import javax.swing.*;

public class frmFactura extends JFrame {
    private String cedulaCliente;
    
    public frmFactura(String cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
        setTitle("Facturas del Cliente: " + cedulaCliente);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new JLabel("Facturas del cliente " + cedulaCliente + " - En construcción", SwingConstants.CENTER));
    }
}