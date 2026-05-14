package com.mycompany.veterinaria.grupo4.util;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase utilitaria para la gestion de la conexion a la base de datos.
 * <p>
 * Soporta tanto SQL Server como MySQL mediante configuracion en archivo properties.
 * Proporciona metodos estaticos para obtener y cerrar la conexion a la base de datos
 * db_veterinaria utilizando credenciales especificas.
 * Implementa un patron Singleton para mantener una unica instancia de conexion.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 2.0 (Soporta SQL Server y MySQL)
 * @since 1.0
 */
public class DatabaseConnection {

    private static String URL = null;
    private static String USER = null;
    private static String PASSWORD = null;
    private static String DRIVER = null;
    
    private static Connection connection = null;
    private static String dbType = null;
    private static boolean configuracionCargada = false;

    static {
        cargarConfiguracion();
    }

    /**
     * Busca el archivo database.properties en multiples ubicaciones
     */
    private static InputStream buscarArchivoProperties() {
        // 1. Buscar en classpath (src/main/resources)
        InputStream input = DatabaseConnection.class.getClassLoader()
                .getResourceAsStream("database.properties");
        if (input != null) {
            System.out.println("Archivo encontrado en classpath");
            return input;
        }
        
        // 2. Buscar en el directorio de recursos
        try {
            File file = new File("src/main/resources/database.properties");
            if (file.exists()) {
                System.out.println("Archivo encontrado en: " + file.getAbsolutePath());
                return new FileInputStream(file);
            }
        } catch (FileNotFoundException e) {
            // Ignorar
        }
        
        // 3. Buscar en el directorio actual
        try {
            File file = new File("database.properties");
            if (file.exists()) {
                System.out.println("Archivo encontrado en: " + file.getAbsolutePath());
                return new FileInputStream(file);
            }
        } catch (FileNotFoundException e) {
            // Ignorar
        }
        
        // 4. Buscar en config/ directory
        try {
            File file = new File("config/database.properties");
            if (file.exists()) {
                System.out.println("Archivo encontrado en: " + file.getAbsolutePath());
                return new FileInputStream(file);
            }
        } catch (FileNotFoundException e) {
            // Ignorar
        }
        
        return null;
    }

