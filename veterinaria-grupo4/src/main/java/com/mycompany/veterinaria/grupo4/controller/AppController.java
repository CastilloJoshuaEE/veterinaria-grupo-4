package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.util.NotificationManager;
import com.mycompany.veterinaria.grupo4.util.SessionManager;
import com.mycompany.veterinaria.grupo4.view.FrmPrincipal;
import com.mycompany.veterinaria.grupo4.view.auth.PnlBgLogin;
import com.mycompany.veterinaria.grupo4.view.PnlMain;
import com.mycompany.veterinaria.grupo4.view.Dashboard;
import com.mycompany.veterinaria.grupo4.view.atencionMedica.PnlAtencionMedica;
import com.mycompany.veterinaria.grupo4.view.cita.PnlCita;
import com.mycompany.veterinaria.grupo4.view.cliente.PnlCliente;
import com.mycompany.veterinaria.grupo4.view.factura.frmFactura;
import com.mycompany.veterinaria.grupo4.view.factura.frmSeleccionarCedula;
import com.mycompany.veterinaria.grupo4.view.mascota.PnlMascota;
import com.mycompany.veterinaria.grupo4.view.personalVeterinario.PnlVeterinario;
import com.mycompany.veterinaria.grupo4.view.swing.menu.MenuAction;
import com.mycompany.veterinaria.grupo4.view.historial.PnlHistorialMedico;
import com.mycompany.veterinaria.grupo4.view.recordatorio.PnlRecordatorioReporte;
import com.mycompany.veterinaria.grupo4.view.servicio.PnlServicio;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Controlador principal de la aplicacion.
 * <p>
 * Gestiona la navegacion entre las diferentes vistas del sistema,
 * el flujo de autenticacion y la carga de modulos segun el menu seleccionado.
 * </p>
 * 
 * @author ROBLES MORALES JUAN ANDRES
 * @version 2.0
 * @since 1.0
 */
public class AppController {
    private FrmPrincipal frm;
    private PnlBgLogin bgLogin;
    private PnlMain main;
    private PnlCliente pnlCliente;
    private PnlCita pnlCita;
    private PnlMascota pnlMascota;
    private PnlVeterinario pnlVeterinario;
    private PnlAtencionMedica pnlAtencion;
    private PnlHistorialMedico pnlHistorialMedico;
    private PnlServicio pnlServicio;
    private PnlRecordatorioReporte pnlRecordatorioReporte;
    private AuthController authcontroller;

    /**
     * Constructor del controlador principal.
     * 
     * @param frm ventana principal de la aplicacion
     */
    public AppController(FrmPrincipal frm) {
        this.frm = frm;
        cargarLogin();
    }
    
    /**
     * Carga la pantalla de login en la ventana principal.
     */
    private void cargarLogin(){
        bgLogin = new PnlBgLogin();
        authcontroller = new AuthController(this, bgLogin);
        
        JPanel bgPrincipal = frm.getBgPrincipal();
        bgPrincipal.removeAll();
        bgPrincipal.setLayout(new BorderLayout());
        
        com.mycompany.veterinaria.grupo4.view.auth.Background bgLoginContainer = frm.getBgLogin();
        JPanel pnlVentana = bgLoginContainer.getPnlVentana();
        pnlVentana.removeAll();
        pnlVentana.setLayout(new BorderLayout());
        pnlVentana.add(bgLogin, BorderLayout.CENTER);
        pnlVentana.revalidate();
        pnlVentana.repaint();
        
        bgPrincipal.add(bgLoginContainer, BorderLayout.CENTER);
        bgPrincipal.revalidate();      
        bgPrincipal.repaint();
        
        frm.revalidate();
        frm.repaint();
    } 
    
    /**
     * Carga el panel principal con el menu y el dashboard.
     */
    public void cargarPnlDefault(){
        if(main == null){
            main = new PnlMain();
            initMenuEvent();
        }
        JPanel bg = frm.getBgPrincipal();
        bg.removeAll();
        bg.setLayout(new BorderLayout());
        bg.add(main, BorderLayout.CENTER);
        bg.revalidate();      
        bg.repaint();
        mostrarDashboard();
    }
    
