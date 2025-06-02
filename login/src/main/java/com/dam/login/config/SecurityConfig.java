/**
 * Configuración de seguridad para la aplicación.
 * Define componentes básicos de seguridad y reglas de acceso.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Define un PasswordEncoder basado en BCrypt para usar en la aplicación.
     * BCrypt es un algoritmo de hashing recomendado para contraseñas.
     *
     * @return Instancia de BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("Configurando BCryptPasswordEncoder");
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura los CORS globalmente para permitir acceso desde diferentes orígenes.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("Configurando política CORS global");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // En producción, restringe a dominios específicos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Configuración de seguridad para la interfaz web con Thymeleaf
     */
    @Bean
    @Order(1)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configurando filtro de seguridad para web");
        return http
            .securityMatcher("/", "/auth/**", "/dashboard/**", "/usuarios/**", "/roles/**", "/modulos/**", "/configuracion/**", 
                           "/css/**", "/js/**", "/images/**", "/webjars/**", "/error", "/login")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/auth/login", "/auth/login-process", "/css/**", "/js/**", "/images/**", "/webjars/**", "/error").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login-process")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/auth/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            )
            .sessionManagement(session -> session
                .maximumSessions(1)
                .expiredUrl("/auth/login?expired=true")
            )
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .build();
    }

    /**
     * Configuración de seguridad para la API REST
     */
    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configurando filtro de seguridad para API REST");
        return http
            .securityMatcher("/api/**")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/api/health/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    log.warn("Acceso no autorizado: {}", authException.getMessage());
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"error\":\"No autorizado\",\"message\":\"" + authException.getMessage() + "\"}\n");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    log.warn("Acceso denegado: {}", accessDeniedException.getMessage());
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("{\"error\":\"Acceso denegado\",\"message\":\"" + accessDeniedException.getMessage() + "\"}\n");
                })
            )
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .build();
    }

    /**
     * Registra los filtros de seguridad para debug
     */
    @Bean
    public FilterRegistrationBean<DebugFilter> debugFilter() {
        log.info("Registrando filtro de debug para diagnóstico");
        FilterRegistrationBean<DebugFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new DebugFilter());
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(Integer.MIN_VALUE); // Asegura que se ejecute primero
        return registrationBean;
    }

    /**
     * Filtro para debug que registra detalles de todas las solicitudes
     */
    @Slf4j
    public static class DebugFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {

            log.debug("DebugFilter: {} {} (desde {})", 
                    request.getMethod(), 
                    request.getRequestURI(),
                    request.getRemoteAddr());

            String authHeader = request.getHeader("Authorization");
            if (authHeader != null) {
                log.debug("DebugFilter: Authorization header presente: {}", 
                        authHeader.startsWith("Bearer ") ? "Bearer [token]" : "tipo desconocido");
            } else {
                log.debug("DebugFilter: No hay Authorization header");
            }

            filterChain.doFilter(request, response);

            log.debug("DebugFilter: Respuesta para {} {}: status={}", 
                    request.getMethod(), 
                    request.getRequestURI(),
                    response.getStatus());
        }
    }
}
