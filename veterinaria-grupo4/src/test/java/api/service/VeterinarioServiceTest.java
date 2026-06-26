package api.service;

import com.mycompany.veterinaria.grupo4.model.dao.IVeterinarioDAO;
import com.mycompany.veterinaria.grupo4.model.entity.EspecialidadVeterinaria;
import com.mycompany.veterinaria.grupo4.model.entity.Usuario;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.service.AuthService;
import com.mycompany.veterinaria.grupo4.service.VeterinarioService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    
    @Mock
    private AuthService authService;
    
    private VeterinarioService veterinarioService;

    private static Veterinario veterinarioPrueba;
    private static final int TEST_VETERINARIO_ID = 1;
    private static final String TEST_CEDULA_VALIDA = "1713175071"; 
    private static final String TEST_CEDULA_UNICA = "0926687921"; 
    private static final String TEST_CEDULA_INVALIDA = "123";
    private static final int ID_INVALIDO = -1;
    private static final int ID_NO_EXISTENTE = 999;
    private static final int TEST_USUARIO_ID = 100;

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
        veterinarioService = new VeterinarioService(veterinarioDAO, authService);

        // Configuración para obtenerPorCedula
        lenient().when(veterinarioDAO.obtenerPorCedula(TEST_CEDULA_VALIDA)).thenReturn(veterinarioPrueba);
        lenient().when(veterinarioDAO.obtenerPorCedula(TEST_CEDULA_UNICA)).thenReturn(null);
        
        // Configuración para obtenerPorId
        lenient().when(veterinarioDAO.obtenerPorId(TEST_VETERINARIO_ID)).thenReturn(veterinarioPrueba);
        lenient().when(veterinarioDAO.obtenerPorId(ID_NO_EXISTENTE)).thenReturn(null);
        
        // Configuración para AuthService - por defecto, el email NO existe
        lenient().when(authService.existeUsuario(anyString())).thenReturn(false);
        
        // Configuración para registrar usuario - por defecto, retorna ID válido
        lenient().when(authService.registrarUsuario(any(Usuario.class))).thenReturn(TEST_USUARIO_ID);
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
        verify(authService, never()).existeUsuario(anyString());
        verify(authService, never()).registrarUsuario(any());

        System.out.println("CP-CRE-SRV-01: Validación fallida - IllegalArgumentException lanzada");
    }

    @Test
    @Order(2)
    @DisplayName("CP-CRE-SRV-02: Crear - Cédula duplicada - ERROR")
    void testCrearCedulaDuplicada() throws SQLException {
        // El mock ya retorna veterinarioPrueba para TEST_CEDULA_VALIDA
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            veterinarioService.crear(veterinarioPrueba);
        });

        assertTrue(exception.getMessage().contains("Ya existe un veterinario con la cédula") || 
               exception.getMessage().contains("Ya existe un veterinario con la cedula"));

        verify(veterinarioDAO, never()).insertar(any());
        verify(authService, never()).existeUsuario(anyString());
        verify(authService, never()).registrarUsuario(any());

        System.out.println("CP-CRE-SRV-02: Cédula duplicada - IllegalArgumentException lanzada");
    }

