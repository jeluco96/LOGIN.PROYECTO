# GUZPASEN - Sistema de Gestión

GUZPASEN es un sistema de gestión para el IES Torre de los Guzmanes que proporciona una interfaz web y una API REST para la administración de usuarios, roles y módulos.

## Características

- Autenticación basada en JWT para la API REST
- Autenticación basada en formularios para la interfaz web
- Gestión de usuarios, roles y permisos
- Interfaz web responsiva con Thymeleaf
- API REST documentada con Swagger/OpenAPI

## Requisitos

- Java 22 o superior
- Maven 3.6.3 o superior
- Base de datos (H2 en desarrollo, MySQL en producción)

## Configuración

### Desarrollo

```bash
# Clonar el repositorio
git clone https://github.com/usuario/guzpasen.git
cd guzpasen

# Ejecutar la aplicación (modo desarrollo)
./mvnw spring-boot:run
```

La aplicación estará disponible en http://localhost:8084

### Producción

```bash
# Compilar la aplicación
./mvnw clean package

# Ejecutar la aplicación (modo producción)
java -jar target/login-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

La aplicación estará disponible en https://localhost:8080

## Documentación API

La documentación de la API REST está disponible en Swagger UI:

- Desarrollo: http://localhost:8084/swagger-ui.html
- JSON: http://localhost:8084/api-docs

## Autenticación

### Interfaz Web

Acceder a `/auth/login` con el email y contraseña del usuario.

### API REST

1. Obtener token JWT:

```bash
curl -X POST http://localhost:8084/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "admin@guzpasen.edu", "clave": "password"}'
```

2. Usar el token en peticiones posteriores:

```bash
curl http://localhost:8084/api/usuarios \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

## Solución de problemas

### Problema de autenticación API

Si experimentas problemas con la autenticación JWT, verifica:

1. El token está siendo enviado correctamente en el header `Authorization: Bearer token`
2. El token no ha expirado (por defecto, 24 horas)
3. La clave secreta (`jwt.secret`) coincide entre el servicio que genera y el que valida el token

### Logs

Revisa los logs para diagnóstico detallado:

- Desarrollo: consola y `./logs/`
- Producción: `/var/log/guzpasen/application.log`

## Licencia

Este proyecto es propiedad del IES Torre de los Guzmanes © 2025.
