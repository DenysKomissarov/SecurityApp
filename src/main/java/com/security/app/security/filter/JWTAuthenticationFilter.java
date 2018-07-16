package com.security.app.security.filter;

import com.security.app.security.token.JWTToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String SECURED_URL = "/user";

    public JWTAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return request.getRequestURI().contains(SECURED_URL)
                && super.requiresAuthentication(request, response)
                && !request.getMethod().equals(HttpMethod.OPTIONS.name());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String tokenFromHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String tokenFromParam = tokenFromHeader==null ? httpServletRequest.getParameter("authorization") : null;

        if (tokenFromHeader == null && tokenFromParam == null) {
            throw new SessionAuthenticationException("Auth token is null");
        }

        return getAuthenticationManager().authenticate(new JWTToken(tokenFromHeader!=null ? tokenFromHeader : tokenFromParam));

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
