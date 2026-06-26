package api.service;

import com.mycompany.veterinaria.grupo4.model.dao.IClienteDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import com.mycompany.veterinaria.grupo4.service.ClienteService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
/**
 * Pruebas unitarias para la capa de SERVICIO (La capa de control).
 * 
 * @author CASTRO AVILA – MODULO: CLIENTE
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClienteServiceTest {

    @Mock
    private IClienteDAO clienteDAO;

    private ClienteService clienteService;

    private static Cliente clientePrueba;
    private static final int TEST_CLIENTE_ID = 1;
    private static final String TEST_CEDULA_VALIDA = "1713175071";
    private static final String TEST_CEDULA_UNICA = "0926687921";

    @BeforeAll
    static void setUpClass() {
        clientePrueba = new Cliente();
        clientePrueba.setIdCliente(TEST_CLIENTE_ID);
        clientePrueba.setCedula(TEST_CEDULA_VALIDA);
        clientePrueba.setNombre("Juan");
        clientePrueba.setApellido("Perez");
        clientePrueba.setTelefono("0999999999");
        clientePrueba.setCorreoElectronico("juan.perez@mail.com");
        clientePrueba.setDireccion("Av. Siempreviva 123");
    }

    @BeforeEach
    void setUp() throws Exception {
        clienteService = new ClienteService(clienteDAO);

        lenient().when(clienteDAO.obtenerPorId(TEST_CLIENTE_ID)).thenReturn(clientePrueba);
        lenient().when(clienteDAO.obtenerPorId(999)).thenReturn(null);
        lenient().when(clienteDAO.obtenerPorCedula(TEST_CEDULA_VALIDA)).thenReturn(clientePrueba);
        lenient().when(clienteDAO.obtenerPorCedula(TEST_CEDULA_UNICA)).thenReturn(null);
    }

    // ═══════════════════════════════════════════════════════
    // PRUEBAS PARA buscarPorNombre() - COMPLEJIDAD CICLOMÁTICA M=4
    // ═══════════════════════════════════════════════════════

    @Test
    @Order(1)
    @DisplayName("CP-BUS-NOM-01: Buscar - Nombre null - ERROR")
    void testBuscarPorNombreNull() throws SQLException {    
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.buscarPorNombre(null);
        });

        assertTrue(exception.getMessage().contains("El termino de busqueda no puede estar vacio"));
        verify(clienteDAO, never()).buscarPorNombre(anyString());

        System.out.println("CP-BUS-NOM-01: Nombre null - IllegalArgumentException lanzada");
    }

    @Test
    @Order(2)
    @DisplayName("CP-BUS-NOM-02: Buscar - Nombre vacío - ERROR")
    void testBuscarPorNombreVacio() throws SQLException {    
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.buscarPorNombre("");
        });

        assertTrue(exception.getMessage().contains("El termino de busqueda no puede estar vacio"));
        verify(clienteDAO, never()).buscarPorNombre(anyString());

        System.out.println("CP-BUS-NOM-02: Nombre vacío - IllegalArgumentException lanzada");
    }

    @Test
    @Order(3)
    @DisplayName("CP-BUS-NOM-03: Buscar - Nombre con 1 caracter - ERROR")
    void testBuscarPorNombreUnCaracter() throws SQLException {    
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.buscarPorNombre("J");
        });

        assertTrue(exception.getMessage().contains("El termino de busqueda debe tener al menos 2 caracteres"));
        verify(clienteDAO, never()).buscarPorNombre(anyString());

        System.out.println("CP-BUS-NOM-03: Nombre con 1 caracter - IllegalArgumentException lanzada");
    }

    @Test
    @Order(4)
    @DisplayName("CP-BUS-NOM-04: Buscar - ÉXITO con resultados")
    void testBuscarPorNombreExitoConResultados() throws SQLException {
        List<Cliente> clientesEsperados = Arrays.asList(clientePrueba);
        when(clienteDAO.buscarPorNombre("%Juan%")).thenReturn(clientesEsperados);

        List<Cliente> resultado = clienteService.buscarPorNombre("Juan");

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
        verify(clienteDAO, times(1)).buscarPorNombre("%Juan%");

        System.out.println("CP-BUS-NOM-04: Búsqueda exitosa con resultados - " + resultado.size() + " clientes encontrados");
    }

    @Test
    @Order(5)
    @DisplayName("CP-BUS-NOM-05: Buscar - ÉXITO sin resultados (lista vacía)")
    void testBuscarPorNombreExitoSinResultados() throws SQLException {
        List<Cliente> listaVacia = Collections.emptyList();
        when(clienteDAO.buscarPorNombre("%Maria%")).thenReturn(listaVacia);

        List<Cliente> resultado = clienteService.buscarPorNombre("Maria");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(clienteDAO, times(1)).buscarPorNombre("%Maria%");

        System.out.println("CP-BUS-NOM-05: Búsqueda exitosa sin resultados - lista vacía");
    }

    @Test
    @Order(6)
    @DisplayName("CP-BUS-NOM-06: Buscar - Error SQL - Retorna null")
    void testBuscarPorNombreErrorSQL() throws SQLException {
        when(clienteDAO.buscarPorNombre("%Pedro%")).thenThrow(new SQLException("Error de conexión"));

        List<Cliente> resultado = clienteService.buscarPorNombre("Pedro");

        assertNull(resultado);
        verify(clienteDAO, times(1)).buscarPorNombre("%Pedro%");

        System.out.println("CP-BUS-NOM-06: Error SQL - retorna null correctamente");
    }

    // ═══════════════════════════════════════════════════════
    // PRUEBAS PARA CREAR CLIENTE (VALIDACIÓN)
    // ═══════════════════════════════════════════════════════

    @Test
    @Order(7)
    @DisplayName("CP-VAL-CLI-01: Validar - Cliente nulo - ERROR")
    void testValidarClienteNulo() throws SQLException {    
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crear(null);
        });

        assertTrue(exception.getMessage().contains("El objeto cliente no puede ser nulo"));
        verify(clienteDAO, never()).insertar(any());

        System.out.println("CP-VAL-CLI-01: Cliente nulo - IllegalArgumentException lanzada");
    }

    @Test
    @Order(8)
    @DisplayName("CP-VAL-CLI-02: Validar - Nombre nulo - ERROR")
    void testValidarNombreNulo() throws SQLException {    
        Cliente clienteInvalido = new Cliente();
        clienteInvalido.setCedula(TEST_CEDULA_VALIDA);
        clienteInvalido.setNombre(null);
        clienteInvalido.setApellido("Perez");
        clienteInvalido.setTelefono("0999999999");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crear(clienteInvalido);
        });

        assertTrue(exception.getMessage().contains("El nombre del cliente es obligatorio"));
        verify(clienteDAO, never()).insertar(any());

        System.out.println("CP-VAL-CLI-02: Nombre nulo - IllegalArgumentException lanzada");
    }

    @Test
    @Order(9)
    @DisplayName("CP-VAL-CLI-03: Validar - Nombre vacío - ERROR")
    void testValidarNombreVacio() throws SQLException {    
        Cliente clienteInvalido = new Cliente();
        clienteInvalido.setCedula(TEST_CEDULA_VALIDA);
        clienteInvalido.setNombre("");
        clienteInvalido.setApellido("Perez");
        clienteInvalido.setTelefono("0999999999");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crear(clienteInvalido);
        });

        assertTrue(exception.getMessage().contains("El nombre del cliente es obligatorio"));
        verify(clienteDAO, never()).insertar(any());

        System.out.println("CP-VAL-CLI-03: Nombre vacío - IllegalArgumentException lanzada");
    }

    @Test
    @Order(10)
    @DisplayName("CP-VAL-CLI-04: Validar - Nombre excede 50 caracteres - ERROR")
    void testValidarNombreExcedeLongitud() throws SQLException {    
        String nombreLargo = "Este nombre es extremadamente largo y supera los cincuenta caracteres permitidos";
        Cliente clienteInvalido = new Cliente();
        clienteInvalido.setCedula(TEST_CEDULA_VALIDA);
        clienteInvalido.setNombre(nombreLargo);
        clienteInvalido.setApellido("Perez");
        clienteInvalido.setTelefono("0999999999");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crear(clienteInvalido);
        });

        assertTrue(exception.getMessage().contains("El nombre no puede exceder los 50 caracteres"));
        verify(clienteDAO, never()).insertar(any());

        System.out.println("CP-VAL-CLI-04: Nombre muy largo - IllegalArgumentException lanzada");
    }

    @Test
    @Order(11)
    @DisplayName("CP-VAL-CLI-05: Validar - Apellido nulo - ERROR")
    void testValidarApellidoNulo() throws SQLException {    
        Cliente clienteInvalido = new Cliente();
        clienteInvalido.setCedula(TEST_CEDULA_VALIDA);
        clienteInvalido.setNombre("Juan");
        clienteInvalido.setApellido(null);
        clienteInvalido.setTelefono("0999999999");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crear(clienteInvalido);
        });

        assertTrue(exception.getMessage().contains("El apellido del cliente es obligatorio"));
        verify(clienteDAO, never()).insertar(any());

        System.out.println("CP-VAL-CLI-05: Apellido nulo - IllegalArgumentException lanzada");
    }

    @Test
    @Order(12)
    @DisplayName("CP-VAL-CLI-06: Validar - Apellido vacío - ERROR")
    void testValidarApellidoVacio() throws SQLException {    
        Cliente clienteInvalido = new Cliente();
        clienteInvalido.setCedula(TEST_CEDULA_VALIDA);
        clienteInvalido.setNombre("Juan");
        clienteInvalido.setApellido("");
        clienteInvalido.setTelefono("0999999999");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crear(clienteInvalido);
        });

        assertTrue(exception.getMessage().contains("El apellido del cliente es obligatorio"));
        verify(clienteDAO, never()).insertar(any());

        System.out.println("CP-VAL-CLI-06: Apellido vacío - IllegalArgumentException lanzada");
    }

    @Test
    @Order(13)
    @DisplayName("CP-VAL-CLI-07: Validar - Apellido excede 50 caracteres - ERROR")
    void testValidarApellidoExcedeLongitud() throws SQLException {    
        String apellidoLargo = "Este apellido es extremadamente largo y supera los cincuenta caracteres";
        Cliente clienteInvalido = new Cliente();
        clienteInvalido.setCedula(TEST_CEDULA_VALIDA);
        clienteInvalido.setNombre("Juan");
        clienteInvalido.setApellido(apellidoLargo);
        clienteInvalido.setTelefono("0999999999");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crear(clienteInvalido);
        });

        assertTrue(exception.getMessage().contains("El apellido no puede exceder los 50 caracteres"));
        verify(clienteDAO, never()).insertar(any());

        System.out.println("CP-VAL-CLI-07: Apellido muy largo - IllegalArgumentException lanzada");
    }

    @Test
    @Order(14)
    @DisplayName("CP-VAL-CLI-08: Validar - Cédula inválida (formato) - ERROR")
    void testValidarCedulaInvalidaFormato() throws SQLException {    
        Cliente clienteInvalido = new Cliente();
        clienteInvalido.setCedula("123");
        clienteInvalido.setNombre("Juan");
        clienteInvalido.setApellido("Perez");
        clienteInvalido.setTelefono("0999999999");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crear(clienteInvalido);
        });

        assertTrue(exception.getMessage().contains("La cedula debe contener exactamente 10 digitos numericos"));
        verify(clienteDAO, never()).insertar(any());

        System.out.println("CP-VAL-CLI-08: Cédula inválida (formato) - IllegalArgumentException lanzada");
    }

    @Test
    @Order(15)
    @DisplayName("CP-VAL-CLI-09: Validar - Teléfono inválido - ERROR")
    void testValidarTelefonoInvalido() throws SQLException {    
        Cliente clienteInvalido = new Cliente();
        clienteInvalido.setCedula(TEST_CEDULA_VALIDA);
        clienteInvalido.setNombre("Juan");
        clienteInvalido.setApellido("Perez");
        clienteInvalido.setTelefono("12345");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crear(clienteInvalido);
        });

        assertTrue(exception.getMessage().contains("El telefono debe tener exactamente 10 digitos numericos"));
        verify(clienteDAO, never()).insertar(any());

        System.out.println("CP-VAL-CLI-09: Teléfono inválido - IllegalArgumentException lanzada");
    }

    @Test
    @Order(16)
    @DisplayName("CP-VAL-CLI-10: Validar - Email inválido - ERROR")
    void testValidarEmailInvalido() throws SQLException {    
        Cliente clienteInvalido = new Cliente();
        clienteInvalido.setCedula(TEST_CEDULA_VALIDA);
        clienteInvalido.setNombre("Juan");
        clienteInvalido.setApellido("Perez");
        clienteInvalido.setTelefono("0999999999");
        clienteInvalido.setCorreoElectronico("correo-invalido");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crear(clienteInvalido);
        });

        assertTrue(exception.getMessage().contains("El formato del correo electronico es invalido"));
        verify(clienteDAO, never()).insertar(any());

        System.out.println("CP-VAL-CLI-10: Email inválido - IllegalArgumentException lanzada");
    }

    @Test
    @Order(17)
    @DisplayName("CP-VAL-CLI-11: Crear - Cédula duplicada - ERROR")
    void testCrearCedulaDuplicada() throws SQLException {
        when(clienteDAO.obtenerPorCedula(TEST_CEDULA_VALIDA)).thenReturn(clientePrueba);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crear(clientePrueba);
        });

        assertTrue(exception.getMessage().contains("Ya existe un cliente con la cedula"));
        verify(clienteDAO, never()).insertar(any());

        System.out.println("CP-VAL-CLI-11: Cédula duplicada - IllegalArgumentException lanzada");
    }

    @Test
    @Order(18)
    @DisplayName("CP-VAL-CLI-12: Crear - ÉXITO")
    void testCrearExito() throws SQLException {
        Cliente clienteNuevo = new Cliente();
        clienteNuevo.setCedula(TEST_CEDULA_UNICA);
        clienteNuevo.setNombre("Maria");
        clienteNuevo.setApellido("Gonzalez");
        clienteNuevo.setTelefono("0988888888");
        clienteNuevo.setCorreoElectronico("maria.gonzalez@mail.com");

        when(clienteDAO.insertar(clienteNuevo)).thenReturn(true);

        boolean resultado = clienteService.crear(clienteNuevo);

        assertTrue(resultado);
        verify(clienteDAO, times(1)).obtenerPorCedula(TEST_CEDULA_UNICA);
        verify(clienteDAO, times(1)).insertar(clienteNuevo);

        System.out.println("CP-VAL-CLI-12: Creación exitosa del cliente");
    }

    @Test
    @Order(19)
    @DisplayName("CP-VAL-CLI-13: Crear - Error SQL - ERROR")
    void testCrearErrorSQL() throws SQLException {
        Cliente clienteNuevo = new Cliente();
        clienteNuevo.setCedula(TEST_CEDULA_UNICA);
        clienteNuevo.setNombre("Maria");
        clienteNuevo.setApellido("Gonzalez");
        clienteNuevo.setTelefono("0988888888");

        when(clienteDAO.insertar(clienteNuevo)).thenThrow(new SQLException("Error de conexión"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clienteService.crear(clienteNuevo);
        });

        assertTrue(exception.getMessage().contains("Error al crear el cliente en la base de datos"));
        verify(clienteDAO, times(1)).insertar(clienteNuevo);

        System.out.println("CP-VAL-CLI-13: Error SQL capturado correctamente");
    }
}