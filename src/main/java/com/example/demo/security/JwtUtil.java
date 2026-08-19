package com.example.demo.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long EXPIRATION_TIME = 1000*60; // 1 minute
    
    // Creates a brand new JWT when a user successfully logs in
    public String generateToken(String username) {
        return Jwts.builder()                           // Starts the JWT construction process
                .setSubject(username)                   // Embeds the username inside the token's payload
                .setIssuedAt(new Date())                // Stamps the exact time the token was created
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Sets the drop-dead expiration time
                .signWith(key)                          // Cryptographically seals it with your secret key
                .compact();                             // Compresses it into the final "Header.Payload.Signature" string format
    }

    // Opens up an incoming token to figure out who is making the request
    public String extractUsername(String token) {
        return Jwts.parserBuilder()                     // Starts the JWT reading process
                .setSigningKey(key)                     // Provides the secret key to prove the token wasn't tampered with
                .build()                                // Builds the parser
                .parseClaimsJws(token)                  // Decodes the token and verifies the signature
                .getBody()                              // Accesses the token's internal payload (the "claims")
                .getSubject();                          // Retrieves the username we embedded earlier
    }

    // Checks if an incoming token is still valid and authentic
    public boolean validateToken(String token) {
        try {
            // Attempts to decode and verify the token using your secret key
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;                                // If it doesn't crash, the token is perfectly valid
        } catch (Exception e) {
            return false;                               // If it crashes, the token is expired, forged, or malformed
        }
    }
}
