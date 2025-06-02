# GUZPASEN - Sistema de Autenticación

Sistema de autenticación y control de acceso para GUZPASEN.

## Requisitos

- Java 22
- Maven 3.8+
- MySQL 8+

## Configuración

1. Clona el repositorio
2. Configura la base de datos en `application.properties`
3. Ejecuta la aplicación con `mvn spring-boot:run`

## Documentación de la API

La API está documentada utilizando Swagger/OpenAPI. Para acceder a la documentación interactiva:

1. Inicia la aplicación
2. Abre en tu navegador: http://localhost:8084/swagger-ui.html

## Colección de Postman

Se incluye una colección de Postman para probar la API:

1. Abre Postman
2. Importa el archivo `postman-collection.json`
3. Configura la variable de entorno `baseUrl` a `http://localhost:8084`
4. Prueba la API usando las solicitudes predefinidas

## Flujo de autenticación

1. Realiza login con tus credenciales en `/auth/login`
2. Guarda el token JWT que recibes en la respuesta
3. Incluye el token en las cabeceras de solicitudes posteriores como `Authorization: Bearer [token]`

## Estructura del proyecto

- `controller`: Controladores REST que definen los endpoints de la API
- `model`: Entidades JPA que representan el modelo de datos
- `repository`: Interfaces para acceso a datos
- `service`: Servicios que implementan la lógica de negocio
- `security`: Componentes relacionados con seguridad y autenticación
- `dto`: Objetos de transferencia de datos
