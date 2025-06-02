-- Script de creación de la base de datos para el sistema de login
-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS login;
USE login;

-- Tabla de roles
CREATE TABLE rol (
    id_rol BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de módulos
CREATE TABLE modulo (
    id_modulo BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    ruta VARCHAR(255),
    icono VARCHAR(100),
    activo BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de grupos
CREATE TABLE grupo (
    id_grupo BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de usuarios
CREATE TABLE usuario (
    id_usuario BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    clave VARCHAR(255) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    rol_id BIGINT NOT NULL,
    usuario_movil BOOLEAN DEFAULT FALSE,
    grupo VARCHAR(100),
    FOREIGN KEY (rol_id) REFERENCES rol(id_rol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de relación usuario-grupo
CREATE TABLE usuario_grupo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    grupo_id BIGINT NOT NULL,
    fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (grupo_id) REFERENCES grupo(id_grupo) ON DELETE CASCADE,
    UNIQUE KEY unique_usuario_grupo (usuario_id, grupo_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de relación roles y módulos
CREATE TABLE rol_modulo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rol_id BIGINT NOT NULL,
    modulo_id BIGINT NOT NULL,
    FOREIGN KEY (rol_id) REFERENCES rol(id_rol) ON DELETE CASCADE,
    FOREIGN KEY (modulo_id) REFERENCES modulo(id_modulo) ON DELETE CASCADE,
    UNIQUE KEY unique_rol_modulo (rol_id, modulo_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de relación usuarios y módulos adicionales
CREATE TABLE usuario_modulo (
    usuario_id BIGINT NOT NULL,
    modulo_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, modulo_id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (modulo_id) REFERENCES modulo(id_modulo) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de relación usuarios y roles adicionales
CREATE TABLE usuario_rol (
    usuario_id BIGINT NOT NULL,
    rol_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (rol_id) REFERENCES rol(id_rol) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Índices para mejorar el rendimiento
CREATE INDEX idx_usuario_email ON usuario(email);
CREATE INDEX idx_usuario_activo ON usuario(activo);
CREATE INDEX idx_modulo_activo ON modulo(activo);
CREATE INDEX idx_usuario_rol ON usuario(rol_id);
