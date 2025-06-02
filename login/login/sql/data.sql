-- Script para insertar datos de prueba en el sistema de login
-- Versión 1.0

USE login;

-- Insertar roles básicos
INSERT INTO rol (nombre, descripcion) VALUES
('ADMINISTRADOR', 'Rol con acceso completo a todos los módulos del sistema'),
('PROFESOR', 'Rol para docentes con acceso a módulos académicos'),
('GESTOR_INCIDENCIAS', 'Rol para personal que gestiona tickets e incidencias'),
('TECNICO', 'Rol para personal de soporte técnico');

-- Insertar módulos del sistema
INSERT INTO modulo (nombre, descripcion, ruta, icono, activo) VALUES
('DASHBOARD', 'Panel principal con resumen de actividad', '/dashboard', 'dashboard', 1),
('USUARIOS', 'Gestión de usuarios del sistema', '/usuarios', 'people', 1),
('ROLES', 'Administración de roles y permisos', '/roles', 'security', 1),
('MODULOS', 'Configuración de módulos del sistema', '/modulos', 'extension', 1),
('GRUPOS', 'Gestión de grupos de usuarios', '/grupos', 'group_work', 1),
('INCIDENCIAS', 'Sistema de tickets e incidencias', '/incidencias', 'report_problem', 1),
('CALENDARIO', 'Calendario de eventos y actividades', '/calendario', 'calendar_today', 1),
('REPORTES', 'Generación de informes y estadísticas', '/reportes', 'assessment', 1),
('CONFIGURACION', 'Configuración general del sistema', '/configuracion', 'settings', 1),
('AYUDA', 'Sistema de ayuda y documentación', '/ayuda', 'help', 1);

-- Insertar grupos
INSERT INTO grupo (nombre, descripcion) VALUES
('INFORMATICA', 'Departamento de Informática'),
('MATEMATICAS', 'Departamento de Matemáticas'),
('LENGUA', 'Departamento de Lengua y Literatura'),
('IDIOMAS', 'Departamento de Idiomas Extranjeros'),
('CIENCIAS', 'Departamento de Ciencias Naturales'),
('SOPORTE', 'Equipo de Soporte Técnico');

-- Insertar usuarios (clave: 'password' en este ejemplo)
INSERT INTO usuario (nombre, apellidos, email, clave, activo, rol_id, usuario_movil, grupo) VALUES
('Admin', 'Sistema', 'admin@guzpasen.edu', '$2a$10$fJ3jTj4yh2Y3USYmSgQRy.59OBE8DmSlGx6LxD1.ou92jsMDTkMza', 1, 1, 1, NULL),
('María', 'González López', 'maria.gonzalez@guzpasen.edu', '$2a$10$fJ3jTj4yh2Y3USYmSgQRy.59OBE8DmSlGx6LxD1.ou92jsMDTkMza', 1, 2, 0, 'MATEMATICAS'),
('Juan', 'Rodríguez Martín', 'juan.rodriguez@guzpasen.edu', '$2a$10$fJ3jTj4yh2Y3USYmSgQRy.59OBE8DmSlGx6LxD1.ou92jsMDTkMza', 1, 2, 1, 'INFORMATICA'),
('Laura', 'Fernández Ruiz', 'laura.fernandez@guzpasen.edu', '$2a$10$fJ3jTj4yh2Y3USYmSgQRy.59OBE8DmSlGx6LxD1.ou92jsMDTkMza', 1, 2, 0, 'LENGUA'),
('Carlos', 'Pérez Díaz', 'carlos.perez@guzpasen.edu', '$2a$10$fJ3jTj4yh2Y3USYmSgQRy.59OBE8DmSlGx6LxD1.ou92jsMDTkMza', 1, 3, 1, NULL),
('Ana', 'López García', 'ana.lopez@guzpasen.edu', '$2a$10$fJ3jTj4yh2Y3USYmSgQRy.59OBE8DmSlGx6LxD1.ou92jsMDTkMza', 1, 4, 0, 'SOPORTE'),
('Miguel', 'Sánchez Torres', 'miguel.sanchez@guzpasen.edu', '$2a$10$fJ3jTj4yh2Y3USYmSgQRy.59OBE8DmSlGx6LxD1.ou92jsMDTkMza', 0, 2, 0, 'CIENCIAS'),
('Elena', 'Martínez Vargas', 'elena.martinez@guzpasen.edu', '$2a$10$fJ3jTj4yh2Y3USYmSgQRy.59OBE8DmSlGx6LxD1.ou92jsMDTkMza', 1, 2, 1, 'IDIOMAS');

-- Asignar usuarios a grupos mediante la tabla de relación
INSERT INTO usuario_grupo (usuario_id, grupo_id) VALUES
(2, 2), -- María al grupo de MATEMATICAS
(3, 1), -- Juan al grupo de INFORMATICA
(4, 3), -- Laura al grupo de LENGUA
(6, 6), -- Ana al grupo de SOPORTE
(7, 5), -- Miguel al grupo de CIENCIAS
(8, 4), -- Elena al grupo de IDIOMAS
(3, 6); -- Juan también está en SOPORTE

-- Asignar módulos a roles
INSERT INTO rol_modulo (rol_id, modulo_id) VALUES
-- Administrador tiene acceso a todos los módulos
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10),
-- Profesor tiene acceso a ciertos módulos
(2, 1), (2, 7), (2, 8), (2, 10),
-- Gestor de incidencias
(3, 1), (3, 6), (3, 8), (3, 10),
-- Técnico
(4, 1), (4, 6), (4, 9), (4, 10);

-- Asignar módulos adicionales a usuarios específicos
INSERT INTO usuario_modulo (usuario_id, modulo_id) VALUES
(2, 5), -- María tiene acceso adicional a GRUPOS
(3, 4), -- Juan tiene acceso adicional a MODULOS
(5, 2); -- Carlos tiene acceso adicional a USUARIOS

-- Asignar roles adicionales a usuarios
INSERT INTO usuario_rol (usuario_id, rol_id) VALUES
(3, 4), -- Juan también es TECNICO además de PROFESOR
(6, 3); -- Ana también es GESTOR_INCIDENCIAS además de TECNICO
