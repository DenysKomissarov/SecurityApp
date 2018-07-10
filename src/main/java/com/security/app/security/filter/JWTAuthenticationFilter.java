package com.security.app.security.filter;

import com.security.app.security.CustomUserDetailsService;
import com.security.app.security.JWTService;
import com.security.app.security.token.JWTToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && jwtService.validateToken(jwt)) {
                Long userId = jwtService.getUserIdFromJWT(jwt);

                UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication); // FIXME  You need to avoid manually setting principals
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

//    public static final String SECURED_URL = "/secured";
//
//    public JWTAuthenticationFilter(String defaultFilterProcessesUrl) {
//        super(defaultFilterProcessesUrl);
//    }
//
//    @Override
//    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
//
//        setHeaders(response);
//
//        return request.getRequestURI().contains(SECURED_URL)
//                && super.requiresAuthentication(request, response)
//                && !request.getMethod().equals(HttpMethod.OPTIONS.name());
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
//        String tokenFromHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
//        String tokenFromParam = tokenFromHeader==null ? httpServletRequest.getParameter("authorization") : null;
//
//        if (tokenFromHeader == null && tokenFromParam == null) {
//            throw new SessionAuthenticationException("Auth token is null");
//        }
//
//        return getAuthenticationManager().authenticate(new JWTToken(tokenFromHeader!=null ? tokenFromHeader : tokenFromParam));
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        super.successfulAuthentication(request, response, chain, authResult);
//        chain.doFilter(request, response);
//    }
//
//    public void setHeaders(HttpServletResponse response) {
//        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
//        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
//        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
//        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "*");
//    }
}
