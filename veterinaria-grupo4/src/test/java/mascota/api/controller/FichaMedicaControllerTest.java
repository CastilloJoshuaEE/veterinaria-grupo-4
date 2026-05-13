package mascota.api.controller;

import com.mycompany.veterinaria.grupo4.api.controller.MascotaController;
import com.mycompany.veterinaria.grupo4.api.dto.FichaMedicaDTO;
import com.mycompany.veterinaria.grupo4.model.entity.FichaMedica;
import com.mycompany.veterinaria.grupo4.service.MascotaService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la capa de CONTROL - FichaMedica (via MascotaController).
 * 
 * <p><b>Requerimientos Funcionales involucrados:</b></p>
 * <ul>
 *   <li>RF-07: Gestion de ficha medica de mascota</li>
 * </ul>
 * 
 * <p><b>Nota:</b> Las operaciones de ficha médica están expuestas en MascotaController,
 * no en un controlador separado.</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FichaMedicaControllerTest {

    @Mock
    private MascotaService mascotaService;

    @InjectMocks
    private MascotaController mascotaController;

    private static final int TEST_MASCOTA_ID = 34;
    private static final int TEST_CLIENTE_ID = 100;

    // ═══════════════════════════════════════════════════════════════════════
    // CASO DE ÉXITO: GUARDAR FICHA MÉDICA
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(1)
    @DisplayName("CP-CTRL-FM-01: Guardar ficha médica - ÉXITO")
    void testGuardarFichaMedicaExito() {
        // Arrange
        FichaMedicaDTO fichaDTO = new FichaMedicaDTO();
        fichaDTO.setIdMascota(TEST_MASCOTA_ID);
        fichaDTO.setAlergias("Polen, acaros, penicilina");
        fichaDTO.setEnfermedadesCronicas("Diabetes tipo 2");
        fichaDTO.setObservaciones("Requiere dieta especial y ejercicio diario");

        when(mascotaService.guardarFichaMedica(
            eq(TEST_MASCOTA_ID),
            eq("Polen, acaros, penicilina"),
            eq("Diabetes tipo 2"),
            eq("Requiere dieta especial y ejercicio diario")
        )).thenReturn(true);

        // Act
        boolean resultado = mascotaController.guardarFichaMedica(fichaDTO);

        // Assert
        assertTrue(resultado);
        verify(mascotaService, times(1)).guardarFichaMedica(
            eq(TEST_MASCOTA_ID),
            eq("Polen, acaros, penicilina"),
            eq("Diabetes tipo 2"),
            eq("Requiere dieta especial y ejercicio diario")
        );
    }

    // ═══════════════════════════════════════════════════════════════════════
    // CASO DE ERROR: GUARDAR FICHA MÉDICA CON MASCOTA INEXISTENTE
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(2)
    @DisplayName("CP-CTRL-FM-02: Guardar ficha médica con mascota inexistente - ERROR")
    void testGuardarFichaMedicaMascotaInexistente() {
        // Arrange
        int idMascotaInexistente = -1;
        FichaMedicaDTO fichaDTO = new FichaMedicaDTO();
        fichaDTO.setIdMascota(idMascotaInexistente);
        fichaDTO.setAlergias("Polen");
        fichaDTO.setEnfermedadesCronicas("Diabetes");
        fichaDTO.setObservaciones("Observación prueba");

        when(mascotaService.guardarFichaMedica(
            eq(idMascotaInexistente),
            anyString(),
            anyString(),
            anyString()
        )).thenThrow(new RuntimeException("La mascota especificada no existe"));

        // Act
        boolean resultado = mascotaController.guardarFichaMedica(fichaDTO);

        // Assert
        assertFalse(resultado);
        verify(mascotaService, times(1)).guardarFichaMedica(
            eq(idMascotaInexistente),
            anyString(),
            anyString(),
            anyString()
        );
    }

    // ═══════════════════════════════════════════════════════════════════════
    // CASO DE ÉXITO: OBTENER FICHA MÉDICA
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(3)
    @DisplayName("CP-CTRL-FM-03: Obtener ficha médica de mascota existente - ÉXITO")
    void testObtenerFichaMedicaExito() {
        // Arrange
        FichaMedicaDTO fichaEsperada = new FichaMedicaDTO();
        fichaEsperada.setIdMascota(TEST_MASCOTA_ID);
        fichaEsperada.setAlergias("Polen");
        fichaEsperada.setEnfermedadesCronicas("Diabetes");
        fichaEsperada.setObservaciones("Dieta especial");

        when(mascotaService.obtenerFichaMedica(TEST_MASCOTA_ID)).thenReturn(fichaEsperada);

        // Act
        FichaMedicaDTO resultado = mascotaController.obtenerFichaMedica(TEST_MASCOTA_ID);

        // Assert
        assertNotNull(resultado);
        assertEquals(TEST_MASCOTA_ID, resultado.getIdMascota());
        assertEquals("Polen", resultado.getAlergias());
        assertEquals("Diabetes", resultado.getEnfermedadesCronicas());
        assertEquals("Dieta especial", resultado.getObservaciones());
        verify(mascotaService, times(1)).obtenerFichaMedica(TEST_MASCOTA_ID);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // CASO DE ERROR: OBTENER FICHA MÉDICA DE MASCOTA INEXISTENTE
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(4)
    @DisplayName("CP-CTRL-FM-04: Obtener ficha médica de mascota inexistente - ERROR (null)")
    void testObtenerFichaMedicaMascotaInexistente() {
        // Arrange
        int idMascotaInexistente = -1;
        when(mascotaService.obtenerFichaMedica(idMascotaInexistente)).thenReturn(null);

        // Act
        FichaMedicaDTO resultado = mascotaController.obtenerFichaMedica(idMascotaInexistente);

        // Assert
        assertNull(resultado);
        verify(mascotaService, times(1)).obtenerFichaMedica(idMascotaInexistente);
    }

}