-- ============================================================
--  MÓDULO 8 | Datos Iniciales (Seed)
-- ============================================================
USE db_veterinaria;
GO

-- ─── Usuarios ────────────────────────────────────────────────
INSERT INTO USUARIO (NOMBRE_USUARIO, CONTRASENA, CORREO_ELECTRONICO, ROL) VALUES
('JUAN',    '1234segura1',  'juan@email.com',    'ADMINISTRADOR'),
('MARCO',   '1234marco2',   'marco@email.com',   'ADMINISTRADOR'),
('MALCOLM', '1234MALCOLM3', 'malcolm@hotmail.com','ADMINISTRADOR');
GO

-- ─── Especialidades veterinarias ─────────────────────────────
INSERT INTO ESPECIALIDAD_VETERINARIA (NOMBRE_ESPECIALIDAD) VALUES
('Medicina General'),
('Cirugía Veterinaria'),
('Dermatología'),
('Cardiología'),
('Odontología'),
('Oftalmología'),
('Neurología'),
('Urgencias'),
('Nutrición'),
('Vacunación y Desparasitación');
GO

-- ─── Verificar especialidades ─────────────────────────────────
DECLARE @EspCount INT;
SELECT @EspCount = COUNT(*) FROM ESPECIALIDAD_VETERINARIA;
IF @EspCount = 0
    RAISERROR('No se insertaron las especialidades veterinarias', 16, 1);
ELSE
    PRINT CONCAT('Especialidades insertadas: ', @EspCount);
GO

-- ─── Veterinarios ────────────────────────────────────────────
BEGIN TRY
    INSERT INTO VETERINARIO
        (CEDULA, NOMBRE, APELLIDO, TELEFONO, PAGO_MENSUAL, DIRECCION, CORREO_ELECTRONICO, ID_ESPECIALIDAD)
    VALUES
    ('1010101010','Carlos',   'Jiménez',  '0991112233',  900.00,'Quito Norte',       'cjimenez@hotmail.com',  1),
    ('2020202020','Elena',    'Vera',     '0992223344', 1300.00,'Quito Sur',          'evera@hotmail.com',     2),
    ('3030303030','Luis',     'Chávez',   '0993334455', 1100.00,'Guayaquil Centro',   'lchavez@hotmail.com',   3),
    ('4040404040','Patricia', 'Ortega',   '0994445566',  950.00,'Cuenca Norte',       'portega@hotmail.com',   4),
    ('5050505050','Andrés',   'Morales',  '0995556677',  970.00,'Quito',              'amorales@hotmail.com',  5),
    ('6060606060','Lorena',   'Mejía',    '0996667788',  920.00,'Loja',               'lmejia@hotmail.com',    6),
    ('7070707070','Jorge',    'Vega',     '0997778899', 1000.00,'Riobamba',           'jvega@hotmail.com',     7),
    ('8080808080','Silvia',   'González', '0998889900', 1400.00,'Ambato',             'sgonzalez@hotmail.com', 8),
    ('9090909090','Mario',    'Castro',   '0999990011',  880.00,'Ibarra',             'mcastro@hotmail.com',   9),
    ('1111111111','Daniela',  'Pérez',    '0990001122',  870.00,'Manta',              'dperez@hotmail.com',   10);
    PRINT 'Veterinarios insertados correctamente.';
END TRY
BEGIN CATCH
    PRINT 'Error al insertar veterinarios: ' + ERROR_MESSAGE();
END CATCH;
GO

-- ─── Cliente de prueba ───────────────────────────────────────
INSERT INTO CLIENTE (CEDULA, NOMBRE, APELLIDO, TELEFONO, DIRECCION, CORREO_ELECTRONICO)
VALUES ('1234567890','Ana','Ramírez','0987654321','Av. Siempre Viva 123','ana.ramirez@email.com');
GO

-- ─── Medicamentos ────────────────────────────────────────────
INSERT INTO MEDICAMENTO (NOMBRE, DESCRIPCION, PRECIO, STOCK) VALUES
('Amoxicilina',       'Antibiótico de amplio espectro',          8.50, 100),
('Ivermectina',       'Antiparasitario interno y externo',       5.75, 200),
('Carprofeno',        'Analgésico y antiinflamatorio',          12.00,  75),
('Enrofloxacina',     'Antibacteriano veterinario',             10.00,  90),
('Prednisolona',      'Antiinflamatorio corticosteroide',        6.50,  80),
('Metronidazol',      'Antiparasitario y antibacteriano',        7.20, 120),
('Meloxicam',         'Analgésico para dolor e inflamación',     9.80, 100),
('Ciprofloxacina',    'Antibiótico bactericida',                 6.90, 150),
('Suero Ringer Lactato','Solución electrolítica',               4.00,  50),
('Ketamina',          'Anestésico general para cirugías',       18.00,  60);
GO

