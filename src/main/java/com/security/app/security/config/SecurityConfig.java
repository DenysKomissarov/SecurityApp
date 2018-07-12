package com.security.app.security.config;

import com.security.app.security.filter.JWTAuthenticationFilter;
import com.security.app.security.provider.JWTProvider;
import com.security.app.security.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String SECURED_PATH = "/**";

    @Autowired
    private JWTService jwtService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider provider(){
        return new JWTProvider(jwtService);
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(provider()));
    }

    @Bean
    public JWTAuthenticationFilter filter() throws Exception {
        JWTAuthenticationFilter filter = new JWTAuthenticationFilter(SECURED_PATH);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler((req, resp, auth) -> {});
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(org.springframework.http.HttpMethod.OPTIONS).permitAll()
                .antMatchers("/user").authenticated()
                .and()
                .addFilterBefore(filter(), UsernamePasswordAuthenticationFilter.class);
    }
}