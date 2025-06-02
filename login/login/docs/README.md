# Modelo de Datos - Sistema GUZPASEN

Este directorio contiene diagramas y documentación sobre el modelo de datos del sistema GUZPASEN.

## Estructura del Modelo

El sistema se basa en un modelo de control de acceso por roles donde:

### Entidades Principales

1. **Usuario**
   - Representa a los usuarios del sistema (profesores, técnicos, administradores, etc.)
   - Tiene un rol principal obligatorio
   - Puede tener roles adicionales
   - Puede tener acceso directo a módulos específicos

2. **Rol**
   - Define perfiles como ADMINISTRADOR, PROFESOR, TECNICO, GESTOR_INCIDENCIAS
   - Cada rol tiene acceso a un conjunto de módulos
   - El rol ADMINISTRADOR tiene acceso a todos los módulos automáticamente

3. **Módulo**
   - Representa una funcionalidad o área del sistema
   - Puede estar activo o inactivo
   - Se asigna a roles y/o directamente a usuarios

4. **RolModulo**
   - Entidad de relación que conecta roles con módulos
   - Define qué módulos están disponibles para cada rol

### Relaciones

- **Usuario → Rol**: Cada usuario tiene un rol principal obligatorio
- **Usuario ↔ Roles**: Un usuario puede tener roles adicionales
- **Usuario ↔ Módulos**: Un usuario puede tener acceso directo a módulos específicos
- **Rol ↔ Módulos**: Un rol tiene acceso a varios módulos (a través de RolModulo)

## Diagramas Disponibles

1. **modelo-er.puml**: Diagrama Entidad-Relación que muestra las entidades y sus relaciones
2. **modelo-relacional.puml**: Diagrama del modelo relacional con las tablas y claves
3. **diagrama-clases.puml**: Diagrama de clases con atributos y métodos
4. **diagrama-flujo-permisos.puml**: Diagrama de flujo que explica cómo se verifica el acceso

## Flujo de Verificación de Permisos

Cuando un usuario intenta acceder a un módulo:

1. Se verifica que el usuario esté activo
2. Si el usuario tiene rol ADMINISTRADOR, se concede acceso automáticamente
3. Se verifica si el rol principal del usuario tiene acceso al módulo
4. Se verifica si el usuario tiene acceso directo al módulo 
5. Se verifica si alguno de los roles adicionales del usuario tiene acceso al módulo

Si alguna de estas condiciones es verdadera, se concede el acceso.

## Notas de Implementación

- Los usuarios inactivos no pueden acceder a ningún módulo
- Los módulos inactivos no son accesibles, incluso si están asignados a un rol o usuario
- El rol ADMINISTRADOR es especial y tiene acceso a todos los módulos del sistema
