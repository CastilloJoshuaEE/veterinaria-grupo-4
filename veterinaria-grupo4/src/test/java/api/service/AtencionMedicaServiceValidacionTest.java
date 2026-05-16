package api.service;

import com.mycompany.veterinaria.grupo4.model.dao.IAtencionMedicaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.AtencionMedica;
import com.mycompany.veterinaria.grupo4.model.entity.Cita;
import com.mycompany.veterinaria.grupo4.service.AtencionMedicaService;
import com.mycompany.veterinaria.grupo4.service.CitaService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para validarAtencionMedica() a través de guardar().
 *
 * <p><b>MÓDULO: ATENCIÓN VETERINARIA</b></p>
 *
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 2.0
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AtencionMedicaServiceValidacionTest {

    // ── Mocks declarados con @Mock (Mockito los inicializa) ──────────────────
    @Mock
    private IAtencionMedicaDAO atencionDAO;

    @Mock
    private CitaService citaService;

    private AtencionMedicaService atencionService;

    private static Cita citaPrueba;
    private static final int TEST_CITA_ID = 100;

    @BeforeAll
    static void setUpClass() {
        citaPrueba = new Cita();
        citaPrueba.setIdCita(TEST_CITA_ID);
        citaPrueba.setEstado("PENDIENTE");
    }

    @BeforeEach
    void setUp() {
        atencionService = new AtencionMedicaService(atencionDAO, citaService);

        lenient().when(citaService.obtenerPorId(TEST_CITA_ID)).thenReturn(citaPrueba);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // PRUEBAS PARA validarAtencionMedica() - COMPLEJIDAD CICLOMÁTICA M=6
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(1)
    @DisplayName("CP-VAL-ATENCION-01: Validar - Atención nula - ERROR")
    void testValidarAtencionNula() throws SQLException {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            atencionService.guardar(null);
        });

        assertTrue(exception.getMessage().contains("La atencion medica no puede ser nula"));
        // "throws SQLException" en la firma permite que el compilador acepte esto:
        verify(atencionDAO, never()).insertar(any());

        System.out.println("CP-VAL-ATENCION-01: Atención nula - IllegalArgumentException lanzada");
    }

    @Test
    @Order(2)
    @DisplayName("CP-VAL-ATENCION-02: Validar - Diagnóstico nulo - ERROR")
    void testValidarDiagnosticoNulo() throws SQLException {
        AtencionMedica atencionInvalida = new AtencionMedica();
        atencionInvalida.setIdCita(TEST_CITA_ID);
        atencionInvalida.setDiagnostico(null);
        atencionInvalida.setTratamiento("Tratamiento válido con más de 3 caracteres");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            atencionService.guardar(atencionInvalida);
        });

        assertTrue(exception.getMessage().contains("El diagnostico es obligatorio"));
        verify(atencionDAO, never()).insertar(any());

        System.out.println("CP-VAL-ATENCION-02: Diagnóstico nulo - IllegalArgumentException lanzada");
    }

    @Test
    @Order(3)
    @DisplayName("CP-VAL-ATENCION-03: Validar - Diagnóstico vacío - ERROR")
    void testValidarDiagnosticoVacio() throws SQLException {
        AtencionMedica atencionInvalida = new AtencionMedica();
        atencionInvalida.setIdCita(TEST_CITA_ID);
        atencionInvalida.setDiagnostico("");
        atencionInvalida.setTratamiento("Tratamiento válido con más de 3 caracteres");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            atencionService.guardar(atencionInvalida);
        });

        assertTrue(exception.getMessage().contains("El diagnostico es obligatorio"));
        verify(atencionDAO, never()).insertar(any());

        System.out.println("CP-VAL-ATENCION-03: Diagnóstico vacío - IllegalArgumentException lanzada");
    }

    @Test
    @Order(4)
    @DisplayName("CP-VAL-ATENCION-04: Validar - Diagnóstico muy corto (menos de 3 caracteres) - ERROR")
    void testValidarDiagnosticoMuyCorto() throws SQLException {
        AtencionMedica atencionInvalida = new AtencionMedica();
        atencionInvalida.setIdCita(TEST_CITA_ID);
        atencionInvalida.setDiagnostico("AB");
        atencionInvalida.setTratamiento("Tratamiento válido con más de 3 caracteres");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            atencionService.guardar(atencionInvalida);
        });

        assertTrue(exception.getMessage().contains("El diagnostico debe tener al menos 3 caracteres"));
        verify(atencionDAO, never()).insertar(any());

        System.out.println("CP-VAL-ATENCION-04: Diagnóstico muy corto - IllegalArgumentException lanzada");
    }

    @Test
    @Order(5)
    @DisplayName("CP-VAL-ATENCION-05: Validar - Tratamiento nulo - ERROR")
    void testValidarTratamientoNulo() throws SQLException {
        AtencionMedica atencionInvalida = new AtencionMedica();
        atencionInvalida.setIdCita(TEST_CITA_ID);
        atencionInvalida.setDiagnostico("Diagnóstico válido con más de 3 caracteres");
        atencionInvalida.setTratamiento(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            atencionService.guardar(atencionInvalida);
        });

        assertTrue(exception.getMessage().contains("El tratamiento es obligatorio"));
        verify(atencionDAO, never()).insertar(any());

        System.out.println("CP-VAL-ATENCION-05: Tratamiento nulo - IllegalArgumentException lanzada");
    }

    @Test
    @Order(6)
    @DisplayName("CP-VAL-ATENCION-06: Validar - Tratamiento vacío - ERROR")
    void testValidarTratamientoVacio() throws SQLException {
        AtencionMedica atencionInvalida = new AtencionMedica();
        atencionInvalida.setIdCita(TEST_CITA_ID);
        atencionInvalida.setDiagnostico("Diagnóstico válido con más de 3 caracteres");
        atencionInvalida.setTratamiento("");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            atencionService.guardar(atencionInvalida);
        });

        assertTrue(exception.getMessage().contains("El tratamiento es obligatorio"));
        verify(atencionDAO, never()).insertar(any());

        System.out.println("CP-VAL-ATENCION-06: Tratamiento vacío - IllegalArgumentException lanzada");
    }

    @Test
    @Order(7)
    @DisplayName("CP-VAL-ATENCION-07: Validar - Tratamiento muy corto (menos de 3 caracteres) - ERROR")
    void testValidarTratamientoMuyCorto() throws SQLException {
        AtencionMedica atencionInvalida = new AtencionMedica();
        atencionInvalida.setIdCita(TEST_CITA_ID);
        atencionInvalida.setDiagnostico("Diagnóstico válido con más de 3 caracteres");
        atencionInvalida.setTratamiento("XY");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            atencionService.guardar(atencionInvalida);
        });

        assertTrue(exception.getMessage().contains("El tratamiento debe tener al menos 3 caracteres"));
        verify(atencionDAO, never()).insertar(any());

        System.out.println("CP-VAL-ATENCION-07: Tratamiento muy corto - IllegalArgumentException lanzada");
    }

    @Test
    @Order(8)
    @DisplayName("CP-VAL-ATENCION-08: Validar - Atención válida (diagnóstico y tratamiento correctos) - ÉXITO")
    void testValidarAtencionValida() throws SQLException {
        AtencionMedica atencionValida = new AtencionMedica();
        atencionValida.setIdCita(TEST_CITA_ID);
        atencionValida.setDiagnostico("Infección respiratoria aguda");
        atencionValida.setTratamiento("Amoxicilina 500mg cada 12h por 7 días");

        when(atencionDAO.insertar(atencionValida)).thenReturn(1);

        int resultado = atencionService.guardar(atencionValida);

        assertTrue(resultado > 0);
        verify(atencionDAO, times(1)).insertar(atencionValida);

        System.out.println("CP-VAL-ATENCION-08: Atención válida - Validación exitosa");
    }
}