# repositorio2025Fernandez

Instalación y puesta en funcionamiento
1. Base de datos
Abrir MySQL Workbench o consola MySQL
Ejecutar el script: scriptFernandez.sql

Esto creará:

Base de datos basefernandez
Tablas
Relaciones
Datos iniciales

2. Configuración de la API (Spring Boot)
Abrir el proyecto backend en IntelliJ IDEA
Configurar el archivo application.properties:
spring.datasource.url=jdbc:mysql://localhost:3306/basefernandez?useSSL=false&serverTimezone=UTC
spring.datasource.username=usuario
spring.datasource.password=contraseña
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
Ejecutar la aplicación

La API esta disponible en: http://localhost:8080/api

3. Aplicación Android
Abrir el proyecto en Android Studio
Esperar a la sincronización de Gradle
Configurar la URL base de la API en Retrofit:
Emulador: http://10.0.2.2:8080/
Dispositivo físico: http://IP_DEL_SERVIDOR:8080/
Ejecutar la aplicación
- Credenciales de acceso
Administrador
Email: admin@mecanicsync.com
Contraseña: admin123
Mecánico
Email: juan@mecanicsync.com
Contraseña: juan123
- Consideraciones importantes
La API debe estar en ejecución antes de iniciar la aplicación Android
El dispositivo debe estar conectado a la misma red que el servidor
Verificar la configuración de la URL de la API en caso de error de conexión

- Repositorio GitHub

El código fuente del proyecto también se encuentra disponible en:

https://github.com/sanfersor/repositorio2025Fernandez

- Notas finales
Los archivos ejecutables (.jar y .apk) no se incluyen en el repositorio ni en la entrega, ya que se generan automáticamente a partir del código fuente.
El proyecto ha sido desarrollado siguiendo buenas prácticas de arquitectura y separación de capas.

- Autor
Proyecto desarrollado por Sandra Fernández Sorribes
Ciclo Formativo de Desarrollo de Aplicaciones Multiplataforma (DAM)