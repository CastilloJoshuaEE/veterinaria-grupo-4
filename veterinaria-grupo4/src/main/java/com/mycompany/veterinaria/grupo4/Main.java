/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.grupo4;

import com.mycompany.veterinaria.grupo4.view.frmComputadora;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

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

            frmComputadora ventana = new frmComputadora();
            ventana.setVisible(true);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(context::close));
    }
}