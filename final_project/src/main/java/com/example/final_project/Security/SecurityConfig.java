package com.example.final_project.Security;

import java.util.List;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

	@Bean
	JwtAuthenticationFilter jwtFilter() {
		return new JwtAuthenticationFilter(jwtService, userDetailsService);
	}

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
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(
						sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(
						auth -> auth
								// URI CRITERIO DI ACCETTAZIONE
								.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
								.requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
								.requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
								.requestMatchers("/error").permitAll()
								.requestMatchers("/", "/index.html", "/css/**", "/js/**", "/img/**", "/favicon.ico")
								.permitAll()
								.requestMatchers(HttpMethod.GET, "/api/products").permitAll()
								.requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
								.requestMatchers(HttpMethod.GET, "/api/categories").permitAll()
								.requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
								.requestMatchers(HttpMethod.POST, "/api/products").hasRole("ADMIN")
								.requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
								.requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")
								.requestMatchers(HttpMethod.POST, "/api/categories").hasRole("ADMIN")
								.requestMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("ADMIN")
								.requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")
								.requestMatchers("/api/users/**").hasRole("ADMIN")
								.anyRequest().authenticated())
				.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of(
				"http://localhost:8080",
				"http://127.0.0.1:8080",
				"http://localhost:5500",
				"http://127.0.0.1:5500"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
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