-- ─── Instrumentos médicos ────────────────────────────────────
INSERT INTO INSTRUMENTO_MEDICO (NOMBRE, DESCRIPCION, COSTO_USO) VALUES
('Estetoscopio',         'Para auscultación cardíaca y pulmonar',  0.50),
('Termómetro Digital',   'Medición de temperatura corporal',       0.30),
('Bisturí',              'Corte durante cirugías o procedimientos', 1.00),
('Pinzas Hemostáticas',  'Detención de sangrado',                   0.80),
('Jeringuilla',          'Aplicación de medicamentos o vacunas',    0.20),
('Tijeras Quirúrgicas',  'Corte de tejidos o vendas',               0.40),
('Lámpara de Exploración','Iluminación durante exámenes',           0.60),
('Otoscopio',            'Exploración de canal auditivo',           0.70),
('Balanza Digital',      'Medición de peso del animal',             0.25),
('Microscopio',          'Análisis de muestras biológicas',         2.00);
GO

-- ─── Servicios ───────────────────────────────────────────────
INSERT INTO SERVICIO (NOMBRE_SERVICIO, DESCRIPCION, PRECIO, DURACION_ESTIMADA, ESTADO) VALUES
('Consulta General de Salud',    'Revisión general de salud',          25.00, 30, 1),
('Cirugía de Esterilización',    'Esterilización quirúrgica',         100.00, 90, 1),
('VACUNA',                       'Aplicación de vacunas',              10.00, 10, 1),
('Tratamiento Pulgas/Garrapatas','Desparasitación externa',            15.00, 20, 1),
('Consulta Dolor o Lesión',      'Evaluación de dolor',                30.00, 30, 1),
('Desparasitación Interna',      'Eliminar parásitos intestinales',    12.00, 15, 1),
('Limpieza Dental',              'Limpieza y revisión dental',         40.00, 45, 1),
('Consulta Dermatológica',       'Enfermedades de la piel',            35.00, 30, 1),
('Control Nutricional',          'Asesoramiento alimenticio',          20.00, 20, 1),
('Emergencia Veterinaria',       'Atención fuera de horario',          50.00, 60, 1);
GO

-- ─── Asignar servicios a veterinarios ────────────────────────
BEGIN TRY
    INSERT INTO SERVICIO_VETERINARIO (ID_SERVICIO, ID_VETERINARIO) VALUES
    (1, 1), (2, 2), (8, 3), (4, 4), (7, 5),
    (6, 6), (5, 7), (10,8), (9, 9), (3,10);
    PRINT 'Servicios asignados a veterinarios correctamente.';
END TRY
BEGIN CATCH
    PRINT 'Error al asignar servicios: ' + ERROR_MESSAGE();
END CATCH;
GO

-- ─── Configuración de recordatorios ─────────────────────────
INSERT INTO RECORDATORIO_CONFIG (TIPO_RECORDATORIO, ANTICIPACION, MENSAJE) VALUES
('CITA',   '15_DIAS',     'Tiene una cita pendiente dentro de 15 días'),
('CITA',   '7_DIAS',      'Tiene una cita pendiente dentro de 7 días'),
('CITA',   '1_MES',       'Tiene una cita pendiente dentro de 1 mes'),
('CITA',   '12_HORAS',    'Tiene una cita pendiente dentro de 12 horas'),
('CITA',   '10_HORAS',    'Tiene una cita pendiente dentro de 10 horas'),
('CITA',   '5_MINUTOS',   'Tiene una cita pendiente dentro de 5 minutos'),
('CITA',   '30_SEGUNDOS', 'Tiene una cita pendiente dentro de 30 segundos'),
('VACUNA', '15_DIAS',     'Vacunación próxima dentro de 15 días'),
('VACUNA', '7_DIAS',      'Vacunación próxima dentro de 7 días'),
('VACUNA', '1_MES',       'Vacunación próxima dentro de 1 mes'),
('VACUNA', '12_HORAS',    'Vacunación próxima dentro de 12 horas'),
('VACUNA', '10_HORAS',    'Vacunación próxima dentro de 10 horas'),
('VACUNA', '5_MINUTOS',   'Vacunación próxima dentro de 5 minutos'),
('VACUNA', '30_SEGUNDOS', 'Vacunación próxima dentro de 30 segundos');
GO

