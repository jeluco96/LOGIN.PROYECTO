# Catálogo de Servicios - Sistema GUZPASEN

## Servicios de Autenticación

### Login de Usuario

| Aspecto | Detalle |
| ------- | ------- |
| **Ruta URL** | http://localhost:8084/auth/login |
| **Método** | POST |
| **Entrada** | Body: JSON {"email": "string", "clave": "string"} |
| **Modo de Acceso** | Postman |
| **Formato Respuesta** | JSON {"token": "string", "id": number, "nombre": "string", "apellidos": "string", "email": "string", "rol": "string"} |
| **Código Respuesta** | 200 OK, 401 Unauthorized, 400 Bad Request |
| **Descripción** | Autentica a un usuario mediante su email y contraseña. Devuelve un token JWT y la información básica del usuario. |
| **Dependencias** | Aplicación web, aplicación móvil |

### Cierre de Sesión

| Aspecto | Detalle |
| ------- | ------- |
| **Ruta URL** | http://localhost:8084/auth/logout |
| **Método** | POST |
| **Entrada** | Header: Authorization: Bearer {token} |
| **Modo de Acceso** | Postman |
| **Formato Respuesta** | JSON {"mensaje": "Sesión cerrada exitosamente", "timestamp": "fecha-hora"} |
| **Código Respuesta** | 200 OK, 500 Internal Server Error |
| **Descripción** | Cierra la sesión del usuario invalidando su token JWT. |
| **Dependencias** | Aplicación web, aplicación móvil |

## Servicios de Gestión de Usuarios

### Login de Usuario (Endpoint alternativo)

| Aspecto | Detalle |
| ------- | ------- |
| **Ruta URL** | http://localhost:8084/api/usuarios/login |
| **Método** | POST |
| **Entrada** | Body: JSON {"email": "string", "clave": "string"} |
| **Modo de Acceso** | Postman |
| **Formato Respuesta** | JSON (datos del usuario) |
| **Código Respuesta** | 200 OK, 401 Unauthorized |
| **Descripción** | Endpoint alternativo para autenticación de usuarios. |
| **Dependencias** | API REST |

### Listar Usuarios

| Aspecto | Detalle |
| ------- | ------- |
| **Ruta URL** | http://localhost:8084/api/usuarios |
| **Método** | GET |
| **Entrada** | Ninguna |
| **Modo de Acceso** | Postman |
| **Formato Respuesta** | JSON (Array de UsuarioDTO) |
| **Código Respuesta** | 200 OK |
| **Descripción** | Devuelve una lista de todos los usuarios del sistema. |
| **Dependencias** | Ninguna |

### Crear Usuario

| Aspecto | Detalle |
| ------- | ------- |
| **Ruta URL** | http://localhost:8084/api/usuarios |
| **Método** | POST |
| **Entrada** | Body: JSON {"nombre": "string", "apellidos": "string", "email": "string", "clave": "string", "rol": {"id": number}, "grupo": "string"} |
| **Modo de Acceso** | Postman (sin autenticación) |
| **Formato Respuesta** | JSON (UsuarioDTO) |
| **Código Respuesta** | 201 Created, 400 Bad Request |
| **Descripción** | Crea un nuevo usuario en el sistema con la información proporcionada. |
| **Dependencias** | Ninguna |

### Obtener Usuario por ID

