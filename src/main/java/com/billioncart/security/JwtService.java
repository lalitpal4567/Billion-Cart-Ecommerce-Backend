package com.billioncart.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	public static final long TOKEN_VALIDITY  = 7 * 24 * 60 * 60 * 1000;
	private final String SECRET_KEY = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";
	
	// generate token
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		
		return Jwts.builder()
				.claims(claims)
				.subject(userDetails.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY ))
				.signWith(getSigningKey())
				.compact();
	}

	// encrypt the secret key
	private SecretKey getSigningKey() {
//		byte[] keyBytes = this.secret.getBytes(StandardCharsets.UTF_8);
		byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	// retrieve username from token
	public String getUsernameFromToken(String token) {
//		return getClaimFromToken(token, Claims::getSubject);
		return extractClaim(token, claims -> claims.getSubject());

	}

	// retrieve a specified claim
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	// extract all token claims
	// secret key is required to retrieve any information about token
	private Claims extractAllClaims(String token){
		 return Jwts.parser()
		.verifyWith(getSigningKey())
		.build()
		.parseSignedClaims(token)
		.getPayload();
	}
	
	// retrieve expiration date from token
	public Date getExpirationDateFromToken(String token) {
		return extractClaim(token, claims -> claims.getExpiration());
	}

	// check if the token has expired
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}


	// check if the token is valid
	public Boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	// refresh token
	public String refreshToken(Map<String, Object> claims, UserDetails userDetails) {
		return Jwts.builder()
				.claims(claims)
				.subject(userDetails.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 604800000))
				.signWith(getSigningKey())
				.compact();
	}
}
