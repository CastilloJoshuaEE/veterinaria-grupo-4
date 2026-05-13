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
    // CASO DE ÉXITO: LISTAR MASCOTAS POR CLIENTE
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(1)
    @DisplayName("CP-CTRL-01: Listar mascotas por cliente - ÉXITO")
    void testListarPorClienteExito() {
        // Arrange
        List<Mascota> mascotasEsperadas = Arrays.asList(mascotaPrueba);
        when(mascotaService.listarPorCliente(TEST_CLIENTE_ID)).thenReturn(mascotasEsperadas);

        // Act
        List<Mascota> resultado = mascotaController.listarPorCliente(TEST_CLIENTE_ID);

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Firulais", resultado.get(0).getNombre());
        verify(mascotaService, times(1)).listarPorCliente(TEST_CLIENTE_ID);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // CASO DE ERROR: LISTAR MASCOTAS POR CLIENTE INEXISTENTE
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(2)
    @DisplayName("CP-CTRL-02: Listar mascotas de cliente inexistente - ERROR (lista vacía)")
    void testListarPorClienteInexistente() {
        // Arrange
        int clienteInexistente = -1;
        when(mascotaService.listarPorCliente(clienteInexistente)).thenReturn(null);

        // Act
        List<Mascota> resultado = mascotaController.listarPorCliente(clienteInexistente);

        // Assert
        assertNull(resultado);
        verify(mascotaService, times(1)).listarPorCliente(clienteInexistente);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // CASO DE ÉXITO: OBTENER MASCOTA POR ID
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(3)
    @DisplayName("CP-CTRL-03: Obtener mascota por ID existente - ÉXITO")
    void testObtenerPorIdExito() {
        // Arrange
        when(mascotaService.obtenerPorId(TEST_MASCOTA_ID)).thenReturn(mascotaPrueba);

        // Act
        Mascota resultado = mascotaController.obtenerPorId(TEST_MASCOTA_ID);

        // Assert
        assertNotNull(resultado);
        assertEquals(TEST_MASCOTA_ID, resultado.getIdMascota());
        assertEquals("Firulais", resultado.getNombre());
        verify(mascotaService, times(1)).obtenerPorId(TEST_MASCOTA_ID);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // CASO DE ERROR: OBTENER MASCOTA POR ID INEXISTENTE
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(4)
    @DisplayName("CP-CTRL-04: Obtener mascota por ID inexistente - ERROR (null)")
    void testObtenerPorIdInexistente() {
        // Arrange
        int idInexistente = 99999;
        when(mascotaService.obtenerPorId(idInexistente)).thenReturn(null);

        // Act
        Mascota resultado = mascotaController.obtenerPorId(idInexistente);

        // Assert
        assertNull(resultado);
        verify(mascotaService, times(1)).obtenerPorId(idInexistente);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // CASO DE ÉXITO: ELIMINAR MASCOTA (SIN HISTORIAL CLÍNICO)
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(5)
    @DisplayName("CP-CTRL-05: Eliminar mascota sin historial clínico - ÉXITO")
    void testEliminarMascotaExito() {
        // Arrange
        when(mascotaService.eliminar(TEST_MASCOTA_ID)).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> mascotaController.eliminar(TEST_MASCOTA_ID));
        verify(mascotaService, times(1)).eliminar(TEST_MASCOTA_ID);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // CASO DE ERROR: ELIMINAR MASCOTA CON HISTORIAL CLÍNICO
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(6)
    @DisplayName("CP-CTRL-06: Eliminar mascota con historial clínico - ERROR (ResponseStatusException)")
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
    }

    // ═══════════════════════════════════════════════════════════════════════
    // CASO DE ÉXITO: GUARDAR FICHA MÉDICA
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(7)
    @DisplayName("CP-CTRL-07: Guardar ficha médica - ÉXITO")
    void testGuardarFichaMedicaExito() {
        // Arrange
        FichaMedicaDTO fichaDTO = new FichaMedicaDTO();
        fichaDTO.setIdMascota(TEST_MASCOTA_ID);
        fichaDTO.setAlergias("Polen, acaros");
        fichaDTO.setEnfermedadesCronicas("Diabetes");
        fichaDTO.setObservaciones("Dieta especial");
        
        when(mascotaService.guardarFichaMedica(anyInt(), anyString(), anyString(), anyString()))
            .thenReturn(true);

        // Act
        boolean resultado = mascotaController.guardarFichaMedica(fichaDTO);

        // Assert
        assertTrue(resultado);
        verify(mascotaService, times(1)).guardarFichaMedica(
            eq(TEST_MASCOTA_ID), eq("Polen, acaros"), eq("Diabetes"), eq("Dieta especial")
        );
    }

    
}