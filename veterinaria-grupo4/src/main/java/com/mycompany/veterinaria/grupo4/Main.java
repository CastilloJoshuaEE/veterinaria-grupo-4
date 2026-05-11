package com.mycompany.veterinaria.grupo4;

import com.formdev.flatlaf.FlatLaf;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.mycompany.veterinaria.grupo4.view.FrmPrincipal;
import com.mycompany.veterinaria.grupo4.controller.AppController;
import java.awt.Font;

import javax.swing.*;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        // FORZAR modo gráfico ANTES de Spring Boot USUARIO: JUAN contraseña: 1234segura1
        System.setProperty("java.awt.headless", "false");
        
        
        SwingUtilities.invokeLater(() -> {
            FlatRobotoFont.install();
            FlatLaf.registerCustomDefaultsSource("com.mycompany.veterinaria.grupo4.view.theme");
            FlatLightLaf.setup();
        
            UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        });
    
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        
        SwingUtilities.invokeLater(() -> {
            FrmPrincipal frm = new FrmPrincipal();
            AppController ctrl = new AppController(frm);
            frm.setTitle("Sistema Veterinaria");
            frm.setVisible(true);
        });
         
        Runtime.getRuntime().addShutdownHook(
            new Thread(context::close)
        );
    }
}