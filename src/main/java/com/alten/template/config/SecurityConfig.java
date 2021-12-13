package com.alten.template.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] SWAGGER_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    private static final String[] WHITELIST = { "/public/test" };



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .authorizeRequests().antMatchers(WHITELIST).permitAll().and()
                .authorizeRequests().antMatchers(SWAGGER_WHITELIST).permitAll().and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .oauth2ResourceServer()
                .authenticationEntryPoint(new CustomOAuth2AuthenticationEntryPoint())
                .jwt();
    }

    public static class CustomOAuth2AuthenticationEntryPoint implements AuthenticationEntryPoint {
        Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

        @Override
        public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException authenticationException) throws IOException {

            logger.warn("Unauthenticated request to uri " + httpServletRequest.getRequestURI() +
                    " made from IP " + getIPAddress(httpServletRequest));
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            final Map<String, Object> body = new HashMap<>();
            body.put("status", HttpServletResponse.SC_UNAUTHORIZED);

            body.put("error", "Valid Bearer token is required");

            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(httpServletResponse.getOutputStream(), body);
        }
    }

    private static String getIPAddress(HttpServletRequest request){
        String ipAddress = request.getHeader("X-Forward-For");
        if(ipAddress == null){ ipAddress = request.getRemoteAddr(); }
        return ipAddress;
    }
}

