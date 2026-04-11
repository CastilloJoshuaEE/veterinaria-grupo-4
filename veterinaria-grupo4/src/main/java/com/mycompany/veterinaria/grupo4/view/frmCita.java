/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.grupo4.view;

import javax.swing.*;

public class frmCita extends JFrame {
    
    public frmCita(String nombreUsuario, int idUsuario) {
        setTitle("Gestión de Citas");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new JLabel("Módulo de Citas - En construcción", SwingConstants.CENTER));
    }
}