DME.md

MVC clásico (DAO + Service + View)

Estructura del proyecto:

veterinaria-grupo4/

│

├── pom.xml

│

├── src/

│   └── main/

│       ├── java/

│       │   └── com/

│       │       └── mycompany/

│       │           └── veterinaria/

│       │               └── grupo4/

│       │

│       │                   ├── Main.java

│       │                   │   ← Punto de entrada (Spring Boot + Swing)

│       │

│       │                   ├── config/

│       │                   │   └── DatabaseConfig.java

│       │                   │       ← Configuración de conexión a SQL Server

│       │

│       │                   ├── api/

│       │                   │   ├── controller/

│       │                   │   │   ← Controladores REST

│       │                   │   │   ├── AuthController.java

│       │                   │   │   ├── ClienteController.java

│       │                   │   │   ├── MascotaController.java

│       │                   │   │   ├── CitaController.java

│       │                   │   │   ├── VeterinarioController.java

│       │                   │   │   ├── ServicioController.java

│       │                   │   │   ├── AtencionMedicaController.java

│       │                   │   │   ├── FacturaController.java

│       │                   │   │   ├── HistorialController.java

│       │                   │   │   └── RecordatorioController.java

│       │                   │

│       │                   │   └── dto/

│       │                   │       ← Objetos de transferencia (JSON)

│       │                   │       ├── LoginRequest.java

│       │                   │       ├── LoginResponse.java

│       │                   │       └── ClienteDTO.java

│       │

│       │                   ├── service/

│       │                   │   ← Lógica de negocio

│       │                   │   ├── EspecialidadService.java

│       │                   │   ├── VacunaService.java

│       │                   │   ├── AuthService.java

│       │                   │   ├── ClienteService.java

│       │                   │   ├── MascotaService.java

│       │                   │   ├── CitaService.java

│       │                   │   ├── VeterinarioService.java

│       │                   │   ├── ServicioService.java

│       │                   │   ├── AtencionMedicaService.java

│       │                   │   ├── FacturaService.java

│       │                   │   ├── HistorialService.java

│       │                   │   ├── RecordatorioService.java

│       │                   │   ├── FichaMedicaService.java

│       │                   │   ├── MedicamentoService.java

│       │                   │   └── InstrumentoMedicoService.java

│       │

│       │                   ├── model/

│       │                   │   ├── entity/

│       │                   │   │   ← Entidades del sistema

│       │                   │   │   ├── Usuario.java

│       │                   │   │   ├── Cliente.java

│       │                   │   │   ├── Mascota.java

│       │                   │   │   ├── FichaMedica.java

│       │                   │   │   ├── Veterinario.java

│       │                   │   │   ├── Cita.java

│       │                   │   │   ├── Servicio.java

│       │                   │   │   ├── InstrumentoMedico.java

│       │                   │   │   ├── Medicamento.java

│       │                   │   │   ├── HistorialMedico.java

│       │                   │   │   ├── VacunaAplicada.java

│       │                   │   │   ├── EspecialidadVeterinaria.java

│       │                   │   │   ├── AtencionMedica.java

│       │                   │   │   ├── Factura.java

│       │                   │   │   ├── DetalleFactura.java

│       │                   │   │   └── Recordatorio.java

│       │                   │

│       │                   │   ├── dao/

│       │                   │   │   ← Interfaces DAO

│       │                   │   │   ├── IVacunaDAO.java

│       │                   │   │   ├── IEspecialidadDAO.java

│       │                   │   │   ├── IUsuarioDAO.java

│       │                   │   │   ├── IClienteDAO.java

│       │                   │   │   ├── IMascotaDAO.java

│       │                   │   │   ├── IFichaMedicaDAO.java

│       │                   │   │   ├── IMedicamentoDAO.java

│       │                   │   │   ├── ICitaDAO.java

│       │                   │   │   ├── IServicioDAO.java

│       │                   │   │   ├── IAtencionMedicaDAO.java

│       │                   │   │   ├── IFacturaDAO.java

│       │                   │   │   ├── IHistorialDAO.java

│       │                   │   │   └── IRecordatorioDAO.java

│       │                   │

│       │                   │   └── impl/

