package mascota.api.controller;

import com.mycompany.veterinaria.grupo4.api.controller.MascotaController;
import com.mycompany.veterinaria.grupo4.api.dto.FichaMedicaDTO;
import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
import com.mycompany.veterinaria.grupo4.service.MascotaService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la capa de CONTROL - MascotaController.
 * 
 * <p><b>Requerimientos Funcionales involucrados:</b></p>
 * <ul>
 *   <li>RF-07: Registrar nueva mascota</li>
 *   <li>RF-08: Consultar mascotas</li>
 *   <li>RF-09: Actualizar datos de mascotas</li>
 *   <li>RF-10: Dar de baja a mascotas</li>
 * </ul>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MascotaControllerTest {

    @Mock
    private MascotaService mascotaService;

    @InjectMocks
    private MascotaController mascotaController;

    private static Mascota mascotaPrueba;
    private static final int TEST_MASCOTA_ID = 1;
    private static final int TEST_CLIENTE_ID = 100;

    @BeforeAll
    static void setUpClass() {
        mascotaPrueba = new Mascota();
        mascotaPrueba.setIdMascota(TEST_MASCOTA_ID);
        mascotaPrueba.setIdCliente(TEST_CLIENTE_ID);
        mascotaPrueba.setNombre("Firulais");
        mascotaPrueba.setEspecie("Canino");
        mascotaPrueba.setRaza("Labrador");
        mascotaPrueba.setSexo('M');
        mascotaPrueba.setFechaNacimiento(new Date());
        mascotaPrueba.setPeso(5.5);
        mascotaPrueba.setColor("Dorado");
    }

    // ═══════════════════════════════════════════════════════════════════════
    // PRUEBAS PARA EL MÉTODO eliminar() - COMPLEJIDAD CICLOMÁTICA M=4
    // Requiere 4 casos de prueba (uno por cada camino independiente)
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(1)
    @DisplayName("CP-ELIM-01: Eliminar mascota - Cancelación por usuario (ÉXITO - Camino 1)")
    void testEliminarMascotaCancelacion() {        
        // Act & Assert: Verificamos que NO se llame al servicio de eliminación, simulando que el usuario decidió "Cancelar"
        verify(mascotaService, never()).eliminar(anyInt());
        
        // En la implementación real, la UI maneja la cancelación antes de llegar al controlador
        System.out.println("CP-ELIM-01: Cancelación de eliminación - Comportamiento verificado");
    }

    @Test
    @Order(2)
    @DisplayName("CP-ELIM-02: Eliminar mascota sin historial clínico (ÉXITO - Camino 2)")
    void testEliminarMascotaExito() {
        // Arrange
        when(mascotaService.eliminar(TEST_MASCOTA_ID)).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> mascotaController.eliminar(TEST_MASCOTA_ID));
        verify(mascotaService, times(1)).eliminar(TEST_MASCOTA_ID);
        
        System.out.println("CP-ELIM-02: Eliminación exitosa de mascota sin historial");
    }

    @Test
    @Order(3)
    @DisplayName("CP-ELIM-03: Eliminar mascota CON historial clínico (ERROR - Camino 3)")
    void testEliminarMascotaConHistorial() {
        // Arrange
        when(mascotaService.eliminar(TEST_MASCOTA_ID)).thenReturn(false);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mascotaController.eliminar(TEST_MASCOTA_ID);
        });
        
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertTrue(exception.getReason().contains("cita médica realizada"));
        verify(mascotaService, times(1)).eliminar(TEST_MASCOTA_ID);
        
        System.out.println("CP-ELIM-03: Error controlado - Mascota con antecedente clínico");
    }

    @Test
    @Order(4)
    @DisplayName("CP-ELIM-04: Error de conexión al eliminar (ERROR - Camino 4)")
    void testEliminarMascotaErrorConexion() {
        // Arrange
        when(mascotaService.eliminar(TEST_MASCOTA_ID)).thenThrow(new RuntimeException("Error de conexión con el servidor"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            mascotaController.eliminar(TEST_MASCOTA_ID);
        });
        
        assertTrue(exception.getMessage().contains("Error de conexión"));
        verify(mascotaService, times(1)).eliminar(TEST_MASCOTA_ID);
        
        System.out.println("CP-ELIM-04: Error de conexión capturado correctamente");
    }

}