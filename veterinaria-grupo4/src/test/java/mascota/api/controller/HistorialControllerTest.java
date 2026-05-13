package mascota.api.controller;

import com.mycompany.veterinaria.grupo4.api.controller.HistorialController;
import com.mycompany.veterinaria.grupo4.model.entity.HistorialMedico;
import com.mycompany.veterinaria.grupo4.service.HistorialService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
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

    // PRUEBAS PARA EL MÉTODO cargarHistorialMedico() - COMPLEJIDAD CICLOMÁTICA M=4
    // Requiere 4 casos de prueba (uno por cada camino independiente)

    @Test
    @Order(1)
    @DisplayName("CP-HIST-01: Cargar historial - Mascota SIN historial (ÉXITO - Camino 1)")
    void testCargarHistorialMascotaSinHistorial() {
        // Arrange: mascota existe pero NO tiene historial médico
        List<HistorialMedico> historialVacio = new ArrayList<>();
        when(historialService.obtenerPorMascota(TEST_MASCOTA_ID)).thenReturn(historialVacio);

        // Act
        List<HistorialMedico> resultado = historialController.obtenerPorMascota(TEST_MASCOTA_ID);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty(), "La lista debe estar vacía para mascota sin historial");
        verify(historialService, times(1)).obtenerPorMascota(TEST_MASCOTA_ID);
        
        System.out.println("CP-HIST-01: Mascota sin historial - tabla vacía");
    }

    @Test
    @Order(2)
    @DisplayName("CP-HIST-02: Cargar historial - Mascota CON historial completo (ÉXITO - Camino 2)")
    void testCargarHistorialMascotaConHistorial() {
        // Arrange: mascota existe y TIENE historial médico
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
        
        System.out.println("CP-HIST-02: Historial cargado correctamente con " + resultado.size() + " registros");
    }

    @Test
    @Order(3)
    @DisplayName("CP-HIST-03: Cargar historial - Registro SIN instrumentos/medicamentos (ÉXITO - Camino 3)")
    void testCargarHistorialSinInstrumentosNiMedicamentos() {
        // Arrange: historial con instrumentos y medicamentos = null (deben mostrar "-")
        HistorialMedico historialSinDatos = new HistorialMedico();
        historialSinDatos.setIdHistorial(20);
        historialSinDatos.setInstrumentosUsados(null);  // Debe mostrar "-"
        historialSinDatos.setMedicamentosRecetados(null); // Debe mostrar "-"
        historialSinDatos.setDiagnostico("Diagnóstico de prueba");
        historialSinDatos.setTratamiento("Tratamiento de prueba");
        
        List<HistorialMedico> historialEsperado = Arrays.asList(historialSinDatos);
        when(historialService.obtenerPorMascota(TEST_MASCOTA_ID)).thenReturn(historialEsperado);

        // Act
        List<HistorialMedico> resultado = historialController.obtenerPorMascota(TEST_MASCOTA_ID);

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        
        HistorialMedico primerRegistro = resultado.get(0);
        // En la UI se mostrará "-", en el objeto puede ser null o vacío
        assertNull(primerRegistro.getInstrumentosUsados());
        assertNull(primerRegistro.getMedicamentosRecetados());
        
        verify(historialService, times(1)).obtenerPorMascota(TEST_MASCOTA_ID);
        
        System.out.println("CP-HIST-03: Historial sin instrumentos/medicamentos - muestra '-' en UI");
    }

    @Test
    @Order(4)
    @DisplayName("CP-HIST-04: Cargar historial - ERROR de conexión (ERROR - Camino 4)")
    void testCargarHistorialErrorConexion() {
        when(historialService.obtenerPorMascota(TEST_MASCOTA_ID))
            .thenThrow(new RuntimeException("Error de conexión con el servidor"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            historialController.obtenerPorMascota(TEST_MASCOTA_ID);
        });
        
        assertTrue(exception.getMessage().contains("Error de conexión"));
        verify(historialService, times(1)).obtenerPorMascota(TEST_MASCOTA_ID);
        
        System.out.println("CP-HIST-04: Error de conexión capturado correctamente");
    }

}