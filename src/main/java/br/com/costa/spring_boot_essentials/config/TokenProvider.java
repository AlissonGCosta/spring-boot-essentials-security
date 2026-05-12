package br.com.costa.spring_boot_essentials.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class TokenProvider {

    @Value("${JWT_EXPIRATION}")
    private long expirationTime;

    @Value("${JWT_KEY}")
    private String key;

    //gerar token
    public String gerarToken(Authentication authentication) {
       UserDetails user = (UserDetails)authentication.getPrincipal();
        return buildTolken(user.getUsername());
    }

    private String buildTolken(String user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .subject(user)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigninKey())
                .compact();
    }

    private SecretKey getSigninKey() {
        return Keys.hmacShaKeyFor(key.getBytes());
    }

    //validar token

    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        }catch(Exception e) {

            return false;
        }
    }

    private Claims getClaims(String token){
        //validar assinatura
        //validar expiração

        return Jwts.parser()
                .verifyWith(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

    //extrair infos tokens
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

}
