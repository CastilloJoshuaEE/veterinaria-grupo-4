MVC clásico (DAO + Service + View)

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
│       │                   │   │
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
│       │                   │
│       │                   │       ├── LoginRequest.java
│       │                   │       ├── LoginResponse.java
│       │                   │       └── ClienteDTO.java
│       │
│       │                   ├── service/
│       │                   │   ← Lógica de negocio
│       │                   │
│       │                   │   ├── AuthService.java
│       │                   │   ├── ClienteService.java
│       │                   │   ├── MascotaService.java
│       │                   │   ├── CitaService.java
│       │                   │   ├── VeterinarioService.java
│       │                   │   ├── ServicioService.java
│       │                   │   ├── AtencionMedicaService.java
│       │                   │   ├── FacturaService.java
│       │                   │   ├── HistorialService.java
│       │                   │   └── RecordatorioService.java
│       │
│       │                   ├── model/
│       │                   │   ├── entity/
│       │                   │   │   ← Entidades del sistema
│       │                   │   │
│       │                   │   │   ├── Usuario.java
│       │                   │   │   ├── Cliente.java
│       │                   │   │   ├── Mascota.java
│       │                   │   │   ├── FichaMedica.java
│       │                   │   │   ├── Veterinario.java
│       │                   │   │   ├── Cita.java
│       │                   │   │   ├── Servicio.java
│       │                   │   │   ├── AtencionMedica.java
│       │                   │   │   ├── Factura.java
│       │                   │   │   ├── DetalleFactura.java
│       │                   │   │   └── Recordatorio.java
│       │                   │
│       │                   │   ├── dao/
│       │                   │   │   ← Interfaces DAO
│       │                   │   │
│       │                   │   │   ├── IUsuarioDAO.java
│       │                   │   │   ├── IClienteDAO.java
│       │                   │   │   ├── IMascotaDAO.java
│       │                   │   │   ├── ICitaDAO.java
│       │                   │   │   ├── IServicioDAO.java
│       │                   │   │   ├── IAtencionMedicaDAO.java
│       │                   │   │   ├── IFacturaDAO.java
│       │                   │   │   ├── IHistorialDAO.java
│       │                   │   │   └── IRecordatorioDAO.java
│       │                   │
│       │                   │   └── impl/
│       │                   │       ← Implementaciones DAO
│       │                   │
│       │                   │       ├── UsuarioDAOImpl.java
│       │                   │       ├── ClienteDAOImpl.java
│       │                   │       ├── MascotaDAOImpl.java
│       │                   │       ├── CitaDAOImpl.java
│       │                   │       ├── ServicioDAOImpl.java
│       │                   │       ├── AtencionMedicaDAOImpl.java
│       │                   │       ├── FacturaDAOImpl.java
│       │                   │       ├── HistorialDAOImpl.java
│       │                   │       └── RecordatorioDAOImpl.java
│       │
│       │                   ├── view/
│       │                   │   ← Interfaz gráfica Swing
│       │                   │
│       │                   │   ├── frmComputadora.java
│       │                   │   ├── frmLogin.java
│       │                   │   ├── frmRegistrarme.java
│       │                   │   ├── frmSistema.java
│       │                   │   ├── frmCliente.java
│       │                   │   ├── frmMascota.java
│       │                   │   ├── frmCita.java
│       │                   │   ├── frmVeterinario.java
│       │                   │   ├── frmServicio.java
│       │                   │   ├── frmAtencionMedica.java
│       │                   │   ├── frmHistorialMedico.java
│       │                   │   ├── frmFactura.java
│       │                   │   ├── frmReporte.java
│       │                   │   └── frmRecordatorio.java
│       │
│       │                   └── util/
│       │                       ← Clases utilitarias
│       │
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
├── pom.xml                                          # Dependencias Maven
│
└── DOCUMENTACION/README.md
