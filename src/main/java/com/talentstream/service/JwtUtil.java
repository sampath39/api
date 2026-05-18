package com.talentstream.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private String SECRET_KEY = "secret";

    private static final long ACCESS_TOKEN_EXPIRY = 1000 * 60 * 60 * 10; // 10 hours
    private static final long REFRESH_TOKEN_EXPIRY = 1000L * 60 * 60 * 24 * 10; // 10 days


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        boolean expired = extractExpiration(token).before(new Date());
        if (expired) {
            logger.warn("Token is expired");
        } else {
            logger.info("Token is still valid");
        }
        return expired;
    }

    public String generateToken(UserDetails userDetails) {
        logger.info("Generating access token for user: {}", userDetails.getUsername());
        return generateToken(userDetails.getUsername());
    }

    public String generateToken(String email) {

        Map<String, Object> claims = new HashMap<>();

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        logger.info("Access token generated successfully");

        return token;
    }

    public String generateRefreshToken(String email) {

        logger.info("Generating refresh token for email: {}", email);

        String refreshToken = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        logger.info("Refresh token generated successfully");

        return refreshToken;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {

        logger.info("Validating access token for user: {}", userDetails.getUsername());

        final String email = extractUsername(token);

        boolean valid = (email.equals(userDetails.getUsername()) && !isTokenExpired(token));

        if (valid) {
            logger.info("Access token is valid");
        } else {
            logger.error("Access token validation failed");
        }

        return valid;
    }

    public Boolean validateRefreshToken(String refreshToken) {

        logger.info("Validating refresh token");

        try {
            String email = extractUsername(refreshToken);

            boolean valid = (email != null && !isTokenExpired(refreshToken));

            if (valid) {
                logger.info("Refresh token is valid");
            } else {
                logger.warn("Refresh token expired or invalid");
            }

            return valid;

        } catch (Exception e) {

            logger.error("Error validating refresh token: {}", e.getMessage());
            return false;

        }
    }
}