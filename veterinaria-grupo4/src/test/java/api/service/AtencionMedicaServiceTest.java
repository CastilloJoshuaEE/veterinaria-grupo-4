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
 * Pruebas unitarias para la capa de SERVICIO - AtencionMedicaService.
 *
 * <p><b>MÓDULO: ATENCIÓN VETERINARIA</b></p>
 *
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 2.0 (con inyección de dependencias)
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AtencionMedicaServiceTest {

    @Mock
    private IAtencionMedicaDAO atencionDAO;

    @Mock
    private CitaService citaService;

    private AtencionMedicaService atencionService;

    private static AtencionMedica atencionPrueba;
    private static Cita citaPrueba;
    private static final int TEST_ATENCION_ID = 1;
    private static final int TEST_CITA_ID = 100;
    private static final int ID_INVALIDO = -1;
    private static final int ID_NO_EXISTENTE = 999;

    @BeforeAll
    static void setUpClass() {
        citaPrueba = new Cita();
        citaPrueba.setIdCita(TEST_CITA_ID);
        citaPrueba.setEstado("PENDIENTE");

        atencionPrueba = new AtencionMedica();
        atencionPrueba.setIdAtencionMedica(TEST_ATENCION_ID);
        atencionPrueba.setIdCita(TEST_CITA_ID);
        atencionPrueba.setDiagnostico("Infección respiratoria");
        atencionPrueba.setTratamiento("Antibióticos por 7 días");
        atencionPrueba.setObservaciones("Requiere control en 15 días");
    }

    @BeforeEach
    void setUp() {
        atencionService = new AtencionMedicaService(atencionDAO, citaService);
        lenient().when(citaService.obtenerPorId(TEST_CITA_ID)).thenReturn(citaPrueba);
        lenient().when(citaService.obtenerPorId(ID_NO_EXISTENTE)).thenReturn(null);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // PRUEBAS PARA EL MÉTODO guardar() - COMPLEJIDAD CICLOMÁTICA M=3
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(1)
    @DisplayName("CP-GUARDAR-01: Guardar - Validación fallida (diagnóstico vacío) - ERROR")
    void testGuardarValidacionFallidaDiagnosticoVacio() throws SQLException {
        AtencionMedica atencionInvalida = new AtencionMedica();
        atencionInvalida.setIdCita(TEST_CITA_ID);
        atencionInvalida.setDiagnostico("");
        atencionInvalida.setTratamiento("Tratamiento válido con más de 3 caracteres");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            atencionService.guardar(atencionInvalida);
        });

        assertTrue(exception.getMessage().contains("El diagnostico es obligatorio"));
        verify(atencionDAO, never()).insertar(any());

        System.out.println("CP-GUARDAR-01: Diagnóstico vacío - IllegalArgumentException lanzada");
    }

    @Test
    @Order(2)
    @DisplayName("CP-GUARDAR-02: Guardar - Validación fallida (tratamiento vacío) - ERROR")
    void testGuardarValidacionFallidaTratamientoVacio() throws SQLException {
        AtencionMedica atencionInvalida = new AtencionMedica();
        atencionInvalida.setIdCita(TEST_CITA_ID);
        atencionInvalida.setDiagnostico("Diagnóstico válido con más de 3 caracteres");
        atencionInvalida.setTratamiento("");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            atencionService.guardar(atencionInvalida);
        });

        assertTrue(exception.getMessage().contains("El tratamiento es obligatorio"));
        verify(atencionDAO, never()).insertar(any());

        System.out.println("CP-GUARDAR-02: Tratamiento vacío - IllegalArgumentException lanzada");
    }

    @Test
    @Order(3)
    @DisplayName("CP-GUARDAR-03: Guardar - ID de cita inválido (≤ 0) - ERROR")
    void testGuardarIdCitaInvalido() throws SQLException {
        AtencionMedica atencionInvalida = new AtencionMedica();
        atencionInvalida.setIdCita(ID_INVALIDO);
        atencionInvalida.setDiagnostico("Diagnóstico válido");
        atencionInvalida.setTratamiento("Tratamiento válido");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            atencionService.guardar(atencionInvalida);
        });

        assertTrue(exception.getMessage().contains("Debe especificar una cita valida"));
        verify(atencionDAO, never()).insertar(any());

        System.out.println("CP-GUARDAR-03: ID de cita inválido - IllegalArgumentException lanzada");
    }

    @Test
    @Order(4)
    @DisplayName("CP-GUARDAR-04: Guardar - Cita no existe - ERROR")
    void testGuardarCitaNoExiste() throws SQLException {
        when(citaService.obtenerPorId(ID_NO_EXISTENTE)).thenReturn(null);

        AtencionMedica atencionInvalida = new AtencionMedica();
        atencionInvalida.setIdCita(ID_NO_EXISTENTE);
        atencionInvalida.setDiagnostico("Diagnóstico válido");
        atencionInvalida.setTratamiento("Tratamiento válido");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            atencionService.guardar(atencionInvalida);
        });

        assertTrue(exception.getMessage().contains("La cita con ID " + ID_NO_EXISTENTE + " no existe"));
        verify(atencionDAO, never()).insertar(any());

        System.out.println("CP-GUARDAR-04: Cita no existe - IllegalArgumentException lanzada");
    }

    @Test
    @Order(5)
    @DisplayName("CP-GUARDAR-05: Guardar - Cita cancelada - ERROR")
    void testGuardarCitaCancelada() throws SQLException {
        Cita citaCancelada = new Cita();
        citaCancelada.setIdCita(TEST_CITA_ID);
        citaCancelada.setEstado("CANCELADA");
        when(citaService.obtenerPorId(TEST_CITA_ID)).thenReturn(citaCancelada);

        // El servicio lanza IllegalStateException para cita cancelada
        assertThrows(IllegalStateException.class, () -> {
            atencionService.guardar(atencionPrueba);
        });

        verify(atencionDAO, never()).insertar(any());

        System.out.println("CP-GUARDAR-05: Cita cancelada - IllegalStateException lanzada");
    }

    @Test
    @Order(6)
    @DisplayName("CP-GUARDAR-06: Guardar - ÉXITO")
    void testGuardarExito() throws SQLException {
        when(atencionDAO.insertar(atencionPrueba)).thenReturn(TEST_ATENCION_ID);

        int resultado = atencionService.guardar(atencionPrueba);

        assertEquals(TEST_ATENCION_ID, resultado);
        verify(atencionDAO, times(1)).insertar(atencionPrueba);
        verify(citaService, times(1)).obtenerPorId(TEST_CITA_ID);

        System.out.println("CP-GUARDAR-06: Guardado exitoso de atención médica - ID: " + resultado);
    }

    @Test
    @Order(7)
    @DisplayName("CP-GUARDAR-07: Guardar - Error SQL - ERROR")
    void testGuardarErrorSQL() throws SQLException {
        when(atencionDAO.insertar(atencionPrueba)).thenThrow(new SQLException("Error de conexión"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            atencionService.guardar(atencionPrueba);
        });

        assertTrue(exception.getMessage().contains("Error al guardar la atencion medica"));
        verify(atencionDAO, times(1)).insertar(atencionPrueba);

        System.out.println("CP-GUARDAR-07: Error SQL capturado correctamente");
    }

    // ═══════════════════════════════════════════════════════════════════════
    // PRUEBAS PARA EL MÉTODO obtenerPorId()
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(8)
    @DisplayName("CP-OBTENER-01: Obtener por ID - ID inválido (≤ 0) - ERROR")
    void testObtenerPorIdInvalido() throws SQLException {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            atencionService.obtenerPorId(ID_INVALIDO);
        });

        assertTrue(exception.getMessage().contains("ID de atencion medica invalido"));
        verify(atencionDAO, never()).obtenerPorId(anyInt());

        System.out.println("CP-OBTENER-01: ID inválido - IllegalArgumentException lanzada");
    }

    @Test
    @Order(9)
    @DisplayName("CP-OBTENER-02: Obtener por ID - ÉXITO")
    void testObtenerPorIdExito() throws SQLException {
        when(atencionDAO.obtenerPorId(TEST_ATENCION_ID)).thenReturn(atencionPrueba);

        AtencionMedica resultado = atencionService.obtenerPorId(TEST_ATENCION_ID);

        assertNotNull(resultado);
        assertEquals(TEST_ATENCION_ID, resultado.getIdAtencionMedica());
        verify(atencionDAO, times(1)).obtenerPorId(TEST_ATENCION_ID);

        System.out.println("CP-OBTENER-02: Obtención exitosa de atención médica");
    }
}