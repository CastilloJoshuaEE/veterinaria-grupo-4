package mascota.api.controller;

import com.mycompany.veterinaria.grupo4.api.controller.HistorialController;
import com.mycompany.veterinaria.grupo4.model.entity.HistorialMedico;
import com.mycompany.veterinaria.grupo4.service.HistorialService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la capa de CONTROL - HistorialController.
 * 
 * <p><b>Requerimientos Funcionales involucrados:</b></p>
 * <ul>
 *   <li>RF-11: Consultar historial médico de una mascota</li>
 * </ul>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HistorialControllerTest {

    @Mock
    private HistorialService historialService;

    @InjectMocks
    private HistorialController historialController;

    private static HistorialMedico historialPrueba;
    private static final int TEST_MASCOTA_ID = 1;
    private static final int TEST_HISTORIAL_ID = 10;

    @BeforeAll
    static void setUpClass() {
        historialPrueba = new HistorialMedico();
        historialPrueba.setIdHistorial(TEST_HISTORIAL_ID);
        historialPrueba.setIdMascota(TEST_MASCOTA_ID);
        historialPrueba.setIdCita(5);
        historialPrueba.setIdAtencionMedica(3);
        historialPrueba.setFecha(new Date());
        historialPrueba.setNombreServicio("Consulta General");
        historialPrueba.setNombreVeterinario("Dr. Juan Pérez");
        historialPrueba.setDiagnostico("Infección respiratoria");
        historialPrueba.setTratamiento("Antibióticos por 7 días");
        historialPrueba.setInstrumentosUsados("Estetoscopio, Termómetro");
        historialPrueba.setMedicamentosRecetados("Amoxicilina (500mg, c/12h, 7 días)");
    }

    // ═══════════════════════════════════════════════════════════════════════
    // CASO DE ÉXITO: OBTENER HISTORIAL POR MASCOTA EXISTENTE
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(1)
    @DisplayName("CP-CTRL-09: Obtener historial por mascota existente - ÉXITO")
    void testObtenerPorMascotaExito() {
        // Arrange
        List<HistorialMedico> historialEsperado = Arrays.asList(historialPrueba);
        when(historialService.obtenerPorMascota(TEST_MASCOTA_ID)).thenReturn(historialEsperado);

        // Act
        List<HistorialMedico> resultado = historialController.obtenerPorMascota(TEST_MASCOTA_ID);

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(TEST_HISTORIAL_ID, resultado.get(0).getIdHistorial());
        assertEquals("Consulta General", resultado.get(0).getNombreServicio());
        assertNotNull(resultado.get(0).getInstrumentosUsados());
        assertNotNull(resultado.get(0).getMedicamentosRecetados());
        verify(historialService, times(1)).obtenerPorMascota(TEST_MASCOTA_ID);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // CASO DE ERROR: OBTENER HISTORIAL POR MASCOTA INEXISTENTE
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(2)
    @DisplayName("CP-CTRL-10: Obtener historial por mascota inexistente - ERROR (lista vacía)")
    void testObtenerPorMascotaInexistente() {
        // Arrange
        int mascotaInexistente = -1;
        when(historialService.obtenerPorMascota(mascotaInexistente)).thenReturn(Arrays.asList());

        // Act
        List<HistorialMedico> resultado = historialController.obtenerPorMascota(mascotaInexistente);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(historialService, times(1)).obtenerPorMascota(mascotaInexistente);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // CASO DE ÉXITO: OBTENER HISTORIAL CON INSTRUMENTOS Y MEDICAMENTOS
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(3)
    @DisplayName("CP-CTRL-11: Obtener historial con instrumentos y medicamentos - ÉXITO")
    void testObtenerHistorialConInstrumentosYMedicamentos() {
        // Arrange
        HistorialMedico historialCompleto = new HistorialMedico();
        historialCompleto.setIdHistorial(20);
        historialCompleto.setInstrumentosUsados("Jeringa, Bisturí, Suturas");
        historialCompleto.setMedicamentosRecetados("Metronidazol (1, 1, 8)");
        
        List<HistorialMedico> historialEsperado = Arrays.asList(historialCompleto);
        when(historialService.obtenerPorMascota(TEST_MASCOTA_ID)).thenReturn(historialEsperado);

        // Act
        List<HistorialMedico> resultado = historialController.obtenerPorMascota(TEST_MASCOTA_ID);

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        
        HistorialMedico primerRegistro = resultado.get(0);
        assertNotNull(primerRegistro.getInstrumentosUsados());
        assertNotNull(primerRegistro.getMedicamentosRecetados());
        assertTrue(primerRegistro.getInstrumentosUsados().contains("Jeringa"));
        assertTrue(primerRegistro.getMedicamentosRecetados().contains("Metronidazol"));
        
        verify(historialService, times(1)).obtenerPorMascota(TEST_MASCOTA_ID);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // CASO DE ERROR: REGISTRAR HISTORIAL CON MASCOTA INEXISTENTE
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(4)
    @DisplayName("CP-CTRL-12: Registrar historial con mascota inexistente - ERROR")
    void testRegistrarHistorialMascotaInexistente() {
        // Arrange
        int mascotaInexistente = -1;
        when(historialService.registrar(eq(mascotaInexistente), any(), any())).thenReturn(false);

        // Act
        boolean resultado = historialController.registrar(mascotaInexistente, null, null);

        // Assert
        assertFalse(resultado);
        verify(historialService, times(1)).registrar(eq(mascotaInexistente), isNull(), isNull());
    }

    // ═══════════════════════════════════════════════════════════════════════
    // CASO DE ÉXITO: REGISTRAR HISTORIAL (RARAMENTE USADO, PERO SE PRUEBA)
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(5)
    @DisplayName("CP-CTRL-13: Registrar historial para mascota existente - ÉXITO")
    void testRegistrarHistorialExito() {
        // Arrange
        when(historialService.registrar(eq(TEST_MASCOTA_ID), eq(5), eq(3))).thenReturn(true);

        // Act
        boolean resultado = historialController.registrar(TEST_MASCOTA_ID, 5, 3);

        // Assert
        assertTrue(resultado);
        verify(historialService, times(1)).registrar(eq(TEST_MASCOTA_ID), eq(5), eq(3));
    }
}