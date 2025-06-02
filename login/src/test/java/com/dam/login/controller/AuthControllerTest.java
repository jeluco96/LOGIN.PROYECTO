package com.dam.login.controller;

import com.dam.login.dto.AuthResponse;
import com.dam.login.dto.LoginRequest;
import com.dam.login.model.Rol;
import com.dam.login.model.Usuario;
import com.dam.login.security.JwtUtil;
import com.dam.login.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    private Usuario testUser;
    private LoginRequest loginRequest;

    @BeforeEach
    public void setup() {
        // Configurar usuario de prueba
        testUser = new Usuario();
        testUser.setId(1L);
        testUser.setNombre("Admin");
        testUser.setApellidos("Test");
        testUser.setEmail("admin@test.com");
        testUser.setClave("$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG"); // 'password'
        testUser.setRol(Rol.ADMIN);
        testUser.setActivo(true);

        // Configurar petición de login
        loginRequest = new LoginRequest();
        loginRequest.setEmail("admin@test.com");
        loginRequest.setClave("password");

        // Configurar mock del servicio
        when(usuarioService.login("admin@test.com", "password")).thenReturn(testUser);
    }

    @Test
    public void loginSuccessTest() throws Exception {
        // Realizar petición de login
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.nombre").value("Admin"))
                .andExpect(jsonPath("$.email").value("admin@test.com"))
                .andReturn();

        // Extraer el token de la respuesta
        String responseContent = result.getResponse().getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(responseContent, AuthResponse.class);
        String token = authResponse.getToken();

        // Verificar que el token sea válido
        assertNotNull(token);
        assertTrue(jwtUtil.isTokenValid(token));

        // Verificar que podemos acceder a un endpoint protegido con el token
        mockMvc.perform(get("/api/health/api")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    public void validateTokenTest() throws Exception {
        // Generar un token válido
        String token = jwtUtil.generateToken(testUser.getEmail());

        // Mock para buscar por email
        when(usuarioService.buscarPorEmail(anyString())).thenReturn(java.util.Optional.of(testUser));

        // Verificar el token
        mockMvc.perform(get("/api/auth/validate-token")
                .param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.email").value("admin@test.com"));
    }

    @Test
    public void healthCheckTest() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }
}
