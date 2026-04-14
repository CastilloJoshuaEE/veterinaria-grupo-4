Procurar ejecutar el archivo 00_MAIN.sql para preparar la base de datos en sql server:

![1775881260894](image/instalacion/1775881260894.png)

Abre CMD o PowerShell en la carpeta del proyecto:

cd "D:\Users\Usuario\Documents\octavo semestre\VERIFICACION Y VALIDACION DE SOFTWARE\UNIDAD 1\PROYECTO PRIMER PARCIAL\proyecto\veterinaria-grupo-4\veterinaria-grupo4"

---

## 1. Verificar Maven

mvn -v

---

Si no funciona:

## 1. Descargar Maven

Ve a:

[https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)

Descarga:

Binary zip archive (ejemplo: apache-maven-3.9.x-bin.zip)

---

## 2. Extraer Maven

Extraer en una ruta simple, por ejemplo:

C:\maven

Debe quedar así:

C:\maven\apache-maven-3.9.x\

---

## 3. Configurar variables de entorno

Abrir:

Buscar en Windows → “Variables de entorno”

---

### Crear variable MAVEN_HOME

Nombre:

MAVEN_HOME

Valor:

C:\maven\apache-maven-3.9.x

---

### Editar variable PATH

Agregar esta ruta:

C:\maven\apache-maven-3.9.x\bin

---

## 4. Reiniciar terminal

Cerrar CMD o PowerShell y abrir uno nuevo

---

## 5. Verificar instalación

mvn -v

Debe mostrar algo como:

Apache Maven 3.9.x

Java version: 17

---

## 6. Descargar dependencias del proyecto

En la carpeta del proyecto ejecutar:

mvn clean install

---

## 7. Ejecutar el proyecto Spring Boot

mvn spring-boot:run

---

## 7. Ejecutar la aplicación desde la carpeta raíz por línea de comandos de windows (opcional)

mvn spring-boot:run

---

## 8. URL de ejecución

[http://localhost:8080](http://localhost:8080)

---

## 9. Probar endpoint

POST [http://localhost:8080/cliente/login](http://localhost:8080/cliente/login)

---

## 10. Problemas de conexión con SQL Server

Verificar en SQL Server Configuration Manager:

SQL Server Network Configuration

→ Protocols for MSSQLSERVER

### Habilitar TCP/IP

TCP/IP debe estar:

Enabled

## 11. Configuración de puerto

Ir a:

TCP/IP → IP Addresses → IPAll

Configurar:

TCP Dynamic Ports = (vacío)

TCP Port = 1433

![1775876064255](image/instalacion/1775876064255.png)

## 12. Reiniciar servicio SQL Server

SQL Server Services

→ SQL Server (MSSQLSERVER)

→ Restart

---

## 13. Probar conexión SQL Server por consola

sqlcmd -S TU_SERVIDOR_SQL_SERVER,1433 -E -C

Si funciona debe aparecer:

1>

---

## 14. Ejecutar en NetBeans IDE 26

Usar botón verde (Run Project)

El proyecto debe ser ejecutado como proyecto Maven/Spring BooT como “Run File”, ejecutar directamente con el botón verde, procure tener la siguiente configuración

![1775880575906](image/instalacion/1775880575906.png)

## 15. Comando final de ejecución manual en windows (Opcional) usar la carpeta raíz

D:\Users\Usuario\Documents\octavo semestre\VERIFICACION Y VALIDACION DE SOFTWARE\UNIDAD 1\PROYECTO PRIMER PARCIAL\proyecto\veterinaria-grupo-4\veterinaria-grupo4> mvn spring-boot:run

NOTA: RECORDAR ACTUALIZAR EL NOMBRE DE: "TU_SERVIDOR" por el vuestro de SQL SERVER. tanto en database.properties como en el DatabaseConnection.java
