package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.model.entity.Estado;
import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
import com.mycompany.veterinaria.grupo4.view.mascota.PnlMascota;
import com.mycompany.veterinaria.grupo4.view.swing.table.ModelAction;
import com.mycompany.veterinaria.grupo4.view.swing.table.ModelProfile;
import com.mycompany.veterinaria.grupo4.view.swing.table.Table;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class CtrlMascotas {

    private PnlMascota pnlMascota;
    private Table tblMascota;

    // ── API ─────────────────────────────────────────────────────
    private RestTemplate restTemplate = new RestTemplate();
    private String apiBaseUrl = "http://localhost:8080/api";

    // ── Ícono por defecto cuando no hay foto ────────────────────
    private final ImageIcon iconoDefault = new ImageIcon(
        getClass().getResource("/icon/pet-paw.png")
    );

    // ── Mascota seleccionada actualmente ────────────────────────
    private Mascota mascotaSeleccionada;

    public CtrlMascotas(PnlMascota pnlMascota) {
        this.pnlMascota = pnlMascota;
        this.tblMascota = pnlMascota.getTblMascota();
        initTabla();
        cargarTabla();
        addListeners();
    }

    // ── Inicializar columnas de la tabla ────────────────────────
    private void initTabla() {
        tblMascota.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"Mascota", "Especie", "Raza", "Sexo", "Peso", "Estado", "Acción"}
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 6; // solo Acción
            }
        });

        tblMascota.getColumnModel().getColumn(0).setPreferredWidth(180); // Mascota (foto+nombre)
        tblMascota.getColumnModel().getColumn(1).setPreferredWidth(80);  // Especie
        tblMascota.getColumnModel().getColumn(2).setPreferredWidth(80);  // Raza
        tblMascota.getColumnModel().getColumn(3).setPreferredWidth(60);  // Sexo
        tblMascota.getColumnModel().getColumn(4).setPreferredWidth(60);  // Peso
        tblMascota.getColumnModel().getColumn(5).setPreferredWidth(90);  // Estado
        tblMascota.getColumnModel().getColumn(6).setPreferredWidth(120); // Acción

        tblMascota.fixTable(pnlMascota.getScrollPane());
    }

    // ── Listeners ───────────────────────────────────────────────
    private void addListeners() {
        pnlMascota.getBtnBuscar().addActionListener(e -> buscar());
        pnlMascota.getBtnNuevo().addActionListener(e -> nuevo());
    }

    // ── Cargar tabla con todas las mascotas ─────────────────────
    private void cargarTabla() {
        try {
            ResponseEntity<List<Mascota>> response = restTemplate.exchange(
                apiBaseUrl + "/mascota/listar",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Mascota>>() {}
            );
            llenarTabla(response.getBody());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnlMascota,
                "Error al cargar mascotas: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Rellena la tabla con una lista de mascotas ──────────────
    private void llenarTabla(List<Mascota> mascotas) {
        DefaultTableModel model = (DefaultTableModel) tblMascota.getModel();
        model.setRowCount(0);
        if (mascotas == null) return;

        for (Mascota m : mascotas) {
            // ── Foto: intenta cargar desde API, si falla usa ícono default
            System.out.println(m.getFoto() == null);
            ImageIcon foto = new ImageIcon(m.getFoto());
           
            ModelProfile perfil = new ModelProfile(foto, m.getNombre());

            tblMascota.addRow(new Object[]{
                perfil,
                m.getEspecie(),
                m.getRaza(),
                m.getSexo() == 'M' ? "Macho" : "Hembra",
                m.getPeso() != null ? m.getPeso() + " kg" : "-",
                Estado.ACTIVO,
                new ModelAction(
                    () -> editar(m),
                    () -> ver(m),
                    () -> eliminar(m)
                )
            });
        }
    }

    // ── Obtiene la foto de la mascota desde la API ──────────────
  

    // ── Búsqueda por nombre ─────────────────────────────────────
    private void buscar() {
        String texto = pnlMascota.getTxtBusqueda().getText().trim();
        if (texto.isEmpty()) {
            cargarTabla();
            return;
        }
        try {
            ResponseEntity<List<Mascota>> response = restTemplate.exchange(
                apiBaseUrl + "/mascota/buscar?nombre=" + texto,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Mascota>>() {}
            );
            llenarTabla(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ── Acciones de la tabla ─────────────────────────────────────
    private void nuevo() {
        // TODO: abrir formulario de nueva mascota
        System.out.println("Nueva mascota");
    }

    private void ver(Mascota m) {
        // TODO: abrir panel de detalle
        System.out.println("Ver: " + m.getNombre());
    }

    private void editar(Mascota m) {
        // TODO: abrir formulario con datos de m
        System.out.println("Editar: " + m.getNombre());
    }

    private void eliminar(Mascota m) {
        int confirm = JOptionPane.showConfirmDialog(pnlMascota,
            "¿Eliminar a " + m.getNombre() + "?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                restTemplate.delete(apiBaseUrl + "/mascota/eliminar/" + m.getIdMascota());
                cargarTabla();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(pnlMascota,
                    "Error al eliminar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ── Guardar/Actualizar (viene del formulario) ────────────────
    public void guardar(Mascota m, byte[] fotoBytes, String nombreArchivo) {
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("idCliente",  m.getIdCliente());//Corregir
            body.add("nombre",     m.getNombre());
            body.add("especie",    m.getEspecie());
            body.add("raza",       m.getRaza());
            body.add("sexo",       String.valueOf(m.getSexo()));
            body.add("peso",       m.getPeso());
            body.add("color",      m.getColor());

            if (fotoBytes != null) {
                body.add("foto", new org.springframework.core.io.ByteArrayResource(fotoBytes) {
                    @Override public String getFilename() { return nombreArchivo; }
                });
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

            if (m.getIdMascota() == 0) {
                // Nueva mascota
                restTemplate.postForObject(apiBaseUrl + "/mascota/crear", request, Integer.class);
                JOptionPane.showMessageDialog(pnlMascota, "Mascota registrada correctamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Actualizar
                body.add("idMascota", m.getIdMascota());
                restTemplate.put(apiBaseUrl + "/mascota/actualizar", request);
                JOptionPane.showMessageDialog(pnlMascota, "Mascota actualizada correctamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
            cargarTabla();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnlMascota,
                "Error al guardar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Guardar ficha médica ─────────────────────────────────────
    public void guardarFichaMedica(int idMascota, String alergias,
            String enfermedades, String observaciones) {
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("idMascota",            idMascota);
            body.add("alergias",             alergias);
            body.add("enfermedadesCronicas", enfermedades);
            body.add("observaciones",        observaciones);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

            restTemplate.postForObject(apiBaseUrl + "/mascota/ficha/actualizar", request, Boolean.class);
            JOptionPane.showMessageDialog(pnlMascota, "Ficha médica guardada correctamente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnlMascota,
                "Error al guardar ficha: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
