-- Datos iniciales para la aplicación
-- Insertar roles si no existen
INSERT INTO rol (nombre, descripcion) SELECT 'ADMINISTRADOR', 'Administrador del sistema con acceso completo' WHERE NOT EXISTS (SELECT 1 FROM rol WHERE nombre = 'ADMINISTRADOR');
INSERT INTO rol (nombre, descripcion) SELECT 'PROFESOR', 'Profesor con acceso a funciones educativas' WHERE NOT EXISTS (SELECT 1 FROM rol WHERE nombre = 'PROFESOR');
INSERT INTO rol (nombre, descripcion) SELECT 'TECNICO', 'Técnico de soporte con acceso a funciones de mantenimiento' WHERE NOT EXISTS (SELECT 1 FROM rol WHERE nombre = 'TECNICO');
INSERT INTO rol (nombre, descripcion) SELECT 'GESTOR_INCIDENCIAS', 'Gestor de incidencias con acceso a funciones de resolución de problemas' WHERE NOT EXISTS (SELECT 1 FROM rol WHERE nombre = 'GESTOR_INCIDENCIAS');

-- Insertar usuario administrador si no existe
-- La contraseña 'password' está codificada con BCrypt
INSERT INTO usuario (nombre, apellidos, email, clave, activo, rol_id, usuario_movil, grupo) 
SELECT 'Administrador', 'Sistema', 'admin@guzpasen.edu', '$2a$10$dbuRzNLA0I0TBsZrGIjQIOSi.3wDa2RL63MFR9aqxym6TqZ9VStTK', true, (SELECT id_rol FROM rol WHERE nombre = 'ADMINISTRADOR'), true, 'ADMIN' 
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'admin@guzpasen.edu');
-- Crear roles
INSERT INTO roles (id, nombre, descripcion) VALUES (1, 'ADMIN', 'Administrador del sistema');
INSERT INTO roles (id, nombre, descripcion) VALUES (2, 'PROFESOR', 'Profesor con acceso a recursos educativos');
INSERT INTO roles (id, nombre, descripcion) VALUES (3, 'ALUMNO', 'Alumno con acceso limitado');

-- Crear módulos
INSERT INTO modulos (id, nombre, descripcion, ruta, icono, activo) VALUES (1, 'DASHBOARD', 'Panel principal', '/dashboard', 'dashboard', true);
INSERT INTO modulos (id, nombre, descripcion, ruta, icono, activo) VALUES (2, 'USUARIOS', 'Gestión de usuarios', '/usuarios', 'people', true);
INSERT INTO modulos (id, nombre, descripcion, ruta, icono, activo) VALUES (3, 'ROLES', 'Gestión de roles', '/roles', 'security', true);
INSERT INTO modulos (id, nombre, descripcion, ruta, icono, activo) VALUES (4, 'MODULOS', 'Gestión de módulos', '/modulos', 'apps', true);
INSERT INTO modulos (id, nombre, descripcion, ruta, icono, activo) VALUES (5, 'INFORMES', 'Generación de informes', '/informes', 'assessment', true);

-- Asignar módulos a roles
INSERT INTO rol_modulo (id, rol_id, modulo_id) VALUES (1, 1, 1); -- ADMIN -> DASHBOARD
INSERT INTO rol_modulo (id, rol_id, modulo_id) VALUES (2, 1, 2); -- ADMIN -> USUARIOS
INSERT INTO rol_modulo (id, rol_id, modulo_id) VALUES (3, 1, 3); -- ADMIN -> ROLES
INSERT INTO rol_modulo (id, rol_id, modulo_id) VALUES (4, 1, 4); -- ADMIN -> MODULOS
INSERT INTO rol_modulo (id, rol_id, modulo_id) VALUES (5, 1, 5); -- ADMIN -> INFORMES
INSERT INTO rol_modulo (id, rol_id, modulo_id) VALUES (6, 2, 1); -- PROFESOR -> DASHBOARD
INSERT INTO rol_modulo (id, rol_id, modulo_id) VALUES (7, 2, 5); -- PROFESOR -> INFORMES
INSERT INTO rol_modulo (id, rol_id, modulo_id) VALUES (8, 3, 1); -- ALUMNO -> DASHBOARD

-- Crear usuarios (contraseñas: $2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7iXinRR1iiq -> 'password')
INSERT INTO usuarios (id, nombre, apellidos, email, clave, rol_id, grupo, usuario_movil, activo) 
VALUES (1, 'Admin', 'Sistema', 'admin@guzpasen.edu', '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7iXinRR1iiq', 1, 'ADMINISTRACIÓN', false, true);

INSERT INTO usuarios (id, nombre, apellidos, email, clave, rol_id, grupo, usuario_movil, activo) 
VALUES (2, 'Juan', 'Pérez', 'juan.perez@guzpasen.edu', '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7iXinRR1iiq', 2, 'MATEMÁTICAS', true, true);

INSERT INTO usuarios (id, nombre, apellidos, email, clave, rol_id, grupo, usuario_movil, activo) 
VALUES (3, 'María', 'García', 'maria.garcia@guzpasen.edu', '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7iXinRR1iiq', 2, 'CIENCIAS', true, true);

INSERT INTO usuarios (id, nombre, apellidos, email, clave, rol_id, grupo, usuario_movil, activo) 
VALUES (4, 'Pedro', 'López', 'pedro.lopez@guzpasen.edu', '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7iXinRR1iiq', 3, 'DAM2', true, true);

INSERT INTO usuarios (id, nombre, apellidos, email, clave, rol_id, grupo, usuario_movil, activo) 
VALUES (5, 'Ana', 'Martínez', 'ana.martinez@guzpasen.edu', '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7iXinRR1iiq', 3, 'DAM1', true, true);

-- Asignar módulos adicionales a usuarios específicos
INSERT INTO usuario_modulo (usuario_id, modulo_id) VALUES (4, 5); -- Pedro (ALUMNO) tiene acceso especial a INFORMES
