package api.service;

import com.mycompany.veterinaria.grupo4.model.dao.IMascotaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
import com.mycompany.veterinaria.grupo4.service.ClienteService;
import com.mycompany.veterinaria.grupo4.service.MascotaService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
/*
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 2.0
 * @since 1.0
*/
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MascotaServiceTest {

    @Mock
    private IMascotaDAO mascotaDAO;

    @Mock
    private ClienteService clienteService;

    private MascotaService mascotaService;

    private static Mascota mascotaPrueba;
    private static final int TEST_MASCOTA_ID = 1;
    private static final int TEST_CLIENTE_ID = 100;
    private static final int ID_INVALIDO = -1;
    private static final int ID_NO_EXISTENTE = 999;

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

    @BeforeEach
    void setUp() throws Exception {
        mascotaService = new MascotaService(clienteService, mascotaDAO);

        lenient().when(mascotaDAO.obtenerPorId(TEST_MASCOTA_ID)).thenReturn(mascotaPrueba);
        lenient().when(mascotaDAO.obtenerPorId(ID_INVALIDO)).thenReturn(null);
        lenient().when(mascotaDAO.obtenerPorId(ID_NO_EXISTENTE)).thenReturn(null);

        lenient().when(clienteService.obtenerPorId(TEST_CLIENTE_ID))
            .thenReturn(mock(com.mycompany.veterinaria.grupo4.model.entity.Cliente.class));
        lenient().when(clienteService.obtenerPorId(999)).thenReturn(null);
    }

    // ═══════════════════════════════════════════════════════
    // PRUEBAS PARA eliminar() - COMPLEJIDAD CICLOMÁTICA M=5
    // ═══════════════════════════════════════════════════════

    @Test
    @Order(1)
    @DisplayName("CP-ELIM-SRV-01: Eliminar - ID inválido (≤ 0) - ERROR")
    void testEliminarIdInvalido() throws SQLException {   
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mascotaService.eliminar(ID_INVALIDO);
        });

        assertTrue(exception.getMessage().contains("ID de mascota invalido"));
        verify(mascotaDAO, never()).eliminar(anyInt());

        System.out.println("CP-ELIM-SRV-01:  ID inválido - IllegalArgumentException lanzada");
    }

    @Test
    @Order(2)
    @DisplayName("CP-ELIM-SRV-02: Eliminar - Mascota no existe - ERROR")
    void testEliminarMascotaNoExiste() throws SQLException {   
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mascotaService.eliminar(ID_NO_EXISTENTE);
        });

        assertTrue(exception.getMessage().contains("No existe una mascota con ID"));
        verify(mascotaDAO, never()).eliminar(anyInt());

        System.out.println("CP-ELIM-SRV-02:  Mascota no existe - IllegalArgumentException lanzada");
    }

    @Test
    @Order(3)
    @DisplayName("CP-ELIM-SRV-03: Eliminar - ÉXITO (sin restricciones)")
    void testEliminarExito() throws SQLException {
        when(mascotaDAO.eliminar(TEST_MASCOTA_ID)).thenReturn(true);

        boolean resultado = mascotaService.eliminar(TEST_MASCOTA_ID);

        assertTrue(resultado);
        verify(mascotaDAO, times(1)).eliminar(TEST_MASCOTA_ID);

        System.out.println("CP-ELIM-SRV-03:  Eliminación exitosa sin restricciones");
    }

    @Test
    @Order(4)
    @DisplayName("CP-ELIM-SRV-04: Eliminar - Error SQL con cita médica realizada - ERROR")
    void testEliminarConCitaMedicaRealizada() throws SQLException {
        SQLException sqlEx = new SQLException("La mascota tiene una cita médica realizada. No se puede eliminar.");
        when(mascotaDAO.eliminar(TEST_MASCOTA_ID)).thenThrow(sqlEx);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            mascotaService.eliminar(TEST_MASCOTA_ID);
        });

        assertTrue(exception.getMessage().contains("cita médica realizada"));
        verify(mascotaDAO, times(1)).eliminar(TEST_MASCOTA_ID);

        System.out.println("CP-ELIM-SRV-04:  Error SQL - Cita médica realizada detectada");
    }

    @Test
    @Order(5)
    @DisplayName("CP-ELIM-SRV-05: Eliminar - Error SQL con citas pendientes - ERROR")
    void testEliminarConCitasPendientes() throws SQLException {
        SQLException sqlEx = new SQLException("La mascota tiene citas pendientes. No se puede eliminar.");
        when(mascotaDAO.eliminar(TEST_MASCOTA_ID)).thenThrow(sqlEx);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            mascotaService.eliminar(TEST_MASCOTA_ID);
        });

        assertTrue(exception.getMessage().contains("citas pendientes"));
        verify(mascotaDAO, times(1)).eliminar(TEST_MASCOTA_ID);

        System.out.println("CP-ELIM-SRV-05:  Error SQL - Citas pendientes detectada");
    }

    @Test
    @Order(6)
    @DisplayName("CP-ELIM-SRV-06: Eliminar - Error SQL genérico - ERROR")
    void testEliminarErrorSQLGenerico() throws SQLException {
        SQLException sqlEx = new SQLException("Error de conexión con la base de datos");
        when(mascotaDAO.eliminar(TEST_MASCOTA_ID)).thenThrow(sqlEx);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            mascotaService.eliminar(TEST_MASCOTA_ID);
        });

        assertTrue(exception.getMessage().contains("Error al eliminar la mascota"));
        verify(mascotaDAO, times(1)).eliminar(TEST_MASCOTA_ID);

        System.out.println("CP-ELIM-SRV-06:  Error SQL genérico capturado correctamente");
    }

    // ═══════════════════════════════════════════════════════
    // PRUEBAS PARA actualizar() - COMPLEJIDAD CICLOMÁTICA M=3
    // ═══════════════════════════════════════════════════════

    @Test
    @Order(7)
    @DisplayName("CP-ACT-SRV-01: Actualizar - ID de mascota inválido (≤ 0) - ERROR")
    void testActualizarIdInvalido() throws SQLException {   
        Mascota mascotaInvalida = new Mascota();
        mascotaInvalida.setIdMascota(0);
        mascotaInvalida.setIdCliente(TEST_CLIENTE_ID);
        mascotaInvalida.setNombre("Prueba");
        mascotaInvalida.setEspecie("Canino");
        mascotaInvalida.setSexo('M');

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mascotaService.actualizar(mascotaInvalida);
        });

        assertTrue(exception.getMessage().contains("ID de mascota invalido para actualizar"));
        verify(mascotaDAO, never()).actualizar(any());

        System.out.println("CP-ACT-SRV-01:  ID inválido - IllegalArgumentException lanzada");
    }

    @Test
    @Order(8)
    @DisplayName("CP-ACT-SRV-02: Actualizar - Mascota no existe - ERROR")
    void testActualizarMascotaNoExiste() throws SQLException {   
        Mascota mascotaNoExistente = new Mascota();
        mascotaNoExistente.setIdMascota(ID_NO_EXISTENTE);
        mascotaNoExistente.setIdCliente(TEST_CLIENTE_ID);
        mascotaNoExistente.setNombre("Prueba");
        mascotaNoExistente.setEspecie("Canino");
        mascotaNoExistente.setSexo('M');

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mascotaService.actualizar(mascotaNoExistente);
        });

        assertTrue(exception.getMessage().contains("No existe una mascota con ID"));
        verify(mascotaDAO, never()).actualizar(any());

        System.out.println("CP-ACT-SRV-02:  Mascota no existe - IllegalArgumentException lanzada");
    }

    @Test
    @Order(9)
    @DisplayName("CP-ACT-SRV-03: Actualizar - Cliente no existe - ERROR")
    void testActualizarClienteNoExiste() throws SQLException {   
        int idClienteNoExistente = 999;
        Mascota mascotaClienteNoExistente = new Mascota();
        mascotaClienteNoExistente.setIdMascota(TEST_MASCOTA_ID);
        mascotaClienteNoExistente.setIdCliente(idClienteNoExistente);
        mascotaClienteNoExistente.setNombre("Prueba");
        mascotaClienteNoExistente.setEspecie("Canino");
        mascotaClienteNoExistente.setSexo('M');

        when(clienteService.obtenerPorId(idClienteNoExistente)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mascotaService.actualizar(mascotaClienteNoExistente);
        });

        assertTrue(exception.getMessage().contains("El cliente con ID " + idClienteNoExistente + " no existe"));
        verify(mascotaDAO, never()).actualizar(any());

        System.out.println("CP-ACT-SRV-03:  Cliente no existe - IllegalArgumentException lanzada");
    }

    @Test
    @Order(10)
    @DisplayName("CP-ACT-SRV-04: Actualizar - ÉXITO")
    void testActualizarExito() throws SQLException {
        when(mascotaDAO.actualizar(mascotaPrueba)).thenReturn(true);

        boolean resultado = mascotaService.actualizar(mascotaPrueba);

        assertTrue(resultado);
        verify(mascotaDAO, times(1)).actualizar(mascotaPrueba);

        System.out.println("CP-ACT-SRV-04:  Actualización exitosa");
    }

    @Test
    @Order(11)
    @DisplayName("CP-ACT-SRV-05: Actualizar - Error SQL - ERROR")
    void testActualizarErrorSQL() throws SQLException {
        SQLException sqlEx = new SQLException("Error de conexión con la base de datos");
        when(mascotaDAO.actualizar(mascotaPrueba)).thenThrow(sqlEx);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            mascotaService.actualizar(mascotaPrueba);
        });

        assertTrue(exception.getMessage().contains("Error al actualizar la mascota"));
        verify(mascotaDAO, times(1)).actualizar(mascotaPrueba);

        System.out.println("CP-ACT-SRV-05:  Error SQL capturado correctamente");
    }
}