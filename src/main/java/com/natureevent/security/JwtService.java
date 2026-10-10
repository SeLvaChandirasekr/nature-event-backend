package com.natureevent.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String secretKey;

	private final long expirationTime = 1000 * 60 * 60; // 1 hour

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
	}

	public String generateToken(String username) {

		Date now = new Date();
		Date expiration = new Date(now.getTime() + expirationTime);

		return Jwts.builder().subject(username).issuedAt(now).expiration(expiration).signWith(getSigningKey())
				.compact();
	}

	public String extractUsername(String token) {

		Claims claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();

		return claims.getSubject();
	}

	public boolean isTokenValid(String token, String username) {

		String extractedUsername = extractUsername(token);

		return extractedUsername.equals(username);
	}
}