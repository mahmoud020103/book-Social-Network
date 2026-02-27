	package com.mahmoud.book.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.Base64;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	@Value("${application.security.jwt.secret-key}")
	private String secretKey;

	@Value("${application.security.jwt.expiration}")

	private Long jwtExpiration;
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(),userDetails);
	}
	public String extractUsername(String token) {
		// TODO Auto-generated method stub
		return extractClaim(token,Claims::getSubject);
	}
	public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		// TODO Auto-generated method stub
		final Claims claims=extractAllClaims(token);
		return claimResolver.apply(claims);
	}
	private Claims extractAllClaims(String token) {
		// TODO Auto-generated method stub
		return Jwts.parser()
                .verifyWith(getSignInKey())   // ✔ SecretKey صحيح
                .build()
                .parseSignedClaims(token)
                .getPayload();
	}
	public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
		// TODO Auto-generated method stub
		return buildToken(claims,userDetails,jwtExpiration);
	}

	private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, Long jwtExpiration2) {
		// TODO Auto-generated method stub
		var authorties=userDetails.getAuthorities()
				.stream().map(GrantedAuthority::getAuthority)
				.toList();

	    var jwtBuilder = Jwts.builder()
	            .signWith(getSignInKey()); // توقيع التوكن

		jwtBuilder.claim("sub", userDetails.getUsername());
	    jwtBuilder.claim("iat", new Date(System.currentTimeMillis()));
	    jwtBuilder.claim("exp", new Date(System.currentTimeMillis() + jwtExpiration));
	    jwtBuilder.claim("authorities", authorties);

	    // إضافة أي extra claims
	    if (extraClaims != null) {
	        extraClaims.forEach((k, v) -> jwtBuilder.claim(k, v));
	    }

	    return jwtBuilder.compact();

	}
	public boolean isTokenValid(String token,UserDetails userDetails) {
		final String username=extractUsername(token);
		return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
	}



	private boolean isTokenExpired(String token) {
		// TODO Auto-generated method stub
		return extractExpiration(token).before(new Date());
	}
	private Date extractExpiration(String token) {
		// TODO Auto-generated method stub
		return extractClaim(token, Claims::getExpiration);
	}
	private SecretKey getSignInKey() {
	    byte[] keyBytes = Base64.getDecoder().decode(secretKey);
	    return Keys.hmacShaKeyFor(keyBytes);
	}
}
