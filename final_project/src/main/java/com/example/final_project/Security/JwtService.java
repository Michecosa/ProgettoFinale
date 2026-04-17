package com.example.final_project.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Incapsula tutte le operazioni sul JSON Web Token:
 * • generazione
 * • estrazione username / ruoli
 * • validazione scadenza & firma
 */
@Service
public class JwtService {

	/** Chiave segreta in Base64 (min. 256 bit per HS256). */
	@Value("${jwt.secret}") // prende il valore dall'application properties
	private String secret; // es. "xvfr25... (Base64)"

	/** Durata del token in millisecondi (es. 86_400_000 = 24h). */
	@Value("${jwt.expiration}")
	private long expirationMs;

	private SecretKey key;

	@PostConstruct
	private void init() {
		// converte la stringa Base64 in chiave HMAC-SHA
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}

	/** Crea un token per l’utente autenticato, con i ruoli nel claim "roles". */
	public String generateToken(Authentication auth) {
		String roles = auth.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		Date now = new Date();
		Date exp = new Date(now.getTime() + expirationMs);

		return Jwts.builder()
				.subject(auth.getName())
				.claim("roles", roles)
				.issuedAt(now)
				.expiration(exp)
				.signWith(key, Jwts.SIG.HS256)
				.compact();
	}

	/**
	 * Estrae lo username (subject) senza sollevare eccezioni in caso di token
	 * invalido.
	 */
	public String extractUsername(String token) {
		try {
			return parse(token).getSubject();
		} catch (Exception e) {
			return null;
		}
	}

	/** Estrae i ruoli come lista di stringhe. */
	public List<String> extractRoles(String token) {
		try {
			String roles = (String) parse(token).get("roles");
			return List.of(roles.split(","));
		} catch (Exception e) {
			return List.of();
		}
	}

	/** Controlla firma e scadenza. */
	public boolean isTokenValid(String token) {
		try {
			Claims c = parse(token);
			return c.getExpiration().after(new Date());
		} catch (Exception e) {
			return false;
		}
	}

	/* ---------- helper ---------- */

	private Claims parse(String token) {
		return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
}
