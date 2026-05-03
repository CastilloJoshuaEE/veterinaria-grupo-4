package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import com.mycompany.veterinaria.grupo4.model.entity.Estado;
import com.mycompany.veterinaria.grupo4.view.cliente.FormRegistroCliente;
import com.mycompany.veterinaria.grupo4.view.cliente.PnlCliente;
import com.mycompany.veterinaria.grupo4.view.swing.table.ModelAction;
import com.mycompany.veterinaria.grupo4.view.swing.table.Table;
import java.awt.Frame;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
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
        FormRegistroCliente form = new FormRegistroCliente(
            (Frame) SwingUtilities.getWindowAncestor(pnlCliente)
        );
        form.getBtnAccion().addActionListener(e -> {
            String error = validarDatos(form);
            if (error != null) {
                JOptionPane.showMessageDialog(form, error, "Validación",
                    JOptionPane.WARNING_MESSAGE);
                return;      
            }
            registrar(getCliente(form));
            form.dispose();
        });
        form.setVisible(true);
    }

    private void editar(Cliente c) {
        FormRegistroCliente form = new FormRegistroCliente(
            (Frame) SwingUtilities.getWindowAncestor(pnlCliente), c
        );
        form.getBtnAccion().addActionListener(e -> {
            String error = validarDatos(form);
            if (error != null) {
                JOptionPane.showMessageDialog(form, error, "Validación",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            actualizar(getCliente(form));
            form.dispose();
            
        });
        form.setVisible(true);
    }

    private void ver(Cliente c) {
        // TODO: abrir panel de detalle
        System.out.println("Ver: " + c.getNombre());
    }
    
    // ── Llamadas a la API  ──────────────────────────────
private void registrar(Cliente c) {
    try {
        restTemplate.postForObject(apiBaseUrl + "/crear",c, Boolean.class);
        
        cargarTabla();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(pnlCliente,
            "Error al registrar: " + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void actualizar(Cliente c) {
    try {
        restTemplate.put(apiBaseUrl + "/actualizar", c);
        cargarTabla();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(pnlCliente,
            "Error al actualizar: " + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
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

    public String validarDatos(FormRegistroCliente form) {
        if (form.getTxtCedula().getText().trim().isEmpty())
            return "Ingrese la cédula";
        if (form.getTxtCedula().getText().trim().length() != 10)
            return "La cédula debe tener 10 dígitos";
        if (!form.getTxtCedula().getText().trim().matches("\\d+"))
            return "La cédula debe contener solo números";
        if (form.getTxtNombre().getText().trim().isEmpty())
            return "Ingrese el nombre";
        if (form.getTxtApellido().getText().trim().isEmpty())
            return "Ingrese el apellido";
        if (form.getTxtTelefono().getText().trim().isEmpty())
            return "Ingrese el teléfono";
        if (form.getTxtTelefono().getText().trim().length() != 10)
            return "El teléfono debe tener 10 dígitos";
        if (!form.getTxtTelefono().getText().trim().matches("\\d+"))
            return "El teléfono debe contener solo números";
        if (form.getTxtEmail().getText().trim().isEmpty())
            return "Ingrese el email";
        if (!form.getTxtEmail().getText().trim().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
            return "Ingrese un email válido";
        return null; // null = todo OK
    }
    
    public Cliente getCliente(FormRegistroCliente form) {
        Cliente c = form.getClienteActual() != null ? form.getClienteActual()  : new Cliente();
        c.setCedula(form.getTxtCedula().getText().trim());
        c.setNombre(form.getTxtNombre().getText().trim());
        c.setApellido(form.getTxtApellido().getText().trim());
        c.setTelefono(form.getTxtTelefono().getText().trim());
        c.setCorreoElectronico(form.getTxtEmail().getText().trim());
        c.setDireccion(form.getTxtDireccion().getText().trim());
        return c;
    }
}