    /**
     * Carga la configuracion desde el archivo database.properties
     */
    private static void cargarConfiguracion() {
        if (configuracionCargada) {
            return;
        }
        
        try (InputStream input = buscarArchivoProperties()) {
            
            if (input == null) {
                // Si no encuentra el archivo, usar valores por defecto para desarrollo
                System.err.println("ADVERTENCIA: No se encontro el archivo database.properties");
                System.err.println("Usando valores por defecto para SQL Server...");
                
                // Valores por defecto para SQL Server
                dbType = "sqlserver";
                USER = "veterinaria_user";
                PASSWORD = "123456";
                URL = "jdbc:sqlserver://localhost:1433;databaseName=db_veterinaria;encrypt=true;trustServerCertificate=true;";
                DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                
                configuracionCargada = true;
                System.out.println("========================================");
                System.out.println("Configuracion de Base de Datos (DEFAULT):");
                System.out.println("  Tipo: " + dbType.toUpperCase());
                System.out.println("  URL: " + URL);
                System.out.println("  Usuario: " + USER);
                System.out.println("========================================");
                return;
            }
            
            Properties props = new Properties();
            props.load(input);
            
            // Obtener el tipo de base de datos (obligatorio)
            dbType = props.getProperty("db.type");
            if (dbType == null || dbType.trim().isEmpty()) {
                dbType = "sqlserver"; // valor por defecto
                System.out.println("Usando db.type por defecto: sqlserver");
            }
            
            dbType = dbType.toLowerCase().trim();
            
            // Obtener credenciales generales
            USER = props.getProperty("db.username", "veterinaria_user");
            PASSWORD = props.getProperty("db.password", "123456");
            
            // Configurar segun el tipo de base de datos
            if ("mysql".equals(dbType)) {
                // Configuracion para MySQL - AGREGAR noAccessToProcedureBodies=true
                String host = props.getProperty("mysql.host", "localhost");
                String port = props.getProperty("mysql.port", "3306");
                String database = props.getProperty("mysql.database", "db_veterinaria");
                
                // Parametros importantes para MySQL:
                // - useSSL=false: deshabilita SSL para conexion local
                // - allowPublicKeyRetrieval=true: permite recuperar clave publica
                // - serverTimezone=America/Guayaquil: zona horaria
                // - noAccessToProcedureBodies=true: EVITA EL ERROR DE PERMISOS EN SP
                URL = String.format("jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Guayaquil&noAccessToProcedureBodies=true",
                        host, port, database);
                DRIVER = "com.mysql.cj.jdbc.Driver";
                
                System.out.println("Configuracion MySQL con noAccessToProcedureBodies=true");
                
            } else {
                // Configuracion para SQL Server (default)
                String server = props.getProperty("sqlserver.server", "localhost");
                String port = props.getProperty("sqlserver.port", "1433");
                String database = props.getProperty("sqlserver.database", "db_veterinaria");
                String username = props.getProperty("sqlserver.username", USER);
                String password = props.getProperty("sqlserver.password", PASSWORD);
                
                // Si hay usuario especifico en sqlserver, usarlo
                if (username != null && !username.trim().isEmpty()) {
                    USER = username;
                }
                if (password != null && !password.trim().isEmpty()) {
                    PASSWORD = password;
                }
                
                URL = String.format("jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=true;trustServerCertificate=true;",
                        server, port, database);
                DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                dbType = "sqlserver";
            }
            
            configuracionCargada = true;
            System.out.println("========================================");
            System.out.println("Configuracion de Base de Datos cargada:");
            System.out.println("  Tipo: " + dbType.toUpperCase());
            System.out.println("  URL: " + URL);
            System.out.println("  Usuario: " + USER);
            System.out.println("========================================");
            
        } catch (Exception e) {
            System.err.println("ERROR FATAL: No se pudo cargar la configuracion de la base de datos");
            System.err.println("Motivo: " + e.getMessage());
            
            // Usar valores por defecto como ultimo recurso
            dbType = "sqlserver";
            USER = "veterinaria_user";
            PASSWORD = "123456";
            URL = "jdbc:sqlserver://localhost:1433;databaseName=db_veterinaria;encrypt=true;trustServerCertificate=true;";
            DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            configuracionCargada = true;
            
            System.out.println("========================================");
            System.out.println("Configuracion de Base de Datos (FALLBACK):");
            System.out.println("  Tipo: " + dbType.toUpperCase());
            System.out.println("  URL: " + URL);
            System.out.println("========================================");
        }
    }

    /**
     * Obtiene la conexion activa a la base de datos.
     * Si no existe conexion o esta cerrada, crea una nueva.
     *
     * @return conexion activa a la base de datos
     * @throws SQLException si ocurre un error al conectar
     */
    public static Connection getConnection() throws SQLException {
        if (!configuracionCargada) {
            cargarConfiguracion();
        }
        
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexion establecida con " + dbType.toUpperCase());
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver no encontrado: " + DRIVER + " - Verifique que la dependencia este en el pom.xml", e);
            } catch (SQLException e) {
                throw new SQLException("Error al conectar a la base de datos: " + e.getMessage(), e);
            }
        }
        return connection;
    }

    /**
     * Cierra la conexion a la base de datos si esta abierta.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Conexion cerrada");
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexion: " + e.getMessage());
            }
        }
    }
    
    /**
     * Obtiene el tipo de base de datos actual.
     * 
     * @return "sqlserver" o "mysql"
     */
    public static String getDbType() {
        return dbType;
    }
    
    /**
     * Verifica si la base de datos es MySQL.
     * 
     * @return true si es MySQL, false si es SQL Server
     */
    public static boolean isMySQL() {
        return "mysql".equals(dbType);
    }
    
    /**
     * Verifica si la base de datos es SQL Server.
     * 
     * @return true si es SQL Server, false si es MySQL
     */
    public static boolean isSQLServer() {
        return "sqlserver".equals(dbType);
    }
    
    /**
     * Verifica si la configuracion fue cargada correctamente.
     * 
     * @return true si la configuracion esta cargada
     */
    public static boolean isConfiguracionCargada() {
        return configuracionCargada;
    }
}