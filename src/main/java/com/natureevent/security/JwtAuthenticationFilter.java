package com.natureevent.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	private final JwtService jwtService;

	public JwtAuthenticationFilter(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");

		logger.debug("JWT FILTER → {} {}", request.getMethod(), request.getRequestURI());

		// =========================================================
		// 1. NO AUTHORIZATION HEADER
		// =========================================================

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {

			logger.debug("JWT FILTER → No Bearer token");

			filterChain.doFilter(request, response);
			return;
		}

		// =========================================================
		// 2. EXTRACT TOKEN
		// =========================================================

		String token = authHeader.substring(7);

		try {

			// =====================================================
			// 3. EXTRACT USERNAME
			// =====================================================

			String username = jwtService.extractUsername(token);

			logger.debug("JWT FILTER → Username: {}", username);

			// =====================================================
			// 4. VALIDATE TOKEN
			// =====================================================

			if (username != null && jwtService.isTokenValid(token, username)
					&& SecurityContextHolder.getContext().getAuthentication() == null) {

				// =================================================
				// 5. CREATE AUTHENTICATED USER
				// =================================================

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,
						null, Collections.emptyList());

				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// =================================================
				// 6. STORE AUTHENTICATION
				// =================================================

				SecurityContextHolder.getContext().setAuthentication(authentication);

				logger.debug("JWT FILTER → Authentication SUCCESS for: {}", username);

			} else {

				logger.debug("JWT FILTER → Token validation FAILED");
			}

		} catch (Exception e) {

			logger.warn("JWT FILTER → Invalid JWT: {}", e.getMessage());
		}

		// =========================================================
		// 7. CONTINUE REQUEST
		// =========================================================

		filterChain.doFilter(request, response);
	}
}