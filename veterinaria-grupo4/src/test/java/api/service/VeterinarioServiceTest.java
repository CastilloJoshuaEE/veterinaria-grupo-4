package api.service;

import com.mycompany.veterinaria.grupo4.model.dao.IVeterinarioDAO;
import com.mycompany.veterinaria.grupo4.model.entity.EspecialidadVeterinaria;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.service.VeterinarioService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la capa de SERVICIO - VeterinarioService.
 * 
 * @author BESILLA TOMALA ANGEL KALED – MODULO: VETERINARIO
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VeterinarioServiceTest {

    @Mock
    private IVeterinarioDAO veterinarioDAO;

    private VeterinarioService veterinarioService;

    private static Veterinario veterinarioPrueba;
    private static final int TEST_VETERINARIO_ID = 1;
    private static final String TEST_CEDULA_VALIDA = "1713175071"; 
    private static final String TEST_CEDULA_UNICA = "0926687921"; 
    private static final String TEST_CEDULA_INVALIDA = "123";
    private static final int ID_INVALIDO = -1;
    private static final int ID_NO_EXISTENTE = 999;

    @BeforeAll
    static void setUpClass() {
        EspecialidadVeterinaria especialidad = new EspecialidadVeterinaria();
        especialidad.setIdEspecialidad(1);
        especialidad.setNombreEspecialidad("Cardiología");

        veterinarioPrueba = new Veterinario();
        veterinarioPrueba.setIdVeterinario(TEST_VETERINARIO_ID);
        veterinarioPrueba.setCedula(TEST_CEDULA_VALIDA);
        veterinarioPrueba.setNombre("Juan");
        veterinarioPrueba.setApellido("Perez");
        veterinarioPrueba.setTelefono("0999999999");
        veterinarioPrueba.setCorreoElectronico("juan.perez@mail.com");
        veterinarioPrueba.setDireccion("Av. Siempreviva 123");
        veterinarioPrueba.setPagoMensual(460.00);
        veterinarioPrueba.setEspecialidad(especialidad);
    }

    @BeforeEach
    void setUp() throws Exception {
        veterinarioService = new VeterinarioService(veterinarioDAO);

        lenient().when(veterinarioDAO.obtenerPorId(TEST_VETERINARIO_ID)).thenReturn(veterinarioPrueba);
        lenient().when(veterinarioDAO.obtenerPorId(ID_NO_EXISTENTE)).thenReturn(null);
        lenient().when(veterinarioDAO.obtenerPorCedula(TEST_CEDULA_VALIDA)).thenReturn(veterinarioPrueba);
        // CORRECCIÓN 2: Asegurarse de que la cédula única no devuelva un veterinario existente
        lenient().when(veterinarioDAO.obtenerPorCedula(TEST_CEDULA_UNICA)).thenReturn(null);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // PRUEBAS PARA EL MÉTODO crear()
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(1)
    @DisplayName("CP-CRE-SRV-01: Crear - Validación fallida (datos inválidos) - ERROR")
    void testCrearValidacionFallida() throws SQLException {
        Veterinario veterinarioInvalido = new Veterinario();
        veterinarioInvalido.setCedula(TEST_CEDULA_INVALIDA);
        veterinarioInvalido.setNombre("Juan");
        veterinarioInvalido.setApellido("Perez");
        veterinarioInvalido.setTelefono("0999999999");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            veterinarioService.crear(veterinarioInvalido);
        });

        assertTrue(exception.getMessage().contains("La cedula debe contener exactamente 10"));

        verify(veterinarioDAO, never()).insertar(any());

        System.out.println("CP-CRE-SRV-01: Validación fallida - IllegalArgumentException lanzada");
    }

    @Test
    @Order(2)
    @DisplayName("CP-CRE-SRV-02: Crear - Cédula duplicada - ERROR")
    void testCrearCedulaDuplicada() throws SQLException {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            veterinarioService.crear(veterinarioPrueba);
        });

        assertTrue(exception.getMessage().contains("Ya existe un veterinario con la cedula: " + TEST_CEDULA_VALIDA));

        verify(veterinarioDAO, never()).insertar(any());

        System.out.println("CP-CRE-SRV-02: Cédula duplicada - IllegalArgumentException lanzada");
    }

    @Test
    @Order(3)
    @DisplayName("CP-CRE-SRV-03: Crear - ÉXITO")
    void testCrearExito() throws SQLException {
        Veterinario veterinarioNuevo = new Veterinario();
        // CORRECCIÓN 5: Usar una cédula única y VÁLIDA
        veterinarioNuevo.setCedula(TEST_CEDULA_UNICA);
        veterinarioNuevo.setNombre("Maria");
        veterinarioNuevo.setApellido("Gonzalez");
        veterinarioNuevo.setTelefono("0988888888");
        veterinarioNuevo.setPagoMensual(460.00);
        
        EspecialidadVeterinaria esp = new EspecialidadVeterinaria();
        esp.setIdEspecialidad(1);
        veterinarioNuevo.setEspecialidad(esp);
        
        // El mock para obtenerPorCedula ya está configurado en el @BeforeEach para devolver null.
        when(veterinarioDAO.insertar(veterinarioNuevo)).thenReturn(true);

        boolean resultado = veterinarioService.crear(veterinarioNuevo);

        assertTrue(resultado);
        verify(veterinarioDAO, times(1)).obtenerPorCedula(TEST_CEDULA_UNICA);
        verify(veterinarioDAO, times(1)).insertar(veterinarioNuevo);

        System.out.println("CP-CRE-SRV-03: Creación exitosa del veterinario");
    }

    @Test
    @Order(4)
    @DisplayName("CP-CRE-SRV-04: Crear - Error SQL - ERROR")
    void testCrearErrorSQL() throws SQLException {
        Veterinario veterinarioNuevo = new Veterinario();
        veterinarioNuevo.setCedula(TEST_CEDULA_UNICA);
        veterinarioNuevo.setNombre("Maria");
        veterinarioNuevo.setApellido("Gonzalez");
        veterinarioNuevo.setTelefono("0988888888");
        veterinarioNuevo.setPagoMensual(460.00);
        
        EspecialidadVeterinaria esp = new EspecialidadVeterinaria();
        esp.setIdEspecialidad(1);
        veterinarioNuevo.setEspecialidad(esp);
        
        when(veterinarioDAO.insertar(veterinarioNuevo)).thenThrow(new SQLException("Error de conexión"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            veterinarioService.crear(veterinarioNuevo);
        });

        assertTrue(exception.getMessage().contains("Error al crear el veterinario en la base de datos"));
        verify(veterinarioDAO, times(1)).insertar(veterinarioNuevo);

        System.out.println("CP-CRE-SRV-04: Error SQL capturado correctamente");
    }

    // ═══════════════════════════════════════════════════════════════════════
    // PRUEBAS PARA EL MÉTODO actualizar()
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(5)
    @DisplayName("CP-ACT-SRV-01: Actualizar - ID de veterinario inválido (≤ 0) - ERROR")
    void testActualizarIdInvalido() throws SQLException {
        Veterinario veterinarioInvalido = new Veterinario();
        veterinarioInvalido.setIdVeterinario(0); // ID inválido
        veterinarioInvalido.setCedula(TEST_CEDULA_UNICA);  // Cédula única y válida
        veterinarioInvalido.setNombre("Juan");
        veterinarioInvalido.setApellido("Perez");
        veterinarioInvalido.setTelefono("0999999999");    // Teléfono válido (obligatorio)
        veterinarioInvalido.setPagoMensual(460.00);         // Pago válido (obligatorio)
        EspecialidadVeterinaria esp = new EspecialidadVeterinaria();
        esp.setIdEspecialidad(1);
        veterinarioInvalido.setEspecialidad(esp);          // Especialidad válida (obligatorio)
        // --- FIN CORRECCIÓN ---

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            veterinarioService.actualizar(veterinarioInvalido);
        });

        assertTrue(exception.getMessage().contains("ID de veterinario invalido para actualizar"));
        verify(veterinarioDAO, never()).actualizar(any());

        System.out.println("CP-ACT-SRV-01: ID inválido - IllegalArgumentException lanzada");
    }

    @Test
    @Order(6)
    @DisplayName("CP-ACT-SRV-02: Actualizar - Veterinario no existe - ERROR")
    void testActualizarVeterinarioNoExiste() throws SQLException {
        int idNoExistente = 999;
        Veterinario veterinarioNoExistente = new Veterinario();
        veterinarioNoExistente.setIdVeterinario(idNoExistente);
        veterinarioNoExistente.setCedula(TEST_CEDULA_UNICA); // Cédula única y válida
        veterinarioNoExistente.setNombre("Juan");
        veterinarioNoExistente.setApellido("Perez");
        veterinarioNoExistente.setTelefono("0999999999");   // Teléfono válido
        veterinarioNoExistente.setPagoMensual(460.00);        // Pago válido
        EspecialidadVeterinaria esp = new EspecialidadVeterinaria();
        esp.setIdEspecialidad(1);
        veterinarioNoExistente.setEspecialidad(esp);         // Especialidad válida

        // El mock para obtenerPorId con ID 999 ya devuelve null (configurado en setUp)
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            veterinarioService.actualizar(veterinarioNoExistente);
        });

        assertTrue(exception.getMessage().contains("No existe un veterinario con ID"));
        verify(veterinarioDAO, never()).actualizar(any());

        System.out.println("CP-ACT-SRV-02: Veterinario no existe - IllegalArgumentException lanzada");
    }

    @Test
    @Order(7)
    @DisplayName("CP-ACT-SRV-03: Actualizar - Cédula en uso por otro veterinario - ERROR")
    void testActualizarCedulaEnUsoPorOtro() throws SQLException {
        int idOtroVeterinario = 2;
        Veterinario otroVeterinario = new Veterinario();
        otroVeterinario.setIdVeterinario(idOtroVeterinario);
        otroVeterinario.setCedula(TEST_CEDULA_VALIDA);

        // Configurar mocks para este caso específico
        when(veterinarioDAO.obtenerPorId(TEST_VETERINARIO_ID)).thenReturn(veterinarioPrueba);
        when(veterinarioDAO.obtenerPorCedula(TEST_CEDULA_VALIDA)).thenReturn(otroVeterinario);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            veterinarioService.actualizar(veterinarioPrueba);
        });

        assertTrue(exception.getMessage().contains("La cedula " + TEST_CEDULA_VALIDA + " ya esta registrada por otro veterinario"));

        verify(veterinarioDAO, never()).actualizar(any());

        System.out.println("CP-ACT-SRV-03: Cédula en uso por otro - IllegalArgumentException lanzada");
    }

    @Test
    @Order(8)
    @DisplayName("CP-ACT-SRV-04: Actualizar - ÉXITO")
    void testActualizarExito() throws SQLException {
        // Configurar mocks para asegurar que la cédula pertenece al mismo veterinario
        when(veterinarioDAO.obtenerPorId(TEST_VETERINARIO_ID)).thenReturn(veterinarioPrueba);
        when(veterinarioDAO.obtenerPorCedula(TEST_CEDULA_VALIDA)).thenReturn(veterinarioPrueba);
        when(veterinarioDAO.actualizar(veterinarioPrueba)).thenReturn(true);

        boolean resultado = veterinarioService.actualizar(veterinarioPrueba);

        assertTrue(resultado);
        verify(veterinarioDAO, times(1)).obtenerPorId(TEST_VETERINARIO_ID);
        verify(veterinarioDAO, times(1)).obtenerPorCedula(TEST_CEDULA_VALIDA);
        verify(veterinarioDAO, times(1)).actualizar(veterinarioPrueba);

        System.out.println("CP-ACT-SRV-04: Actualización exitosa del veterinario");
    }

    @Test
    @Order(9)
    @DisplayName("CP-ACT-SRV-05: Actualizar - Error SQL - ERROR")
    void testActualizarErrorSQL() throws SQLException {
        // Configurar mocks para simular un error de SQL durante la actualización
        when(veterinarioDAO.obtenerPorId(TEST_VETERINARIO_ID)).thenReturn(veterinarioPrueba);
        when(veterinarioDAO.obtenerPorCedula(TEST_CEDULA_VALIDA)).thenReturn(veterinarioPrueba);
        when(veterinarioDAO.actualizar(veterinarioPrueba)).thenThrow(new SQLException("Error de conexión"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            veterinarioService.actualizar(veterinarioPrueba);
        });

        assertTrue(exception.getMessage().contains("Error al actualizar el veterinario en la base de datos"));
        verify(veterinarioDAO, times(1)).actualizar(veterinarioPrueba);

        System.out.println("CP-ACT-SRV-05: Error SQL capturado correctamente");
    }
}