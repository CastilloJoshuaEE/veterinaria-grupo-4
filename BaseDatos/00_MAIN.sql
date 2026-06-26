-- ============================================================
--  db_veterinaria  |  ARCHIVO PRINCIPAL DE DESPLIEGUE
--  GRUPO 4
-- ============================================================
USE master;
GO
-- =====================================================
-- CONFIGURACIÓN DE RUTA BASE
-- =====================================================
-- Si ejecutas desde CMD (sqlcmd):
--   → deja BasePath vacío y solo ejecuta en el CMD, desde la ruta donde esta el archivo:
--   → sqlcmd -S . -E -i 00_MAIN.sql
-- Si ejecutas desde SSMS, Modo SQLCMD requerido Query > SQLCMD Mode  (Ctrl+Shift+Q)
--   → colocar en BASE_PATH la ruta absoluta a la carpeta BaseDatos
--		Ejemplo:
--		:setvar BASE_PATH "C:\PROYECTOS\veterinaria-grupo-4\BaseDatos\"
--   → Importante: No olvidar el "\" al final
-- =====================================================
:setvar BasePath "C:\PROYECTOS\veterinaria-grupo-4\BaseDatos\"


-- ============================================================
--  CREAR BASE DE DATOS (solo si no existe)
-- ============================================================
IF NOT EXISTS (SELECT 1 FROM sys.databases WHERE name = 'db_veterinaria')
BEGIN
    CREATE DATABASE db_veterinaria;
    PRINT 'Base de datos db_veterinaria creada.';
END
ELSE
    PRINT 'Base de datos db_veterinaria ya existe.';
GO

USE db_veterinaria;
GO

-- ============================================================
--  MÓDULO 1 | Esquema de tablas
-- ============================================================
PRINT '>> Ejecutando: 01_Esquema.sql';
:r $(BasePath)01_Esquema.sql

-- ============================================================
--  MÓDULO 2 | SPs de Usuarios, Clientes y Veterinarios
-- ============================================================
PRINT '>> Ejecutando: 02_SP_Usuarios_Clientes_Vets.sql';
:r $(BasePath)02_SP_Usuarios_Clientes_Vets.sql

-- ============================================================
--  MÓDULO 3 | SPs de Mascotas y Citas
-- ============================================================
PRINT '>> Ejecutando: 03_SP_Mascotas_Citas.sql';
:r $(BasePath)03_SP_Mascotas_Citas.sql

-- ============================================================
--  MÓDULO 4 | SPs de Atención Médica, Instrumentos y Medicamentos
-- ============================================================
PRINT '>> Ejecutando: 04_SP_Atencion_Medica.sql';
:r $(BasePath)04_SP_Atencion_Medica.sql

-- ============================================================
--  MÓDULO 5 | SPs de Servicios y Facturas
-- ============================================================
PRINT '>> Ejecutando: 05_SP_Servicios_Facturas.sql';
:r $(BasePath)05_SP_Servicios_Facturas.sql

-- ============================================================
--  MÓDULO 6 | SPs de Vacunas, Alertas y Recordatorios
-- ============================================================
PRINT '>> Ejecutando: 06_SP_Vacunas_Alertas.sql';
:r $(BasePath)06_SP_Vacunas_Alertas.sql

-- ============================================================
--  MÓDULO 7 | Historial Médico
-- ============================================================
PRINT '>> Ejecutando: 07_SP_Historial_Medico.sql';
:r $(BasePath)07_SP_Historial_Medico.sql

-- ============================================================
--  MÓDULO 8 | Datos iniciales (seed)
-- ============================================================
PRINT '>> Ejecutando: 08_Datos_Iniciales.sql';
:r $(BasePath)08_Datos_Iniciales.sql

-- ============================================================
PRINT '>> Despliegue completo de db_veterinaria finalizado.';
GO