    /**
     * Inicializa los eventos del menu principal.
     * Define que accion ejecutar para cada opcion del menu.
     * 
     * ADMINISTRADOR:
     * 0: Bienvenida → Dashboard
     * 1: Citas Médicas → PnlCita
     * 2: Atención Médica → PnlAtencionMedica
     * 3: Mascotas → (sub: 1=Ver todos, 2=Historial Médico)
     * 4: Clientes → PnlCliente
     * 5: Inventario → (sub: 3=Servicios)
     * 6: Facturación → frmFactura
     * 7: Personal Veterinario → PnlVeterinario
     * 8: Configuración → PnlRecordatorioReporte
     * 9: Ayuda → (sub: 1=Documentación, 2=Acerca de)
     * 10: Cerrar Sesión → logout
     * 
     * RECEPCIONISTA:
     * 0: Bienvenida → Dashboard
     * 1: Citas Médicas → PnlCita
     * 2: Mascotas → (sub: 1=Ver todos, 2=Historial Médico)
     * 3: Clientes → PnlCliente
     * 4: Facturación → frmFactura
     * 5: Ayuda → (sub: 1=Documentación, 2=Acerca de)
     * 6: Cerrar Sesión → logout
     * 
     * VETERINARIO:
     * 0: Bienvenida → Dashboard
     * 1: Atención Médica → PnlAtencionMedica
     * 2: Mascotas → (sub: 1=Ver todos, 2=Historial Médico)
     * 3: Clientes → PnlCliente
     * 4: Facturación → frmFactura
     * 5: Personal Veterinario → PnlVeterinario
     * 6: Ayuda → (sub: 1=Documentación, 2=Acerca de)
     * 7: Cerrar Sesión → logout
     */
    private void initMenuEvent() {
        main.addMenuEvent((int index, int subIndex, MenuAction action) -> {
            System.out.println("Menu seleccionado - index: " + index + ", subIndex: " + subIndex);
            
            String rol = SessionManager.getInstance().getCurrentUserRole();
            
            switch (rol.toUpperCase()) {
                case "ADMINISTRADOR":
                    manejarMenuAdministrador(index, subIndex, action);
                    break;
                case "RECEPCIONISTA":
                    manejarMenuRecepcionista(index, subIndex, action);
                    break;
                case "VETERINARIO":
                    manejarMenuVeterinario(index, subIndex, action);
                    break;
                default:
                    action.cancel();
                    break;
            }
        });
    }
    
    /**
     * Maneja las acciones del menú para ADMINISTRADOR
     */
    private void manejarMenuAdministrador(int index, int subIndex, MenuAction action) {
        switch (index) {
            case 0: // Bienvenida
                mostrarDashboard();
                break;
            case 1: // Citas Médicas
                mostrarCitas();
                break;
            case 2: // Atención Médica
                mostrarAtencion();
                break;
            case 3: // Mascotas
                switch (subIndex) {
                    case 0: // Mascotas (principal)
                    case 1: // Ver todos
                        mostrarMascotas();
                        break;
                    case 2: // Historial Médico
                        mostrarHistorialMedico();
                        break;
                    default:
                        action.cancel();
                        break;
                }
                break;
            case 4: // Clientes
                mostrarClientes();
                break;
            case 5: // Inventario
                switch (subIndex) {
                    case 0: // Inventario (principal)
                    case 1: // Servicios
                        mostrarServicios();
                        break;
                    default:
                        action.cancel();
                        break;
                }
                break;
            case 6: // Facturación
                mostrarFacturacion();
                break;
            case 7: // Personal Veterinario
                mostrarPersonalVeterinario();
                break;
            case 8: // Configuración
                mostrarReporteRecordatorios();
                break;
            case 9: // Ayuda
                switch (subIndex) {
                    case 1: // Ver Documentación
                        abrirDocumentacion();
                        break;
                    case 2: // Acerca de
                        mostrarAcercaDe();
                        break;
                    default:
                        action.cancel();
                        break;
                }
                break;
            case 10: // Cerrar Sesión
                cerrarSesion();
                break;
            default:
                action.cancel();
                break;
        }
    }
    
