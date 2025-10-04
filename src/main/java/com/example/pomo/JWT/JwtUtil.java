//package com.example.pomo.JWT;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.Claims;
//
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import java.nio.charset.StandardCharsets;
//import java.util.Date;
//
//@Component
//public class JwtUtil {
//    private static final String SECRET = "this_is_a_very_secret_key_1234567890!"; // >= 32 chars
//    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
//
//    public String generateToken(String username){
//        return Jwts.builder()
//                .subject(username)
//                .issuedAt(new Date())
//                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
//                .signWith(KEY)
//                .compact();
//    }
//
//
//
//    public String validateToken(String token) {
//        try {
//            Claims claims = Jwts.parser()
//                    .verifyWith(KEY)
//                    .build()
//                    .parseSignedClaims(token)
//                    .getPayload();
//
//            return claims.getSubject();
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//}
package com.example.pomo.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final String SECRET = "this_is_a_very_secret_key_1234567890!"; // >= 32 chars
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION = 1000 * 60 * 60; // 1 hour

    // 1️⃣ Generate JWT token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // 2️⃣ Extract username from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 3️⃣ Extract any claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 4️⃣ Validate token for user
    public boolean validateToken(String token, org.springframework.security.core.userdetails.UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 5️⃣ Check if token expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 6️⃣ Extract all claims (latest non-deprecated)
    private Claims extractAllClaims(String token) {
        Jws<Claims> jwsClaims = Jwts.parser()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token); // parses and validates signature
        return jwsClaims.getBody();
    }
}
