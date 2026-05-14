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

/**
 * Clase principal del sistema de gestión veterinaria Pet Town.
 * <p>
 * Esta clase inicializa la aplicación Spring Boot y la interfaz gráfica de usuario.
 * Configura el tema visual FlatLaf y establece el entorno gráfico para la aplicación.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MÓDULO: ATENCIÓN VETERINARIA
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
public class Main {

    /**
     * Método principal que inicia la aplicación.
     * <p>
     * Realiza las siguientes tareas:
     * <ul>
     *   <li>Configura el modo gráfico headless a false</li>
     *   <li>Instala la fuente Roboto y configura el tema FlatLaf</li>
     *   <li>Inicia el contexto de Spring Boot</li>
     *   <li>Muestra la ventana principal del sistema</li>
     *   <li>Registra un hook para cerrar el contexto al finalizar</li>
     * </ul>
     * </p>
     * 
     * @param args argumentos de línea de comandos (no utilizados)
     */
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