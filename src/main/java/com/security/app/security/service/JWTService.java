package com.security.app.security.service;

import com.security.app.model.User;
import com.security.app.security.UserPrincipal;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class JWTService {

    @Value("${app.jwtSecret}")
    private String secret;

    @Value("${app.tokenValidHours}")
    private int tokenValidHours;

    private static final Logger logger = LoggerFactory.getLogger(JWTService.class);

    public String generateToken(User account) throws UnsupportedEncodingException {

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.add(Calendar.HOUR, tokenValidHours);

        Map<String, Object> cailms = new HashMap<>();
        List<String> userRoles;
        cailms.put("id", Long.toString(account.getId()));
        cailms.put("email", account.getEmail());
        cailms.put("userName", account.getUsername());
        cailms.put("roles", account.getRoles());

        return Jwts.builder().setSubject("ICO token")
                .setExpiration(calendar.getTime())
                .setClaims(cailms)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }


    public UserPrincipal getUserDetailFromToken(String token){
        Jws<Claims> userDetailsParts = parseToken(token);

        User user = new User();
        user.setEmail((String) userDetailsParts.getBody().get("email"));
        user.setId(Long.parseLong(userDetailsParts.getBody().get("id").toString()));

        Set<String> roles = new LinkedHashSet<>(userDetailsParts.getBody().get("roles", ArrayList.class));
        user.setRoles(roles);

        return new UserPrincipal(user);
    }

    private Jws<Claims> parseToken(String token){
        Jws<Claims> claimsJwt = null;

        claimsJwt = Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(token);

        return claimsJwt;
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }


}
