//package com.logmaven.exmaven.util;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.security.Key;
//
//@Component
//public class JwtUtil {
//
//    private Key secretKey;
//
//    @PostConstruct
//    public void init() {
//        // Generate a secure key for HS256
//        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//    }
//
//    public String generateToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .signWith(secretKey)
//                .compact();
//    }
//
//    public String getUsername(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(secretKey)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(secretKey)
//                    .build()
//                    .parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//}
//

//package com.logmaven.exmaven.util;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.security.Key;
//
//@Component
//public class JwtUtil {
//
//    private Key secretKey;
//
//
//    @PostConstruct
//    public void init() {
//        // Generate a secure key for HS256
//        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//    }
//
//    public String generateToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .signWith(secretKey)
//                .compact();
//    }
//
//    public String getUsername(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(secretKey)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(secretKey)
//                    .build()
//                    .parseClaimsJws(token);
//            return true;
//        } catch (io.jsonwebtoken.ExpiredJwtException e) {
//            System.out.println("Token is expired: " + e.getMessage()); // Corrected to use concatenation
//        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
//            System.out.println("Unsupported JWT token: " + e.getMessage()); // Corrected to use concatenation
//        } catch (io.jsonwebtoken.MalformedJwtException e) {
//            System.out.println("Malformed JWT token: " + e.getMessage()); // Corrected to use concatenation
//        } catch (Exception e) {
//            System.out.println("Token validation error: " + e.getMessage()); // Corrected to use concatenation
//        }
//        return false;
//    }
//
//
//}

package com.logmaven.exmaven.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;

@Component
public class JwtUtil {

    private Key secretKey;

    @PostConstruct
    public void init() {
        // Generate a secure key for HS256
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(secretKey)
                .compact();
    }

    public String getUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse token: " + e.getMessage());
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.out.println("Token is expired: " + e.getMessage());
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            System.out.println("Unsupported JWT token: " + e.getMessage());
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            System.out.println("Malformed JWT token: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Token validation error: " + e.getMessage());
        }
        return false;
    }

    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse token: " + e.getMessage());
        }
    }
}

