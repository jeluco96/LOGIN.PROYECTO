package com.dam.login.model;

/**
 * Enumeración que define los diferentes tipos de grupos disponibles en el sistema.
 * Cada tipo representa una categoría organizativa diferente dentro del centro educativo.
 */
public enum TipoGrupo {
    /**
     * Representa un departamento académico o administrativo dentro del centro educativo.
     * Por ejemplo: Departamento de Matemáticas, Departamento de Informática, etc.
     */
    DEPARTAMENTO,

    /**
     * Representa un equipo educativo asignado a un nivel, curso o grupo de alumnos específico.
     * Por ejemplo: Equipo Educativo 1ºESO, Equipo Educativo 2ºDAM, etc.
     */
    EQUIPO_EDUCATIVO,

    /**
     * Representa el equipo directivo del centro educativo.
     * Incluye roles como director, jefe de estudios, secretario, etc.
     */
    EQUIPO_DIRECTIVO,

    /**
     * Representa equipos de trabajo específicos o comisiones temporales o permanentes.
     * Por ejemplo: Equipo de Calidad, Comisión de Actividades, etc.
     */
    EQUIPO_TRABAJO
}