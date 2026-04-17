package com.example.security.security;

import com.example.security.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

/**
 * Configura la sicurezza dell'applicazione:
 * - Imposta il filtro JWT per le richieste
 * - Definisce le regole di autorizzazione
 * - Configura l'AuthenticationManager con UserDetailsService personalizzato
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtAuthFilter jwtFilter;
  private final CustomUserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;

  // Costruttore con injection del filtro JWT, UserDetailsService e
  // PasswordEncoder
  public SecurityConfig(JwtAuthFilter jwtFilter, CustomUserDetailsService userDetailsService, PasswordEncoder encoder) {
    this.jwtFilter = jwtFilter;
    this.userDetailsService = userDetailsService;
    this.passwordEncoder = encoder;
  }

  /**
   * Definisce la catena di sicurezza HTTP:
   * - Disabilita CSRF
   * - Imposta la sessione in modalitÃ stateless (JWT)
   * - Applica regole di autorizzazione
   * - Registra il filtro JWT prima del filtro di autenticazione standard
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .authenticationProvider(authenticationProvider())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**", "/public/**").permitAll()
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    return provider;
  }

  /**
   * Configura l'AuthenticationManager da usare manualmente o nel controller.
   * Usa il servizio utenti personalizzato e il password encoder scelto (es.
   * BCrypt).
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return new ProviderManager(authenticationProvider());
  }

  /**
   * Configurazione CORS per permettere chiamate da Postman o Frontend locali.
   */
  @Bean
  public UrlBasedCorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("*")); // In produzione, specifica i domini reali
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}