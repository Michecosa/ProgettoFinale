package com.example.final_project.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	/** Filtro che valida il JWT e popola lo SecurityContext. */
	private JwtAuthenticationFilter jwtFilter;

	/**
	 * Definisce la catena di filtri usata da Spring Security.
	 * - CSRF off (stateless, solo API)
	 * - Sessioni disattivate (usiamo JWT)
	 * - End-point liberi / autenticati / con ruolo ADMIN
	 * - JwtAuthenticationFilter prima del filtro username/password
	 */
	@Bean // il metodo viene eseguito all'avvio e il return messo nell'ApplicationContext
			// come Bean
	public SecurityFilterChain api(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(
						sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(
						auth -> auth
								// URI CRITERIO DI ACCETTAZIONE
								.requestMatchers("/api/auth/login").permitAll()
								.requestMatchers("/api/auth/register").permitAll()
								.requestMatchers(HttpMethod.GET, "/api/products").permitAll()
								.requestMatchers(HttpMethod.POST, "/api/products").hasRole("ADMIN")
								.anyRequest().authenticated())
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	/** Espone l’AuthenticationManager per usarlo nei controller di login. */
	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	/** Encoder standard: BCrypt con 10 round (default). */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
