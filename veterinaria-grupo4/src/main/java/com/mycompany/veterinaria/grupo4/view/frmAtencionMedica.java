/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.grupo4.view;

import javax.swing.*;

public class frmAtencionMedica extends JFrame {
    
    public frmAtencionMedica(String nombreUsuario, int idUsuario) {
        setTitle("Atención Médica");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new JLabel("Módulo de Atención Médica - En construcción", SwingConstants.CENTER));
    }
}