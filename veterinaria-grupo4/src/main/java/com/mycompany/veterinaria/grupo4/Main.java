/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.grupo4;

import com.mycompany.veterinaria.grupo4.view.frmComputadora;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import com.formdev.flatlaf.FlatLightLaf;
import com.mycompany.veterinaria.grupo4.view.auth.FrmPrincipal;
import com.mycompany.veterinaria.grupo4.viewController.AppController;

import javax.swing.*;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        // FORZAR modo gráfico ANTES de Spring Boot USUARIO: JUAN contraseña: 123456
        System.setProperty("java.awt.headless", "false");

        ConfigurableApplicationContext context =
                SpringApplication.run(Main.class, args);

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            FrmPrincipal frm = new FrmPrincipal();
            AppController ctrl = new AppController(frm);
            frm.setVisible(true);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(context::close));
    }
}