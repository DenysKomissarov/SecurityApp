package com.security.security.jwt.provider;


import com.security.security.jwt.JWTToken;
import com.security.security.jwt.service.JWTService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.cache.NullUserCache;

public class JWTProvider extends AbstractUserDetailsAuthenticationProvider {

    private final JWTService serivce;

    public JWTProvider(JWTService serivce) {
        this.serivce = serivce;
        this.setUserCache(new NullUserCache());
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        String token = String.valueOf(authentication.getPrincipal());
        return serivce.getUserDetailFromToken(token.substring(7, token.length()));
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return JWTToken.class.isAssignableFrom(authentication);
    }
}
