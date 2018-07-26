package com.security.security.jwt.service;


import com.security.data.audit.details.UserPrincipal;
import com.security.data.audit.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class JWTService {


    private final String secret;

    private final int tokenValidHours;
    public JWTService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.tokenValidHours}") int tokenValidHours) {
        this.secret = secret;
        this.tokenValidHours = tokenValidHours;
    }

    public String generateToken(User account) throws UnsupportedEncodingException {

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.add(Calendar.HOUR, tokenValidHours);

        Map<String, Object> cailms = new HashMap<>();
        cailms.put("id", account.getId());
        cailms.put("email", account.getEmail());
        cailms.put("userName", account.getUsername());
        cailms.put("roles", account.getRoles());

        return Jwts.builder().setSubject("ICO token")
                .setExpiration(calendar.getTime())
                .setClaims(cailms)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    @SuppressWarnings("unchecked")
    public UserPrincipal getUserDetailFromToken(String token){
        Jws<Claims> userDetailsParts = parseToken(token);

        User user = new User();
        user.setEmail((String) userDetailsParts.getBody().get("email"));
        user.setId((UUID) userDetailsParts.getBody().get("id"));

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

}
