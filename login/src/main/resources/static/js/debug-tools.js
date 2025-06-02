/**
 * Herramientas de depuración para ayudar a diagnosticar problemas de autenticación
 * Solo para uso en desarrollo
 */

const debugTools = {
    /**
     * Analiza un token JWT y muestra su información
     * @param {string} token Token JWT a analizar
     * @returns {Promise<Object>} Información del token
     */
    async analyzeToken(token) {
        try {
            const response = await fetch(`/api/debug/token?token=${encodeURIComponent(token)}`);
            if (!response.ok) {
                throw new Error(`Error al analizar token: ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            console.error('Error en analyzeToken:', error);
            throw error;
        }
    },

    /**
     * Obtiene información sobre la autenticación actual
     * @returns {Promise<Object>} Información de autenticación
     */
    async getAuthInfo() {
        try {
            const response = await fetch('/api/debug/auth');
            if (!response.ok) {
                throw new Error(`Error al obtener info de autenticación: ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            console.error('Error en getAuthInfo:', error);
            throw error;
        }
    },

    /**
     * Fuerza la autenticación para un usuario
     * @param {string} email Email del usuario
     * @returns {Promise<Object>} Resultado de la operación
     */
    async forceAuth(email) {
        try {
            const response = await fetch(`/api/debug/force-auth?email=${encodeURIComponent(email)}`, {
                method: 'POST'
            });
            if (!response.ok) {
                throw new Error(`Error al forzar autenticación: ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            console.error('Error en forceAuth:', error);
            throw error;
        }
    },

    /**
     * Añade herramientas de depuración a la consola del navegador
     */
    initConsoleTools() {
        if (typeof window !== 'undefined') {
            window.debugJwt = this.analyzeToken;
            window.debugAuth = this.getAuthInfo;
            window.forceAuth = this.forceAuth;
            window.getStoredToken = () => localStorage.getItem('jwt_token');
            window.clearStoredToken = () => localStorage.removeItem('jwt_token');

            console.log('%c🔧 Herramientas de depuración cargadas', 'color: green; font-weight: bold');
            console.log('%cUso:', 'font-weight: bold');
            console.log('- debugJwt(token): Analiza un token JWT');
            console.log('- debugAuth(): Muestra info de autenticación actual');
            console.log('- forceAuth(email): Fuerza autenticación para un usuario');
            console.log('- getStoredToken(): Obtiene el token almacenado');
            console.log('- clearStoredToken(): Elimina el token almacenado');
        }
    }
};

// Auto-inicializar en entorno de desarrollo
if (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1') {
    debugTools.initConsoleTools();
}