│       │                   │       ← Implementaciones DAO

│       │                   │       ├── UsuarioDAOImpl.java

│       │                   │       ├── ClienteDAOImpl.java

│       │                   │       ├── MascotaDAOImpl.java

│       │                   │       ├── CitaDAOImpl.java

│       │                   │       ├── FacturaDAOImpl.java

│       │                   │       ├── FichaMedicaDAOImpl.java

│       │                   │       ├── HistorialDAOImpl.java

│       │                   │       ├── InstrumentoMedicoDAOImpl.java

│       │                   │       ├── MedicamentoDAOImpl.java

│       │                   │       ├── RecordatorioDAOImpl.java

│       │                   │       ├── EspecialidadDAOImpl.java

│       │                   │       ├── VacunaDAOImpl.java

│       │                   │       ├── ServicioDAOImpl.java

│       │                   │       └── AtencionMedicaDAOImpl.java

│       │

│       │                   ├── view/

│       │                   │   ← Interfaz gráfica Swing

│       │                   │   ├── frmComputadora.java

│       │                   │   ├── frmLogin.java

│       │                   │   ├── frmRegistrarme.java

│       │                   │   ├── frmSistema.java

│       │                   │   ├── frmCliente.java

│       │                   │   ├── frmMascota.java

│       │                   │   ├── frmSeleccionarCedula.java

│       │                   │   ├── frmBuscarInstrumento.java

│       │                   │   ├── frmBuscarMascota.java

│       │                   │   ├── frmBuscarMedicamento.java

│       │                   │   ├── frmCalendarioCitasMedicas.java

│       │                   │   ├── frmEditarFichaMedica.java

│       │                   │   ├── frmEditarRecordatorio.java

│       │                   │   ├── frmCita.java

│       │                   │   ├── frmMetodoPago.java

│       │                   │   ├── frmNotificacion.java

│       │                   │   ├── frmNuevoRecordatorio.java

│       │                   │   ├── frmVeterinario.java

│       │                   │   ├── frmServicio.java

│       │                   │   ├── frmAtencionMedica.java

│       │                   │   ├── frmHistorialMedico.java

│       │                   │   ├── frmFactura.java

│       │                   │   ├── frmDetalleFactura.java

│       │                   │   ├── frmReporte.java

│       │                   │   └── frmRecordatorio.java

│       │

│       │                   └── util/

│       │                       ← Clases utilitarias

│       │                       ├── DatabaseConnection.java

│       │                       ├── DatabaseUtil.java

│       │                       ├── Parametro.java

│       │                       └── RecordatorioTimer.java

│

│       └── resources/

│           ├── application.properties

│           │   ← Configuración Spring Boot (puerto, BD, etc.)

│           │

│           └── images/

│               ├── app_icon.png

│               ├── user.png

│               └── otros recursos gráficos

│

└── DOCUMENTACION/instalacion.md

---

Explicación del flujo (MVC clásico con DAO + Service + View):

1. Vista (view - Swing)

   El usuario interactúa con formularios como frmLogin, frmCliente, frmMascota, etc.

   Cuando el usuario realiza una acción (clic, guardar, buscar), la vista envía la solicitud.
2. Controlador (api/controller)

   El controlador recibe la petición (por ejemplo AuthController o ClienteController).

   Aquí se manejan endpoints REST o llamadas desde la interfaz.
3. Service (service)

   El controlador delega la lógica al Service correspondiente.

   El Service contiene las reglas de negocio, validaciones y procesos (por ejemplo: validar login, registrar cliente, generar factura).
4. DAO (model/dao + impl)

   El Service llama a los DAO para acceder a la base de datos.

   Las interfaces definen métodos y las implementaciones ejecutan SQL.
5. Entidades (model/entity)

   Los DAO trabajan con objetos del sistema (Usuario, Cliente, Mascota, etc.) que representan las tablas de la base de datos.
6. Respuesta

   El resultado vuelve en este orden:

   DAO → Service → Controller → View

Si es API:

Controller devuelve JSON (DTO).

Si es Swing:

La vista actualiza la interfaz con los datos.

Resumen del flujo:

View → Controller → Service → DAO → Base de datos

Base de datos → DAO → Service → Controller → View