    /**
     * Maneja las acciones del menú para RECEPCIONISTA
     */
    private void manejarMenuRecepcionista(int index, int subIndex, MenuAction action) {
        switch (index) {
            case 0: // Bienvenida
                mostrarDashboard();
                break;
            case 1: // Citas Médicas
                mostrarCitas();
                break;
            case 2: // Mascotas
                switch (subIndex) {
                    case 0:
                    case 1:
                        mostrarMascotas();
                        break;
                    case 2:
                        mostrarHistorialMedico();
                        break;
                    default:
                        action.cancel();
                        break;
                }
                break;
            case 3: // Clientes
                mostrarClientes();
                break;
            case 4: // Facturación
                mostrarFacturacion();
                break;
            case 5: // Ayuda
                switch (subIndex) {
                    case 1:
                        abrirDocumentacion();
                        break;
                    case 2:
                        mostrarAcercaDe();
                        break;
                    default:
                        action.cancel();
                        break;
                }
                break;
            case 6: // Cerrar Sesión
                cerrarSesion();
                break;
            default:
                action.cancel();
                break;
        }
    }
    
    /**
     * Maneja las acciones del menú para VETERINARIO
     */
    private void manejarMenuVeterinario(int index, int subIndex, MenuAction action) {
        switch (index) {
            case 0: // Bienvenida
                mostrarDashboard();
                break;
            case 1: // Atención Médica
                mostrarAtencion();
                break;
            case 2: // Mascotas
                switch (subIndex) {
                    case 0:
                    case 1:
                        mostrarMascotas();
                        break;
                    case 2:
                        mostrarHistorialMedico();
                        break;
                    default:
                        action.cancel();
                        break;
                }
                break;
            case 3: // Clientes
                mostrarClientes();
                break;
            case 4: // Facturación
                mostrarFacturacion();
                break;
            case 5: // Personal Veterinario
                mostrarPersonalVeterinario();
                break;
            case 6: // Ayuda
                switch (subIndex) {
                    case 1:
                        abrirDocumentacion();
                        break;
                    case 2:
                        mostrarAcercaDe();
                        break;
                    default:
                        action.cancel();
                        break;
                }
                break;
            case 7: // Cerrar Sesión
                cerrarSesion();
                break;
            default:
                action.cancel();
                break;
        }
    }
    
    /**
     * Muestra el panel de dashboard (inicio).
     */
    private void mostrarDashboard() {
        main.showForm(new Dashboard());
    }

    /**
     * Muestra el panel de gestion de mascotas.
     */
    private void mostrarMascotas() {
        if (pnlMascota == null) pnlMascota = new PnlMascota();
        CtrlMascotas ctrl = new CtrlMascotas(pnlMascota);
        main.showForm(pnlMascota);
    }
    
    /**
     * Muestra el panel de atencion medica.
     */
    private void mostrarAtencion() {
        if (pnlAtencion == null) pnlAtencion = new PnlAtencionMedica();
        CtrlAtencionMedica ctrl = new CtrlAtencionMedica(pnlAtencion);
        main.showForm(pnlAtencion);
    }

    /**
     * Muestra el panel de gestion de citas.
     */
    private void mostrarCitas() {
        if (pnlCita == null) pnlCita = new PnlCita();
        CtrlCitas ctrl = new CtrlCitas(pnlCita);
        main.showForm(pnlCita);
    }

    /**
     * Muestra el panel de gestion de clientes.
     */
    private void mostrarClientes() {
        if (pnlCliente == null) pnlCliente = new PnlCliente();
        CtrlCliente ctrlCliente = new CtrlCliente(pnlCliente);
        main.showForm(pnlCliente);
    }
    
