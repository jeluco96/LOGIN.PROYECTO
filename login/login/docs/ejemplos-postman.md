# Ejemplos de Peticiones para Postman - API GUZPASEN

## Autenticación

### 1. Login de Usuario
```http
POST http://localhost:8084/auth/login
Content-Type: application/json

{
    "email": "admin@example.com",
    "clave": "password123"
}
```

### 2. Cerrar Sesión
```http
POST http://localhost:8084/auth/logout
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## Gestión de Usuarios

### 3. Crear Usuario
```http
POST http://localhost:8084/api/usuarios
Content-Type: application/json

{
    "nombre": "Juan",
    "apellidos": "Pérez García",
    "email": "juan.perez@example.com",
    "clave": "password123",
    "rol": {
        "id": 2
    },
    "grupo": "INFORMATICA"
}
```

### 4. Listar Todos los Usuarios
```http
GET http://localhost:8084/api/usuarios
```

### 5. Obtener Usuario por ID
```http
GET http://localhost:8084/api/usuarios/1
```

### 6. Modificar Usuario
```http
PUT http://localhost:8084/api/usuarios/1
Content-Type: application/json

{
    "nombre": "Juan Carlos",
    "apellidos": "Pérez García",
    "email": "juancarlos.perez@example.com",
    "rol": {
        "id": 2
    },
    "grupo": "CONTABILIDAD",
    "activo": true
}
```

### 7. Resetear Contraseña
```http
POST http://localhost:8084/api/usuarios/1/resetClave
Content-Type: application/json

{
    "nuevaClave": "nuevaPassword123"
}
```

### 8. Desactivar Usuario
```http
POST http://localhost:8084/api/usuarios/1/desactivar
```

### 9. Activar Usuario
```http
POST http://localhost:8084/api/usuarios/1/activar
```

### 10. Listar Usuarios Activos
```http
GET http://localhost:8084/api/usuarios/activos
```

### 11. Listar Usuarios por Rol
```http
GET http://localhost:8084/api/usuarios/rol/1
```

### 12. Listar Usuarios por Grupo
```http
GET http://localhost:8084/api/usuarios/grupo/INFORMATICA
```

## Gestión de Grupos

### 13. Asignar Grupo a Usuario
```http
POST http://localhost:8084/api/usuarios/1/grupo
Content-Type: application/json

{
    "grupo": "SISTEMAS"
}
```

## Gestión de Módulos

### 14. Asignar Módulo a Usuario
```http
POST http://localhost:8084/api/usuarios/1/modulos/3
```

### 15. Quitar Módulo de Usuario
```http
DELETE http://localhost:8084/api/usuarios/1/modulos/3
```

### 16. Obtener Módulos de Usuario
```http
GET http://localhost:8084/api/usuarios/1/modulos
```

### 17. Validar Permiso a Módulo
```http
GET http://localhost:8084/api/usuarios/1/permiso/GESTION_USUARIOS
```

## Documentación

### 18. Acceder a Swagger UI
```http
GET http://localhost:8084/swagger-ui.html
```

### 19. Obtener Especificación OpenAPI
```http
GET http://localhost:8084/api-docs
```

## Consejos para Postman

1. Crea una variable de entorno `baseUrl` con el valor `http://localhost:8084` para facilitar el cambio de entorno.

2. Después de hacer login, guarda el token JWT en una variable de colección:
   ```javascript
   var jsonData = pm.response.json();
   pm.collectionVariables.set("token", jsonData.token);
   ```

3. Configura la autorización en las peticiones que lo requieran:
   - Tipo: Bearer Token
   - Token: {{token}}

4. Para peticiones con parámetros dinámicos, usa variables de entorno:
   ```
   GET {{baseUrl}}/api/usuarios/{{userId}}/modulos
   ```