@Test
@Order(3)
@DisplayName("CP-CRE-SRV-03: Crear - Email ya registrado como usuario - ERROR")
void testCrearEmailYaRegistrado() throws SQLException {
    Veterinario veterinarioNuevo = new Veterinario();
    veterinarioNuevo.setCedula(TEST_CEDULA_UNICA);
    veterinarioNuevo.setNombre("Maria");
    veterinarioNuevo.setApellido("Gonzalez");
    veterinarioNuevo.setTelefono("0988888888");
    veterinarioNuevo.setPagoMensual(460.00);
    veterinarioNuevo.setCorreoElectronico("maria.gonzalez@mail.com");
    
    EspecialidadVeterinaria esp = new EspecialidadVeterinaria();
    esp.setIdEspecialidad(1);
    veterinarioNuevo.setEspecialidad(esp);
    
    // Simular que el email YA EXISTE en la tabla USUARIO
    when(authService.existeUsuario("maria.gonzalez@mail.com")).thenReturn(true);

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
        veterinarioService.crear(veterinarioNuevo);
    });

    assertTrue(exception.getMessage().contains("El correo electrónico 'maria.gonzalez@mail.com' ya está registrado como usuario del sistema"));

    verify(veterinarioDAO, never()).insertar(any());
    verify(authService, times(1)).existeUsuario("maria.gonzalez@mail.com");
    verify(authService, never()).registrarUsuario(any());

    System.out.println("CP-CRE-SRV-03: Email ya registrado - IllegalStateException lanzada");
}

    @Test
    @Order(4)
    @DisplayName("CP-CRE-SRV-04: Crear - ÉXITO")
    void testCrearExito() throws SQLException {
        Veterinario veterinarioNuevo = new Veterinario();
        veterinarioNuevo.setCedula(TEST_CEDULA_UNICA);
        veterinarioNuevo.setNombre("Maria");
        veterinarioNuevo.setApellido("Gonzalez");
        veterinarioNuevo.setTelefono("0988888888");
        veterinarioNuevo.setPagoMensual(460.00);
        veterinarioNuevo.setCorreoElectronico("maria.gonzalez@mail.com");
        
        EspecialidadVeterinaria esp = new EspecialidadVeterinaria();
        esp.setIdEspecialidad(1);
        veterinarioNuevo.setEspecialidad(esp);
        
        // Mock para authService - email NO existe
        when(authService.existeUsuario("maria.gonzalez@mail.com")).thenReturn(false);
        when(authService.registrarUsuario(any(Usuario.class))).thenReturn(TEST_USUARIO_ID);
        when(veterinarioDAO.insertar(veterinarioNuevo)).thenReturn(true);

        boolean resultado = veterinarioService.crear(veterinarioNuevo);

        assertTrue(resultado);
        verify(veterinarioDAO, times(1)).obtenerPorCedula(TEST_CEDULA_UNICA);
        verify(authService, times(1)).existeUsuario("maria.gonzalez@mail.com");
        verify(authService, times(1)).registrarUsuario(any(Usuario.class));
        verify(veterinarioDAO, times(1)).insertar(veterinarioNuevo);

        System.out.println("CP-CRE-SRV-04: Creación exitosa del veterinario");
    }

    @Test
    @Order(5)
    @DisplayName("CP-CRE-SRV-05: Crear - Error SQL - ERROR")
    void testCrearErrorSQL() throws SQLException {
        Veterinario veterinarioNuevo = new Veterinario();
        veterinarioNuevo.setCedula(TEST_CEDULA_UNICA);
        veterinarioNuevo.setNombre("Maria");
        veterinarioNuevo.setApellido("Gonzalez");
        veterinarioNuevo.setTelefono("0988888888");
        veterinarioNuevo.setPagoMensual(460.00);
        veterinarioNuevo.setCorreoElectronico("maria.gonzalez@mail.com");
        
        EspecialidadVeterinaria esp = new EspecialidadVeterinaria();
        esp.setIdEspecialidad(1);
        veterinarioNuevo.setEspecialidad(esp);
        
        // Mock para authService
        when(authService.existeUsuario("maria.gonzalez@mail.com")).thenReturn(false);
        when(authService.registrarUsuario(any(Usuario.class))).thenReturn(TEST_USUARIO_ID);
        
        // Simular error en DAO
        when(veterinarioDAO.insertar(veterinarioNuevo)).thenThrow(new SQLException("Error de conexión"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            veterinarioService.crear(veterinarioNuevo);
        });

        assertTrue(exception.getMessage().contains("Error al crear el veterinario en la base de datos"));
        verify(veterinarioDAO, times(1)).insertar(veterinarioNuevo);
        verify(authService, times(1)).registrarUsuario(any(Usuario.class));

        System.out.println("CP-CRE-SRV-05: Error SQL capturado correctamente");
    }

    // ═══════════════════════════════════════════════════════════════════════
    // PRUEBAS PARA EL MÉTODO actualizar()
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @Order(6)
    @DisplayName("CP-ACT-SRV-01: Actualizar - ID de veterinario inválido (≤ 0) - ERROR")
    void testActualizarIdInvalido() throws SQLException {
        Veterinario veterinarioInvalido = new Veterinario();
        veterinarioInvalido.setIdVeterinario(0);
        veterinarioInvalido.setCedula(TEST_CEDULA_UNICA);
        veterinarioInvalido.setNombre("Juan");
        veterinarioInvalido.setApellido("Perez");
        veterinarioInvalido.setTelefono("0999999999");
        veterinarioInvalido.setPagoMensual(460.00);
        EspecialidadVeterinaria esp = new EspecialidadVeterinaria();
        esp.setIdEspecialidad(1);
        veterinarioInvalido.setEspecialidad(esp);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            veterinarioService.actualizar(veterinarioInvalido);
        });

        assertTrue(exception.getMessage().contains("ID de veterinario invalido para actualizar"));
        verify(veterinarioDAO, never()).actualizar(any());

        System.out.println("CP-ACT-SRV-01: ID inválido - IllegalArgumentException lanzada");
    }

    @Test
    @Order(7)
    @DisplayName("CP-ACT-SRV-02: Actualizar - Veterinario no existe - ERROR")
    void testActualizarVeterinarioNoExiste() throws SQLException {
        int idNoExistente = 999;
        Veterinario veterinarioNoExistente = new Veterinario();
        veterinarioNoExistente.setIdVeterinario(idNoExistente);
        veterinarioNoExistente.setCedula(TEST_CEDULA_UNICA);
        veterinarioNoExistente.setNombre("Juan");
        veterinarioNoExistente.setApellido("Perez");
        veterinarioNoExistente.setTelefono("0999999999");
        veterinarioNoExistente.setPagoMensual(460.00);
        EspecialidadVeterinaria esp = new EspecialidadVeterinaria();
        esp.setIdEspecialidad(1);
        veterinarioNoExistente.setEspecialidad(esp);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            veterinarioService.actualizar(veterinarioNoExistente);
        });

        assertTrue(exception.getMessage().contains("No existe un veterinario con ID"));
        verify(veterinarioDAO, never()).actualizar(any());

        System.out.println("CP-ACT-SRV-02: Veterinario no existe - IllegalArgumentException lanzada");
    }

    @Test
    @Order(8)
    @DisplayName("CP-ACT-SRV-03: Actualizar - Cédula en uso por otro veterinario - ERROR")
    void testActualizarCedulaEnUsoPorOtro() throws SQLException {
        int idOtroVeterinario = 2;
        Veterinario otroVeterinario = new Veterinario();
        otroVeterinario.setIdVeterinario(idOtroVeterinario);
        otroVeterinario.setCedula(TEST_CEDULA_VALIDA);

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
    @Order(9)
    @DisplayName("CP-ACT-SRV-04: Actualizar - ÉXITO")
    void testActualizarExito() throws SQLException {
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
    @Order(10)
    @DisplayName("CP-ACT-SRV-05: Actualizar - Error SQL - ERROR")
    void testActualizarErrorSQL() throws SQLException {
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