    /**
     * Muestra el panel de gestion de personal veterinario.
     */
    private void mostrarPersonalVeterinario() {
        if (pnlVeterinario == null) pnlVeterinario = new PnlVeterinario();
        CtrlVeterinario ctrlVeterinario = new CtrlVeterinario(pnlVeterinario);
        main.showForm(pnlVeterinario);
    }
    
    /**
     * Muestra el panel de historial medico.
     */
    private void mostrarHistorialMedico() {
        if (pnlHistorialMedico == null) {
            pnlHistorialMedico = new PnlHistorialMedico();
        }
        CtrlHistorialMedico ctrlHistorial = new CtrlHistorialMedico(pnlHistorialMedico);
        main.showForm(pnlHistorialMedico);
    }
    
    /**
     * Muestra el panel de gestion de servicios.
     */
    private void mostrarServicios() {
        if (pnlServicio == null) pnlServicio = new PnlServicio();
        CtrlServicio ctrl = new CtrlServicio(pnlServicio);
        main.showForm(pnlServicio);
    }
    
    /**
     * Muestra el panel de reporte de recordatorios.
     */
    private void mostrarReporteRecordatorios() {
        if (pnlRecordatorioReporte == null) {
            pnlRecordatorioReporte = new PnlRecordatorioReporte();
        }
        main.showForm(pnlRecordatorioReporte);
    }
    
    /**
     * Muestra el dialogo de facturacion.
     */
    private void mostrarFacturacion() {
        frmSeleccionarCedula dialog = new frmSeleccionarCedula(frm);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            String cedula = dialog.getCedulaSeleccionada();
            frmFactura facturaDialog = new frmFactura(frm, cedula);
            facturaDialog.setVisible(true);
        }
    }
    
    /**
     * Ejecuta el cierre de sesion.
     */
    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(frm,
            "¿Cerrar sesion?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            NotificationManager.getInstance().stop();
            SessionManager.getInstance().logout();
            main = null;
            pnlCliente = null;
            pnlCita = null;
            pnlMascota = null;
            pnlVeterinario = null;
            pnlHistorialMedico = null;
            pnlServicio = null;
            pnlRecordatorioReporte = null; 
            bgLogin = null;
            authcontroller = null;
            cargarLogin();
        }
    }
    
    /**
     * Abre la documentacion Javadoc del sistema en el navegador web.
     */
    private void abrirDocumentacion() {
        try {
            File docFile = new File("target/site/apidocs/index.html");
            if (!docFile.exists()) {
                docFile = new File("docs/javadoc/index.html");
            }
            if (!docFile.exists()) {
                String url = "http://localhost:8080/docs/javadoc/index.html";
                Desktop.getDesktop().browse(new URI(url));
                return;
            }
            Desktop.getDesktop().browse(docFile.toURI());
        } catch (IOException | URISyntaxException ex) {
            JOptionPane.showMessageDialog(frm, 
                "No se pudo abrir la documentacion.\nError: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Muestra el dialogo "Acerca de" con informacion del proyecto.
     */
    private void mostrarAcercaDe() {
        String mensaje = """
            ============================================
            VETERINARIA PET TOWN
            ============================================
            
            Sistema de Gestion Veterinaria
            Version: 1.0.0
            Fecha de inicio: 15/04/2026
            
            DESARROLLADORES:
            --------------------------------
            • BESILLA TOMALA ANGEL KALED – MODULO: VETERINARIO
            • CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
            • CASTRO AVILA JONATHAN XAVIER – MODULO: CLIENTE
            • CHILAN CHILAN DANNY ANDRES – MODULO: AGENDAMIENTO DE CITA
            • ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
            
            TECNOLOGIAS:
            --------------------------------
            • Java 17
            • Spring Boot 3.1.5
            • SQL Server/MySQL
            • FlatLaf (UI)
            
            © 2026 - Todos los derechos reservados
            ============================================
            """;
        
        JOptionPane.showMessageDialog(frm, mensaje, "Acerca de Veterinaria Pet Town", 
            JOptionPane.INFORMATION_MESSAGE);
    }    
}