| Aspecto | Detalle |
| ------- | ------- |
| **Ruta URL** | http://localhost:8084/api/usuarios/{id} |
| **Método** | GET |
| **Entrada** | Path Param: id (ejemplo: http://localhost:8084/api/usuarios/1) |
| **Modo de Acceso** | Postman (sin autenticación) |
| **Formato Respuesta** | JSON (UsuarioDTO) |
| **Código Respuesta** | 200 OK, 404 Not Found |
| **Descripción** | Obtiene la información detallada de un usuario específico por su ID. |
| **Dependencias** | Ninguna |

### Modificar Usuario

| Aspecto | Detalle |
| ------- | ------- |
| **Ruta URL** | http://localhost:8084/api/usuarios/{id} |
| **Método** | PUT |
| **Entrada** | Path Param: id, Body: JSON (datos del usuario a modificar) |
| **Modo de Acceso** | Postman (sin autenticación) |
| **Formato Respuesta** | JSON (UsuarioDTO) |
| **Código Respuesta** | 200 OK, 400 Bad Request |
| **Descripción** | Modifica los datos de un usuario existente. |
| **Dependencias** | Ninguna |

### Resetear Contraseña

| Aspecto | Detalle |
| ------- | ------- |
| **Ruta URL** | http://localhost:8084/api/usuarios/{id}/resetClave |
| **Método** | POST |
| **Entrada** | Path Param: id, Body: JSON {"nuevaClave": "string"} |
| **Modo de Acceso** | Postman (sin autenticación) |
| **Formato Respuesta** | Vacío |
| **Código Respuesta** | 200 OK, 400 Bad Request |
| **Descripción** | Permite resetear la contraseña de un usuario específico. |
| **Dependencias** | Ninguna |

### Desactivar Usuario

| Aspecto | Detalle |
| ------- | ------- |
| **Ruta URL** | http://localhost:8084/api/usuarios/{id}/desactivar |
| **Método** | POST |
| **Entrada** | Path Param: id |
| **Modo de Acceso** | Postman (sin autenticación) |
| **Formato Respuesta** | Vacío |
| **Código Respuesta** | 200 OK, 400 Bad Request |
| **Descripción** | Desactiva un usuario impidiendo su acceso al sistema. |
| **Dependencias** | Ninguna |

### Activar Usuario

| Aspecto | Detalle |
| ------- | ------- |
| **Ruta URL** | http://localhost:8084/api/usuarios/{id}/activar |
| **Método** | POST |
| **Entrada** | Path Param: id |
| **Modo de Acceso** | Postman (sin autenticación) |
| **Formato Respuesta** | Vacío |
| **Código Respuesta** | 200 OK, 400 Bad Request |
| **Descripción** | Activa un usuario permitiendo su acceso al sistema. |
| **Dependencias** | Ninguna |

### Listar Usuarios Activos

| Aspecto | Detalle |
| ------- | ------- |
| **Ruta URL** | http://localhost:8084/api/usuarios/activos |
| **Método** | GET |
| **Entrada** | Ninguna |
| **Modo de Acceso** | Postman (sin autenticación) |
| **Formato Respuesta** | JSON (Array de UsuarioDTO) |
| **Código Respuesta** | 200 OK |
| **Descripción** | Devuelve una lista de todos los usuarios activos del sistema. |
| **Dependencias** | Ninguna |

### Listar Usuarios por Rol

| Aspecto | Detalle |
| ------- | ------- |
| **Ruta URL** | http://localhost:8084/api/usuarios/rol/{rol} |
| **Método** | GET |
| **Entrada** | Path Param: rol |
| **Modo de Acceso** | Postman (sin autenticación) |
| **Formato Respuesta** | JSON (Array de UsuarioDTO) |
| **Código Respuesta** | 200 OK |
| **Descripción** | Devuelve una lista de usuarios filtrados por rol específico. |
| **Dependencias** | Ninguna |

### Listar Usuarios por Grupo

| Aspecto | Detalle |
| ------- | ------- |
| **Ruta URL** | http://localhost:8084/api/usuarios/grupo/{grupo} |
| **Método** | GET |
| **Entrada** | Path Param: grupo (ejemplo: INFORMATICA) |
| **Modo de Acceso** | Postman (sin autenticación) |
| **Formato Respuesta** | JSON (Array de UsuarioDTO) |
| **Código Respuesta** | 200 OK |
| **Descripción** | Devuelve una lista de usuarios filtrados por grupo específico. |
| **Dependencias** | Ninguna |

## Servicios de Gestión de Grupos

### Asignar Grupo a Usuario

| Aspecto | Detalle |
| ------- | ------- |
| **Ruta URL** | http://localhost:8084/api/usuarios/{id}/grupo |
| **Método** | POST |
| **Entrada** | Path Param: id, Body: JSON {"grupo": "string"} |
| **Modo de Acceso** | Postman (sin autenticación) |
| **Formato Respuesta** | Vacío |
| **Código Respuesta** | 200 OK, 400 Bad Request |
| **Descripción** | Asigna un grupo específico a un usuario. |
| **Dependencias** | Ninguna |

## Servicios de Gestión de Módulos

### Asignar Módulo a Usuario

| Aspecto | Detalle |
| ------- | ------- |
| **Ruta URL** | http://localhost:8084/api/usuarios/{idUsuario}/modulos/{idModulo} |
| **Método** | POST |
| **Entrada** | Path Params: idUsuario, idModulo |
| **Modo de Acceso** | Postman (sin autenticación) |
| **Formato Respuesta** | Vacío |
| **Código Respuesta** | 200 OK, 400 Bad Request |
| **Descripción** | Asigna un módulo específico a un usuario. |
| **Dependencias** | Ninguna |

### Quitar Módulo de Usuario

| Aspecto | Detalle |
| ------- | ------- |
| **Ruta URL** | http://localhost:8084/api/usuarios/{idUsuario}/modulos/{idModulo} |
| **Método** | DELETE |
| **Entrada** | Path Params: idUsuario, idModulo |
| **Modo de Acceso** | Postman (sin autenticación) |
| **Formato Respuesta** | Vacío |
| **Código Respuesta** | 200 OK, 400 Bad Request |
| **Descripción** | Quita un módulo específico de un usuario. |
| **Dependencias** | Ninguna |

### Obtener Módulos de Usuario

| Aspecto | Detalle |
| ------- | ------- |
| **Ruta URL** | http://localhost:8084/api/usuarios/{id}/modulos |
| **Método** | GET |
| **Entrada** | Path Param: id |
| **Modo de Acceso** | Postman (sin autenticación) |
| **Formato Respuesta** | JSON (Array de Modulo) |
| **Código Respuesta** | 200 OK, 404 Not Found |
| **Descripción** | Obtiene todos los módulos asignados a un usuario específico. |
| **Dependencias** | Ninguna |

### Validar Permiso de Usuario a Módulo

| Aspecto | Detalle |
| ------- | ------- |
| **Ruta URL** | http://localhost:8084/api/usuarios/{id}/permiso/{nombreModulo} |
| **Método** | GET |
| **Entrada** | Path Params: id, nombreModulo |
| **Modo de Acceso** | Postman (sin autenticación) |
| **Formato Respuesta** | JSON {"tienePermiso": boolean} |
| **Código Respuesta** | 200 OK, 404 Not Found |
| **Descripción** | Verifica si un usuario tiene permiso para acceder a un módulo específico. |
| **Dependencias** | Ninguna |

## Servicios de Documentación

### Swagger UI

| Aspecto | Detalle |
| ------- | ------- |
| **Ruta URL** | http://localhost:8084/swagger-ui.html |
| **Método** | GET |
| **Entrada** | Ninguna |
| **Modo de Acceso** | Navegador |
| **Formato Respuesta** | HTML |
| **Código Respuesta** | 200 OK |
| **Descripción** | Interfaz web para explorar y probar la API de forma interactiva. |
| **Dependencias** | Ninguna |

### API Docs

| Aspecto | Detalle |
| ------- | ------- |
| **Ruta URL** | http://localhost:8084/api-docs |
| **Método** | GET |
| **Entrada** | Ninguna |
| **Modo de Acceso** | Navegador, Postman |
| **Formato Respuesta** | JSON (OpenAPI spec) |
| **Código Respuesta** | 200 OK |
| **Descripción** | Especificación OpenAPI de la API en formato JSON. |
| **Dependencias** | Ninguna |
