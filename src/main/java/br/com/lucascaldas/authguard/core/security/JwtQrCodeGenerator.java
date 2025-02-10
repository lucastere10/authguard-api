package br.com.lucascaldas.authguard.core.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@Configuration
@ConfigurationProperties(prefix = "qrcode.security.token")
@Slf4j
public class JwtQrCodeGenerator {

    private String secret;

    public String generateToken(Long room) {
        log.debug("Generating token with room: {}", room);
        long expirationUnix = (System.currentTimeMillis() / 1000) + 45;
        return Jwts.builder()
                .claim("label", "QrCodeAuthenticator")
                .claim("issuer", "Authguard")
                .claim("room", room)
                .setExpiration(new Date(expirationUnix * 1000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> decipherToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token);
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    public void setSecret(String secret) {
        this.secret = secret;
    }
}