-- ─── Datos de ejemplo: mascota, cita y atención ──────────────
BEGIN TRY
    BEGIN TRANSACTION;

    DECLARE @ID_CLIENTE INT, @ID_MASCOTA INT, @ID_CITA INT, 
            @ID_ATENCION INT, @ID_METODO_PAGO INT, @ID_FACTURA INT;

    SELECT @ID_CLIENTE = ID_CLIENTE FROM CLIENTE WHERE CEDULA = '1234567890';
    
    -- 1. Mascota
    INSERT INTO MASCOTA (ID_CLIENTE, NOMBRE, ESPECIE, RAZA, SEXO, FECHA_NACIMIENTO, PESO, COLOR)
    VALUES (@ID_CLIENTE, 'Max', 'Perro', 'Labrador', 'M', '2020-09-22', 25.5, 'Marrón');
    SET @ID_MASCOTA = SCOPE_IDENTITY();

    -- 2. Cita (Ahora con Mascota y Veterinario incluidos desde el inicio)
    -- Usamos el veterinario 10 (Daniela Pérez) para vacunación (Servicio 3)
    INSERT INTO CITA (ID_CLIENTE, ID_MASCOTA, ID_SERVICIO, ID_VETERINARIO, FECHA_HORA, OBSERVACIONES, ESTADO)
    VALUES (@ID_CLIENTE, @ID_MASCOTA, 3, 10, GETDATE(), 'Vacunación anual', 'PENDIENTE');
    SET @ID_CITA = SCOPE_IDENTITY();

    -- 3. Vacuna (Usamos el SP que ya calcula la fecha próxima)
    EXEC SP_REGISTRAR_VACUNA_APLICADA 
        @ID_MASCOTA = @ID_MASCOTA, 
        @NOMBRE = 'Rabia', 
        @DESCRIPCION = 'Protección contra el virus de la rabia', 
        @PERIODO_MESES = 12, 
        @FECHA_APLICACION = '2023-09-20';

    -- 4. Atención médica (Solo campos necesarios, el Trigger pondrá la Cita en 'REALIZADA')
    INSERT INTO ATENCION_MEDICA (ID_CITA, FECHA, DIAGNOSTICO, TRATAMIENTO, OBSERVACIONES)
    VALUES (@ID_CITA, GETDATE(), 'Vacunación anual', 'Vacuna antirrábica aplicada', 'Mascota en buen estado');
    SET @ID_ATENCION = SCOPE_IDENTITY();

    -- 5. Facturación
    INSERT INTO METODO_PAGO (METODO, VALOR_TOTAL) VALUES ('EFECTIVO', 11.20);
    SET @ID_METODO_PAGO = SCOPE_IDENTITY();

    INSERT INTO FACTURA (ID_CLIENTE, SUBTOTAL, IVA, TOTAL, ESTADO, ID_METODO_PAGO)
    VALUES (@ID_CLIENTE, 10.00, 1.20, 11.20, 'PAGADA', @ID_METODO_PAGO);
    SET @ID_FACTURA = SCOPE_IDENTITY();

    -- 6. Detalle Factura (Sin ID_MASCOTA redundante)
    INSERT INTO DETALLE_FACTURA (ID_FACTURA, ID_SERVICIO, CANTIDAD, PRECIO_UNITARIO, DESCUENTO, TOTAL, ID_ATENCION_MEDICA)
    VALUES (@ID_FACTURA, 3, 1, 10.00, 0.00, 10.00, @ID_ATENCION);

    COMMIT TRANSACTION;
    PRINT 'Datos de ejemplo insertados correctamente siguiendo el modelo POO.';
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    PRINT 'Error en datos de ejemplo: ' + ERROR_MESSAGE() + ' (Línea ' + CAST(ERROR_LINE() AS VARCHAR) + ')';
END CATCH;
GO

-- ─── Crear usuario de base de datos y asignar permisos ─────────
IF EXISTS (SELECT * FROM sys.server_principals WHERE name = 'veterinaria_user')
BEGIN
    DROP LOGIN veterinaria_user;
END
GO
CREATE LOGIN veterinaria_user WITH PASSWORD = '123456';
USE db_veterinaria;
CREATE USER veterinaria_user FOR LOGIN veterinaria_user;
ALTER ROLE db_owner ADD MEMBER veterinaria_user; 
GO
-- Nueva vacuna aplicada --
DECLARE @ID_MASCOTA INT;
SELECT @ID_MASCOTA = ID_MASCOTA FROM MASCOTA WHERE NOMBRE = 'Max';

IF @ID_MASCOTA IS NULL
    SELECT @ID_MASCOTA = MIN(ID_MASCOTA) FROM MASCOTA;

PRINT CONCAT('ID de la mascota: ', @ID_MASCOTA);
GO
-- INSERTAR VACUNA CON FECHA PRÓXIMA A VENCER
DECLARE @ID_MASCOTA INT;
SELECT @ID_MASCOTA = ID_MASCOTA FROM MASCOTA WHERE NOMBRE = 'Max';

IF @ID_MASCOTA IS NULL
    SELECT @ID_MASCOTA = MIN(ID_MASCOTA) FROM MASCOTA;

INSERT INTO VACUNA_APLICADA (ID_MASCOTA, NOMBRE, DESCRIPCION, PERIODO_MESES, FECHA_APLICACION, FECHA_PROXIMA)
VALUES (
    @ID_MASCOTA,
    'Vacuna Séxtuple Canina',
    'Protección contra moquillo, hepatitis, leptospirosis, parvovirus, parainfluenza',
    12,
    DATEADD(DAY, -350, GETDATE()),   -- Hace ~350 días
    DATEADD(DAY, 15, GETDATE())      -- En 15 días (PRÓXIMA A VENCER)
);

PRINT 'Vacuna insertada correctamente.';
GO
-- ─── Resumen final ───────────────────────────────────────────
PRINT '──────────────────────────────────────';
PRINT 'Resumen de objetos creados:';
SELECT type_desc AS TIPO, COUNT(*) AS CANTIDAD
FROM sys.objects
WHERE type IN ('U','P','TR')
GROUP BY type_desc
ORDER BY type_desc;
GO
