package com.natureevent.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http

				// Disable CSRF because this is a stateless JWT API
				.csrf(csrf -> csrf.disable())

				// Enable CORS
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))

				// Do not create HTTP sessions
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// API authorization rules
				.authorizeHttpRequests(auth -> auth

						// Public APIs
						.requestMatchers("/api/health", "/api/auth/**").permitAll()

						// User registration
						.requestMatchers(HttpMethod.POST, "/api/users").permitAll().requestMatchers("/api/locations/**")
						.permitAll()

						// All remaining APIs require JWT
						.anyRequest().authenticated())

				// Validate JWT before Spring Security authentication
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	// =========================================================
	// CORS CONFIGURATION
	// =========================================================

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {

		CorsConfiguration configuration = new CorsConfiguration();

		// React frontend (localhost and deployed Vercel domains)
		configuration.setAllowedOriginPatterns(List.of(
				"http://localhost:[*]",
				"http://127.0.0.1:[*]",
				"https://*.vercel.app"
		));

		// HTTP methods
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

		// Request headers
		configuration.setAllowedHeaders(List.of("*"));

		// Allow credentials
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}
}