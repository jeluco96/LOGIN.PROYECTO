/**
 * Script para pruebas con Postman
 * Copiar y pegar en la pestaña "Tests" de Postman para las peticiones de login y API
 */

// SCRIPT PARA LA PETICIÓN DE LOGIN (POST /api/auth/login)

// Verificar que la respuesta sea exitosa
pm.test("Respuesta exitosa", function () {
    pm.response.to.have.status(200);
});

// Verificar que se reciba un token JWT
pm.test("Contiene token JWT", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.token).to.be.a('string');
    pm.expect(jsonData.token.length).to.be.greaterThan(20);

    // Guardar el token para usar en otras peticiones
    pm.environment.set("jwt_token", jsonData.token);

    console.log("Token guardado: " + jsonData.token.substring(0, 20) + "...");
});

// Verificar que contenga los datos del usuario
pm.test("Contiene datos del usuario", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.userId).to.exist;
    pm.expect(jsonData.nombre).to.exist;
    pm.expect(jsonData.email).to.exist;
    pm.expect(jsonData.rol).to.exist;
});

// Instrucciones para uso en consola
console.log("\nPrueba de login completada. Ahora puedes usar el token JWT en otras peticiones.\n");
console.log("En Headers de las siguientes peticiones, agrega:\n");
console.log("Authorization: Bearer " + pm.environment.get("jwt_token") + "\n");

// SCRIPT PARA PETICIONES PROTEGIDAS (por ejemplo GET /api/usuarios)

// Verificar respuesta exitosa
pm.test("Respuesta exitosa", function () {
    pm.response.to.have.status(200);
});

// Verificar que no haya errores de autenticación
pm.test("No hay errores de autenticación", function () {
    pm.response.to.not.have.status(401);
    pm.response.to.not.have.status(403);
});

// Verificar que el cuerpo de la respuesta sea JSON válido
pm.test("Respuesta es JSON válido", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.be.an('object').or.an('array');
});

// INSTRUCCIONES PARA POSTMAN
/*
1. Crear una colección nueva "GUZPASEN API Tests"

2. Crear variables de entorno:
   - base_url: http://localhost:8084
   - jwt_token: (dejar vacío inicialmente)

3. Crear peticiones:

   a) Login (POST):
      URL: {{base_url}}/api/auth/login
      Body (raw JSON): 
      {
        "email": "admin@guzpasen.edu",
        "clave": "password"
      }
      Tests: copiar la primera parte del script

   b) Verificar Token (GET):
      URL: {{base_url}}/api/auth/validate-token?token={{jwt_token}}

   c) Health Check (GET):
      URL: {{base_url}}/api/health

   d) Listar Usuarios (GET):
      URL: {{base_url}}/api/usuarios
      Headers: Authorization: Bearer {{jwt_token}}
      Tests: copiar la segunda parte del script

4. Ejecutar en orden:
   - Login (para obtener el token)
   - Verificar Token (para validar)
   - Endpoints protegidos (usando el token)
*/
