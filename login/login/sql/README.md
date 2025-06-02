# Scripts SQL para el Sistema GUZPASEN

Este directorio contiene los scripts SQL necesarios para crear y poblar la base de datos del sistema de gestión de usuarios y accesos GUZPASEN.

## Archivos disponibles

- `schema.sql`: Script para crear la estructura de la base de datos (tablas, índices, relaciones).
- `data.sql`: Script para insertar datos de prueba en la base de datos.

## Instrucciones de uso

### Configuración inicial

1. Asegúrate de tener MySQL instalado y en ejecución.
2. Crea una base de datos llamada `login` si no utilizas el script completo:
   ```sql
   CREATE DATABASE IF NOT EXISTS login;
   ```

### Ejecución de los scripts

#### Opción 1: Desde MySQL Command Line

```bash
mysql -u root -p < schema.sql
mysql -u root -p < data.sql
```

#### Opción 2: Desde MySQL Workbench

1. Abre MySQL Workbench y conéctate a tu servidor.
2. Abre los archivos `schema.sql` y `data.sql`.
3. Ejecuta primero `schema.sql` y luego `data.sql`.

## Estructura de la base de datos

### Tablas principales

- `rol`: Roles disponibles en el sistema
- `modulo`: Módulos o funcionalidades del sistema
- `usuario`: Usuarios registrados en el sistema
- `grupo`: Agrupaciones de usuarios (departamentos)

### Tablas de relación

- `rol_modulo`: Relación entre roles y los módulos a los que tienen acceso
- `usuario_modulo`: Módulos adicionales asignados específicamente a usuarios
- `usuario_rol`: Roles adicionales asignados a usuarios
- `usuario_grupo`: Relación entre usuarios y los grupos a los que pertenecen

## Datos de prueba

### Credenciales de usuario

Todos los usuarios creados tienen la misma contraseña para facilitar las pruebas:

- **Contraseña**: `password`

Usuarios disponibles:

| Email                       | Rol              | Nombre | Apellidos       |
|-----------------------------|------------------|--------|----------------|
| admin@guzpasen.edu          | ADMINISTRADOR    | Admin  | Sistema        |
| maria.gonzalez@guzpasen.edu | PROFESOR         | María  | González López |
| juan.rodriguez@guzpasen.edu | PROFESOR+TECNICO | Juan   | Rodríguez Martín |
| laura.fernandez@guzpasen.edu| PROFESOR         | Laura  | Fernández Ruiz |
| carlos.perez@guzpasen.edu   | GESTOR_INCIDENCIAS | Carlos | Pérez Díaz    |
| ana.lopez@guzpasen.edu      | TECNICO+GESTOR   | Ana    | López García   |

## Notas importantes

- La contraseña almacenada está hasheada con BCrypt.
- El usuario `miguel.sanchez@guzpasen.edu` está marcado como inactivo para pruebas.
- Algunos usuarios tienen acceso a la aplicación móvil (campo `usuario_movil`).
