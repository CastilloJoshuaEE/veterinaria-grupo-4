package api.service;

import com.mycompany.veterinaria.grupo4.model.dao.ICitaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Cita;
import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
import com.mycompany.veterinaria.grupo4.model.entity.Servicio;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.service.CitaService;
import com.mycompany.veterinaria.grupo4.service.MascotaService;
import com.mycompany.veterinaria.grupo4.service.ServicioService;
import com.mycompany.veterinaria.grupo4.service.VeterinarioService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la capa de SERVICIO - CitaService.
 *
 * <p><b>MÓDULO: AGENDAMIENTO DE CITA</b></p>
 *
 * @author CHILAN CHILAN DANNY ANDRES – MODULO: AGENDAMIENTO DE CITA
 * @version 2.0 (con inyección de dependencias)
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CitaServiceTest {

    @Mock
    private ICitaDAO citaDAO;

    @Mock
    private VeterinarioService veterinarioService;

    @Mock
    private ServicioService servicioService;

    @Mock
    private MascotaService mascotaService;

    private CitaService citaService;

    private static Cita citaPrueba;
    private static Veterinario veterinarioPrueba;
    private static Servicio servicioPrueba;
    private static Mascota mascotaPrueba;
    private static Cliente clientePrueba;

    private static final int TEST_CITA_ID       = 1;
    private static final int TEST_CLIENTE_ID    = 100;
    private static final int TEST_MASCOTA_ID    = 200;
    private static final int TEST_SERVICIO_ID   = 300;
    private static final int TEST_VETERINARIO_ID = 400;
    private static final int ID_INVALIDO        = -1;
    private static final int ID_NO_EXISTENTE    = 999;

    @BeforeAll
    static void setUpClass() {
        clientePrueba = new Cliente();
        clientePrueba.setIdCliente(TEST_CLIENTE_ID);
        clientePrueba.setNombre("Juan");
        clientePrueba.setApellido("Perez");

        mascotaPrueba = new Mascota();
        mascotaPrueba.setIdMascota(TEST_MASCOTA_ID);
        mascotaPrueba.setNombre("Firulais");
        mascotaPrueba.setIdCliente(TEST_CLIENTE_ID);

        servicioPrueba = new Servicio();
        servicioPrueba.setIdServicio(TEST_SERVICIO_ID);
        servicioPrueba.setNombreServicio("Consulta General");
        servicioPrueba.setEstado(true);

        veterinarioPrueba = new Veterinario();
        veterinarioPrueba.setIdVeterinario(TEST_VETERINARIO_ID);
        veterinarioPrueba.setNombre("Carlos");
        veterinarioPrueba.setApellido("Jimenez");

        citaPrueba = new Cita();
        citaPrueba.setIdCita(TEST_CITA_ID);
        citaPrueba.setCliente(clientePrueba);
        citaPrueba.setMascota(mascotaPrueba);
        citaPrueba.setServicio(servicioPrueba);
        citaPrueba.setVeterinario(veterinarioPrueba);
        citaPrueba.setFechaHora(new Date(System.currentTimeMillis() + 86400000)); // mañana
        citaPrueba.setEstado("PENDIENTE");
        citaPrueba.setObservaciones("Cita de prueba");
    }

    @BeforeEach
    void setUp() {
        // Usar el constructor con inyección de dependencias
        citaService = new CitaService(citaDAO, veterinarioService, servicioService, mascotaService);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // PRUEBAS PARA listarPorServicioYVeterinario() - COMPLEJIDAD CICLOMÁTICA M=4
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(1)
    @DisplayName("CP-LIST-FILTRO-01: Listar - ID servicio inválido (≤ 0) - ERROR")
    void testListarPorServicioYVeterinarioIdServicioInvalido() throws SQLException {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            citaService.listarPorServicioYVeterinario(ID_INVALIDO, TEST_VETERINARIO_ID, "PENDIENTE");
        });

        assertTrue(exception.getMessage().contains("ID de servicio invalido"));
        verify(citaDAO, never()).obtenerPorServicioYVeterinario(anyInt(), anyInt(), anyString());

        System.out.println("CP-LIST-FILTRO-01: ID servicio inválido - IllegalArgumentException lanzada");
    }

    @Test
    @Order(2)
    @DisplayName("CP-LIST-FILTRO-02: Listar - ID veterinario inválido (≤ 0) - ERROR")
    void testListarPorServicioYVeterinarioIdVeterinarioInvalido() throws SQLException {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            citaService.listarPorServicioYVeterinario(TEST_SERVICIO_ID, ID_INVALIDO, "PENDIENTE");
        });

        assertTrue(exception.getMessage().contains("ID de veterinario invalido"));
        verify(citaDAO, never()).obtenerPorServicioYVeterinario(anyInt(), anyInt(), anyString());

        System.out.println("CP-LIST-FILTRO-02: ID veterinario inválido - IllegalArgumentException lanzada");
    }

    @Test
    @Order(3)
    @DisplayName("CP-LIST-FILTRO-03: Listar - Estado inválido - ERROR")
    void testListarPorServicioYVeterinarioEstadoInvalido() throws SQLException {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            citaService.listarPorServicioYVeterinario(TEST_SERVICIO_ID, TEST_VETERINARIO_ID, "INVALIDO");
        });

        assertTrue(exception.getMessage().contains("Estado invalido"));
        verify(citaDAO, never()).obtenerPorServicioYVeterinario(anyInt(), anyInt(), anyString());

        System.out.println("CP-LIST-FILTRO-03: Estado inválido - IllegalArgumentException lanzada");
    }

    @Test
    @Order(4)
    @DisplayName("CP-LIST-FILTRO-04: Listar - Estado null (sin filtro por estado) - ÉXITO")
    void testListarPorServicioYVeterinarioEstadoNull() throws SQLException {
        List<Cita> citasEsperadas = Arrays.asList(citaPrueba);
        when(citaDAO.obtenerPorServicioYVeterinario(TEST_SERVICIO_ID, TEST_VETERINARIO_ID, null))
                .thenReturn(citasEsperadas);

        List<Cita> resultado = citaService.listarPorServicioYVeterinario(
                TEST_SERVICIO_ID, TEST_VETERINARIO_ID, null);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(citaDAO, times(1)).obtenerPorServicioYVeterinario(TEST_SERVICIO_ID, TEST_VETERINARIO_ID, null);

        System.out.println("CP-LIST-FILTRO-04: Búsqueda con estado null - exitosa");
    }

    @Test
    @Order(5)
    @DisplayName("CP-LIST-FILTRO-05: Listar - Con resultados - ÉXITO")
    void testListarPorServicioYVeterinarioConResultados() throws SQLException {
        List<Cita> citasEsperadas = Arrays.asList(citaPrueba);
        when(citaDAO.obtenerPorServicioYVeterinario(TEST_SERVICIO_ID, TEST_VETERINARIO_ID, "PENDIENTE"))
                .thenReturn(citasEsperadas);

        List<Cita> resultado = citaService.listarPorServicioYVeterinario(
                TEST_SERVICIO_ID, TEST_VETERINARIO_ID, "PENDIENTE");

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("PENDIENTE", resultado.get(0).getEstado());
        verify(citaDAO, times(1)).obtenerPorServicioYVeterinario(
                TEST_SERVICIO_ID, TEST_VETERINARIO_ID, "PENDIENTE");

        System.out.println("CP-LIST-FILTRO-05: Búsqueda con resultados - exitosa");
    }

    @Test
    @Order(6)
    @DisplayName("CP-LIST-FILTRO-06: Listar - Sin resultados (lista vacía) - ÉXITO")
    void testListarPorServicioYVeterinarioSinResultados() throws SQLException {
        List<Cita> listaVacia = Collections.emptyList();
        when(citaDAO.obtenerPorServicioYVeterinario(TEST_SERVICIO_ID, TEST_VETERINARIO_ID, "REALIZADA"))
                .thenReturn(listaVacia);

        List<Cita> resultado = citaService.listarPorServicioYVeterinario(
                TEST_SERVICIO_ID, TEST_VETERINARIO_ID, "REALIZADA");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(citaDAO, times(1)).obtenerPorServicioYVeterinario(
                TEST_SERVICIO_ID, TEST_VETERINARIO_ID, "REALIZADA");

        System.out.println("CP-LIST-FILTRO-06: Búsqueda sin resultados - lista vacía");
    }

    @Test
    @Order(7)
    @DisplayName("CP-LIST-FILTRO-07: Listar - Error SQL - Retorna null")
    void testListarPorServicioYVeterinarioErrorSQL() throws SQLException {
        when(citaDAO.obtenerPorServicioYVeterinario(TEST_SERVICIO_ID, TEST_VETERINARIO_ID, "PENDIENTE"))
                .thenThrow(new SQLException("Error de conexión"));

        List<Cita> resultado = citaService.listarPorServicioYVeterinario(
                TEST_SERVICIO_ID, TEST_VETERINARIO_ID, "PENDIENTE");

        assertNull(resultado);
        verify(citaDAO, times(1)).obtenerPorServicioYVeterinario(
                TEST_SERVICIO_ID, TEST_VETERINARIO_ID, "PENDIENTE");

        System.out.println("CP-LIST-FILTRO-07: Error SQL - retorna null correctamente");
    }

    // ═══════════════════════════════════════════════════════════════════════
    // PRUEBAS PARA agendar() - COMPLEJIDAD CICLOMÁTICA M=5
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(8)
    @DisplayName("CP-AGENDAR-01: Agendar - Validación fallida (cita nula) - ERROR")
    void testAgendarCitaNula() throws SQLException {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            citaService.agendar(null);
        });

        assertTrue(exception.getMessage().contains("La cita no puede ser nula"));
        verify(citaDAO, never()).agendar(any());

        System.out.println("CP-AGENDAR-01: Cita nula - IllegalArgumentException lanzada");
    }

    @Test
    @Order(9)
    @DisplayName("CP-AGENDAR-02: Agendar - Cliente nulo - ERROR")
    void testAgendarClienteNulo() throws SQLException {
        Cita citaInvalida = new Cita();
        citaInvalida.setCliente(null);
        citaInvalida.setMascota(mascotaPrueba);
        citaInvalida.setServicio(servicioPrueba);
        citaInvalida.setVeterinario(veterinarioPrueba);
        citaInvalida.setFechaHora(new Date(System.currentTimeMillis() + 86400000));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            citaService.agendar(citaInvalida);
        });

        assertTrue(exception.getMessage().contains("Debe especificar un cliente valido"));
        verify(citaDAO, never()).agendar(any());

        System.out.println("CP-AGENDAR-02: Cliente nulo - IllegalArgumentException lanzada");
    }

    @Test
    @Order(10)
    @DisplayName("CP-AGENDAR-03: Agendar - Fecha pasada - ERROR")
    void testAgendarFechaPasada() throws SQLException {
        Cita citaFechaPasada = new Cita();
        citaFechaPasada.setCliente(clientePrueba);
        citaFechaPasada.setMascota(mascotaPrueba);
        citaFechaPasada.setServicio(servicioPrueba);
        citaFechaPasada.setVeterinario(veterinarioPrueba);
        citaFechaPasada.setFechaHora(new Date(System.currentTimeMillis() - 86400000)); // ayer

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            citaService.agendar(citaFechaPasada);
        });

        assertTrue(exception.getMessage().contains("No se puede agendar una cita en una fecha/hora pasada"));
        verify(citaDAO, never()).agendar(any());

        System.out.println("CP-AGENDAR-03: Fecha pasada - IllegalArgumentException lanzada");
    }

    @Test
    @Order(11)
    @DisplayName("CP-AGENDAR-04: Agendar - Veterinario no existe - ERROR")
    void testAgendarVeterinarioNoExiste() throws SQLException {
        when(veterinarioService.obtenerPorId(TEST_VETERINARIO_ID)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            citaService.agendar(citaPrueba);
        });

        assertTrue(exception.getMessage().contains("El veterinario especificado no existe"));
        verify(citaDAO, never()).agendar(any());

        System.out.println("CP-AGENDAR-04: Veterinario no existe - IllegalArgumentException lanzada");
    }

    @Test
    @Order(12)
    @DisplayName("CP-AGENDAR-05: Agendar - Servicio no existe - ERROR")
    void testAgendarServicioNoExiste() throws SQLException {
        when(veterinarioService.obtenerPorId(TEST_VETERINARIO_ID)).thenReturn(veterinarioPrueba);
        when(servicioService.obtenerPorId(TEST_SERVICIO_ID)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            citaService.agendar(citaPrueba);
        });

        assertTrue(exception.getMessage().contains("El servicio especificado no existe"));
        verify(citaDAO, never()).agendar(any());

        System.out.println("CP-AGENDAR-05: Servicio no existe - IllegalArgumentException lanzada");
    }

    @Test
    @Order(13)
    @DisplayName("CP-AGENDAR-06: Agendar - Servicio inactivo - ERROR")
    void testAgendarServicioInactivo() throws SQLException {
        Servicio servicioInactivo = new Servicio();
        servicioInactivo.setIdServicio(TEST_SERVICIO_ID);
        servicioInactivo.setNombreServicio("Consulta General");
        servicioInactivo.setEstado(false);

        when(veterinarioService.obtenerPorId(TEST_VETERINARIO_ID)).thenReturn(veterinarioPrueba);
        when(servicioService.obtenerPorId(TEST_SERVICIO_ID)).thenReturn(servicioInactivo);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            citaService.agendar(citaPrueba);
        });

        assertTrue(exception.getMessage().contains("El servicio especificado no esta activo"));
        verify(citaDAO, never()).agendar(any());

        System.out.println("CP-AGENDAR-06: Servicio inactivo - IllegalStateException lanzada");
    }

    @Test
    @Order(14)
    @DisplayName("CP-AGENDAR-07: Agendar - Mascota no existe - ERROR")
    void testAgendarMascotaNoExiste() throws SQLException {
        when(veterinarioService.obtenerPorId(TEST_VETERINARIO_ID)).thenReturn(veterinarioPrueba);
        when(servicioService.obtenerPorId(TEST_SERVICIO_ID)).thenReturn(servicioPrueba);
        when(mascotaService.obtenerPorId(TEST_MASCOTA_ID)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            citaService.agendar(citaPrueba);
        });

        assertTrue(exception.getMessage().contains("La mascota especificada no existe"));
        verify(citaDAO, never()).agendar(any());

        System.out.println("CP-AGENDAR-07: Mascota no existe - IllegalArgumentException lanzada");
    }

    @Test
    @Order(15)
    @DisplayName("CP-AGENDAR-08: Agendar - Mascota no pertenece al cliente - ERROR")
    void testAgendarMascotaNoPerteneceCliente() throws SQLException {
        Mascota mascotaOtroCliente = new Mascota();
        mascotaOtroCliente.setIdMascota(TEST_MASCOTA_ID);
        mascotaOtroCliente.setNombre("Firulais");
        mascotaOtroCliente.setIdCliente(ID_NO_EXISTENTE); // otro cliente

        when(veterinarioService.obtenerPorId(TEST_VETERINARIO_ID)).thenReturn(veterinarioPrueba);
        when(servicioService.obtenerPorId(TEST_SERVICIO_ID)).thenReturn(servicioPrueba);
        when(mascotaService.obtenerPorId(TEST_MASCOTA_ID)).thenReturn(mascotaOtroCliente);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            citaService.agendar(citaPrueba);
        });

        assertTrue(exception.getMessage().contains("La mascota no pertenece al cliente especificado"));
        verify(citaDAO, never()).agendar(any());

        System.out.println("CP-AGENDAR-08: Mascota no pertenece al cliente - IllegalArgumentException lanzada");
    }

    @Test
    @Order(16)
    @DisplayName("CP-AGENDAR-09: Agendar - ÉXITO")
    void testAgendarExito() throws SQLException {
        when(veterinarioService.obtenerPorId(TEST_VETERINARIO_ID)).thenReturn(veterinarioPrueba);
        when(servicioService.obtenerPorId(TEST_SERVICIO_ID)).thenReturn(servicioPrueba);
        when(mascotaService.obtenerPorId(TEST_MASCOTA_ID)).thenReturn(mascotaPrueba);
        when(citaDAO.agendar(citaPrueba)).thenReturn(TEST_CITA_ID);

        int resultado = citaService.agendar(citaPrueba);

        assertEquals(TEST_CITA_ID, resultado);
        verify(citaDAO, times(1)).agendar(citaPrueba);
        verify(veterinarioService, times(1)).obtenerPorId(TEST_VETERINARIO_ID);
        verify(servicioService, times(1)).obtenerPorId(TEST_SERVICIO_ID);
        verify(mascotaService, times(1)).obtenerPorId(TEST_MASCOTA_ID);

        System.out.println("CP-AGENDAR-09: Agendamiento exitoso - ID: " + resultado);
    }

    @Test
    @Order(17)
    @DisplayName("CP-AGENDAR-10: Agendar - Error SQL - ERROR")
    void testAgendarErrorSQL() throws SQLException {
        when(veterinarioService.obtenerPorId(TEST_VETERINARIO_ID)).thenReturn(veterinarioPrueba);
        when(servicioService.obtenerPorId(TEST_SERVICIO_ID)).thenReturn(servicioPrueba);
        when(mascotaService.obtenerPorId(TEST_MASCOTA_ID)).thenReturn(mascotaPrueba);
        when(citaDAO.agendar(citaPrueba)).thenThrow(new SQLException("Error de conexión"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            citaService.agendar(citaPrueba);
        });

        assertTrue(exception.getMessage().contains("Error al agendar la cita"));
        verify(citaDAO, times(1)).agendar(citaPrueba);

        System.out.println("CP-AGENDAR-10: Error SQL capturado correctamente");
    }
}