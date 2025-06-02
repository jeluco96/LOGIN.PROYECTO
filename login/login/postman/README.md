# Guía para Pruebas con Postman - API GUZPASEN

Este directorio contiene los recursos necesarios para probar la API RESTful de GUZPASEN usando Postman.

## Contenido

- `GUZPASEN_API.postman_collection.json`: Colección de Postman con todas las peticiones preconfiguradas.
- `GUZPASEN_DEV.postman_environment.json`: Archivo de entorno para desarrollo local.

## Instalación y configuración

1. Descarga e instala [Postman](https://www.postman.com/downloads/) si aún no lo tienes.
2. Abre Postman y sigue estos pasos:

### Importar la colección

1. Haz clic en el botón "Import" en la esquina superior izquierda.
2. Arrastra y suelta el archivo `GUZPASEN_API.postman_collection.json` o navega para seleccionarlo.
3. Confirma la importación.

## Estructura de la colección

La colección está organizada en las siguientes carpetas:

- **Autenticación**: Endpoints para iniciar sesión y gestionar tokens.
- **Usuarios**: CRUD completo de usuarios y operaciones específicas.
- **Roles**: Gestión de roles y sus permisos.
- **Módulos**: Administración de módulos del sistema.
- **Grupos**: Gestión de grupos y asignación de usuarios.

## Flujo básico de prueba

1. **Iniciar sesión**: Ejecuta primero la petición "Login" para obtener un token JWT.
   - La respuesta incluirá un token que se guardará automáticamente en la variable de colección `token`.

2. **Realizar peticiones**: Una vez autenticado, puedes ejecutar cualquier otra petición.
   - Todas las peticiones están configuradas para usar automáticamente el token almacenado.

3. **Cerrar sesión**: Al finalizar, puedes ejecutar la petición "Logout" para invalidar el token actual.

## Ejemplos de uso

### Autenticación

```
POST {{baseUrl}}/auth/login
Body: {
    "email": "admin@guzpasen.edu",
    "clave": "password"
}
```

### Obtener información de usuario

```
GET {{baseUrl}}/usuarios/1
```

### Crear nuevo usuario

```
POST {{baseUrl}}/usuarios
Body: {
    "nombre": "Nuevo",
    "apellidos": "Usuario",
    "email": "nuevo@guzpasen.edu",
    "clave": "password",
    "rol": {
        "id": 2
    },
    "grupo": "INFORMATICA"
}
```

## Pruebas automatizadas

La colección incluye scripts de prueba que verifican automáticamente:

- Códigos de estado correctos (200, 201, 204)
- Formato JSON válido en las respuestas
- Almacenamiento automático del token JWT

## Solución de problemas

- **Error 401 Unauthorized**: Asegúrate de haber ejecutado primero la petición de login.
- **Error 403 Forbidden**: El usuario autenticado no tiene permisos para ese recurso.
- **Error 404 Not Found**: Verifica que el ID o ruta sean correctos.
- **Error 500 Internal Server Error**: Revisa los logs del servidor para más detalles.

## Personalización

Puedes ajustar la URL base y otras variables del entorno editando las variables de colección o creando tu propio entorno personalizado.
