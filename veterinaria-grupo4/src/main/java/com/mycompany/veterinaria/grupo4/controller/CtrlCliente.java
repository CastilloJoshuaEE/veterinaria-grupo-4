package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import com.mycompany.veterinaria.grupo4.model.entity.Estado;
import com.mycompany.veterinaria.grupo4.view.cliente.PnlCliente;
import com.mycompany.veterinaria.grupo4.view.swing.table.ModelAction;
import com.mycompany.veterinaria.grupo4.view.swing.table.Table;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class CtrlCliente {

    private PnlCliente pnlCliente;
    private Table tblCliente;

    // ── API ─────────────────────────────────────────────────────
    private RestTemplate restTemplate = new RestTemplate();
    private String apiBaseUrl = "http://localhost:8080/api/cliente";

    // ── Cliente seleccionado actualmente ────────────────────────
    private Cliente clienteSeleccionado;
    

    public CtrlCliente(PnlCliente pnlCliente) {
        this.pnlCliente = pnlCliente;
        this.tblCliente = pnlCliente.getTblCliente();
        cargarTabla();
        addListeners();
    }

    // ── Listeners de botones del panel ──────────────────────────
    private void addListeners() {
        pnlCliente.getBtnBuscar().addActionListener(e -> buscar());
        pnlCliente.getBtnNuevo().addActionListener(e -> nuevo());
    }

    // ── Carga todos los clientes en la tabla ────────────────────
    private void cargarTabla() {
        try {
            ResponseEntity<List<Cliente>> response = restTemplate.exchange(
                apiBaseUrl + "/listar",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Cliente>>() {}
            );
            List<Cliente> clientes = response.getBody();
            DefaultTableModel model = (DefaultTableModel) tblCliente.getModel();
            model.setRowCount(0); // limpiar antes de llenar
            
                tblCliente.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nombre", "Cédula", "Teléfono", "Email", "Estado", "Acción"}
            ) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return col == 6; // TableCellAction lo maneja internamente
                }
            });

            tblCliente.getColumnModel().getColumn(0).setMaxWidth(50);  // ID pequeño
            tblCliente.getColumnModel().getColumn(5).setPreferredWidth(90);  // Estado
            tblCliente.getColumnModel().getColumn(6).setPreferredWidth(120); // Acción

            if (clientes != null) {
                for (Cliente c : clientes) {
                    tblCliente.addRow(new Object[]{
                        c.getIdCliente(),
                        c.getNombre() + " " + c.getApellido(),
                        c.getCedula(),
                        c.getTelefono(),
                        c.getCorreoElectronico(),
                        Estado.ACTIVO,
                        new ModelAction(
                            () -> editar(c),
                            () -> ver(c),
                            () -> eliminar(c)
                        )
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnlCliente,
                "Error al cargar clientes: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Búsqueda por nombre ─────────────────────────────────────
    private void buscar() {
        String texto = pnlCliente.getTxtBusqueda().getText().trim();
        if (texto.isEmpty()) {
            cargarTabla(); // si está vacío recarga todo
            return;
        }
        try {
            ResponseEntity<List<Cliente>> response = restTemplate.exchange(
                apiBaseUrl + "/buscar/nombre?nombre=" + texto,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Cliente>>() {}
            );
            List<Cliente> clientes = response.getBody();
            DefaultTableModel model = (DefaultTableModel) pnlCliente.getTblCliente().getModel();
            model.setRowCount(0);
            if (clientes != null) {
                for (Cliente c : clientes) {
                    pnlCliente.getTblCliente().addRow(new Object[]{
                        c.getIdCliente(),
                        c.getNombre() + " " + c.getApellido(),
                        c.getCedula(),
                        c.getTelefono(),
                        c.getCorreoElectronico(),
                        Estado.ACTIVO,
                        new ModelAction(
                            () -> editar(c),
                            () -> ver(c),
                            () -> eliminar(c)
                        )
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ── Acciones de la tabla ─────────────────────────────────────
    private void nuevo() {
        // TODO: abrir formulario de registro
        System.out.println("Nuevo cliente");
    }

    private void editar(Cliente c) {
        // TODO: abrir formulario con datos de c
        System.out.println("Editar: " + c.getNombre());
    }

    private void ver(Cliente c) {
        // TODO: abrir panel de detalle
        System.out.println("Ver: " + c.getNombre());
    }

    private void eliminar(Cliente c) {
        int confirm = JOptionPane.showConfirmDialog(pnlCliente,
            "¿Eliminar a " + c.getNombre() + " " + c.getApellido() + "?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                restTemplate.delete(apiBaseUrl + "/eliminar/" + c.getIdCliente());
                cargarTabla(); // recarga la tabla tras eliminar
            } catch (Exception e) {
                JOptionPane.showMessageDialog(pnlCliente,
                    "Error al eliminar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}