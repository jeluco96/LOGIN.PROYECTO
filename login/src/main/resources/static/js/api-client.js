/**
 * Cliente JavaScript para interactuar con la API REST
 */
class ApiClient {
    constructor() {
        this.baseUrl = '/api';
        this.token = localStorage.getItem('jwt_token');
        this.tokenRefreshPromise = null;
        this.debugMode = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1';
    }

    /**
     * Activa/desactiva el modo debug
     * @param {boolean} enabled Estado del modo debug
     */
    setDebugMode(enabled) {
        this.debugMode = enabled;
    }

    /**
     * Registra mensajes de depuración si el modo debug está activado
     * @param {...any} args Argumentos a registrar
     */
    debug(...args) {
        if (this.debugMode) {
            console.log('[ApiClient]', ...args);
        }
    }

    /**
     * Configura los headers para las peticiones
     * @param {boolean} includeAuth Indica si incluir el token de autenticación
     * @returns {Object} Headers de la petición
     */
    getHeaders(includeAuth = true) {
        const headers = {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        };

        if (includeAuth && this.token) {
            headers['Authorization'] = `Bearer ${this.token}`;
            this.debug('Añadiendo token a la petición:', this.token.substring(0, 15) + '...');
        }

        return headers;
    }

    /**
     * Realiza login y guarda el token JWT
     * @param {string} email - Email del usuario
     * @param {string} clave - Contraseña del usuario
     * @returns {Promise} Promesa con la respuesta
     */
    async login(email, clave) {
        this.debug('Intentando login para:', email);
        try {
            const response = await fetch(`${this.baseUrl}/auth/login`, {
                method: 'POST',
                headers: this.getHeaders(false),
                body: JSON.stringify({ email, clave })
            });

            this.debug('Respuesta login status:', response.status);

            if (!response.ok) {
                let errorMsg = 'Error en la autenticación';
                try {
                    const errorData = await response.json();
                    errorMsg = errorData.message || errorData.error || errorMsg;
                } catch (e) {
                    try {
                        errorMsg = await response.text() || errorMsg;
                    } catch (e2) {
                        // Fallback al mensaje por defecto
                    }
                }
                throw new Error(errorMsg);
            }

            const data = await response.json();
            this.debug('Login exitoso, token recibido');

            if (!data.token) {
                throw new Error('El servidor no devolvió un token válido');
            }

            this.token = data.token;
            localStorage.setItem('jwt_token', data.token);
            localStorage.setItem('user_info', JSON.stringify({
                id: data.userId,
                nombre: data.nombre,
                apellidos: data.apellidos,
                email: data.email,
                rol: data.rol
            }));

            // Validar que el token funciona
            try {
                this.debug('Verificando token recibido...');
                await this.validateToken(data.token);
            } catch (validationError) {
                this.debug('Advertencia: El token recibido puede no ser válido:', validationError.message);
                // No fallamos el login, solo registramos la advertencia
            }

            return data;
        } catch (error) {
            this.debug('Error en login:', error.message);
            throw error;
        }
    }

    /**
     * Valida un token JWT
     * @param {string} token Token JWT a validar
     * @returns {Promise} Promesa con la respuesta
     */
    async validateToken(token = this.token) {
        if (!token) {
            throw new Error('No hay token para validar');
        }

        try {
            const response = await fetch(`${this.baseUrl}/auth/validate-token?token=${encodeURIComponent(token)}`);

            if (!response.ok) {
                throw new Error(`Error al validar token: ${response.status}`);
            }

            const data = await response.json();
            return data.valid;
        } catch (error) {
            this.debug('Error al validar token:', error.message);
            throw error;
        }
    }

    /**
     * Cierra la sesión eliminando el token
     */
    logout() {
        this.token = null;
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('user_info');
    }

    /**
     * Obtiene la lista de usuarios
     * @returns {Promise} Promesa con la lista de usuarios
     */
    async getUsuarios() {
        try {
            const response = await fetch(`${this.baseUrl}/usuarios`, {
                method: 'GET',
                headers: this.getHeaders()
            });

            if (!response.ok) {
                throw new Error('Error al obtener usuarios');
            }

            return await response.json();
        } catch (error) {
            console.error('Error al obtener usuarios:', error);
            throw error;
        }
    }

    /**
     * Obtiene usuarios por rol
     * @param {number} rolId - ID del rol
     * @returns {Promise} Promesa con la lista de usuarios
     */
    async getUsuariosPorRol(rolId) {
        try {
            const response = await fetch(`${this.baseUrl}/usuarios/rol/${rolId}`, {
                method: 'GET',
                headers: this.getHeaders()
            });

            if (!response.ok) {
                throw new Error('Error al obtener usuarios por rol');
            }

            return await response.json();
        } catch (error) {
            console.error('Error al obtener usuarios por rol:', error);
            throw error;
        }
    }

    /**
     * Verifica si el usuario está autenticado
     * @returns {boolean} True si está autenticado, false en caso contrario
     */
    isAuthenticated() {
        return !!this.token;
    }

    /**
     * Obtiene la información del usuario actual
     * @returns {Object|null} Información del usuario o null si no está autenticado
     */
    getCurrentUser() {
        const userInfo = localStorage.getItem('user_info');
        return userInfo ? JSON.parse(userInfo) : null;
    }
}

// Crear una instancia global
const apiClient = new ApiClient();
