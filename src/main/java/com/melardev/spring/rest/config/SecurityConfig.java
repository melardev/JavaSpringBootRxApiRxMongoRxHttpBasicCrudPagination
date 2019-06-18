package com.melardev.spring.rest.config;

import com.melardev.spring.rest.services.AppUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {


    private DelegatingPasswordEncoder passwordEncoder;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http
                // Disable CSRF, we are in an API and not using forms
                .csrf().disable()
                .authorizeExchange()
                // Allow anything, restriction will be enforce through anotations
                .pathMatchers("/**").permitAll()
                // .anyExchange().authenticated()
                .and()
                // Enable HTTP Basic authentication
                .httpBasic()
                .and()
                // No form login
                .formLogin().disable()
                .build();
    }

    @SuppressWarnings("deprecation")
    @Bean
    public PasswordEncoder passwordEncoder() {
        if (passwordEncoder == null) {
            String encodingId = "bcrypt";
            Map<String, PasswordEncoder> encoders = new HashMap<>();
            encoders.put(encodingId, new BCryptPasswordEncoder());
            encoders.put("ldap", new org.springframework.security.crypto.password.LdapShaPasswordEncoder());
            encoders.put("MD4", new org.springframework.security.crypto.password.Md4PasswordEncoder());
            encoders.put("MD5", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("MD5"));
            encoders.put("noop", org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());
            encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
            encoders.put("scrypt", new SCryptPasswordEncoder());
            encoders.put("SHA-1", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-1"));
            encoders.put("SHA-256", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-256"));
            encoders.put("sha256", new org.springframework.security.crypto.password.StandardPasswordEncoder());

            passwordEncoder = new DelegatingPasswordEncoder(encodingId, encoders);
            passwordEncoder.setDefaultPasswordEncoderForMatches(new BCryptPasswordEncoder(10));
        }
        return passwordEncoder;
    }

    @Bean
    public ReactiveUserDetailsService inMemoryUserDetailsService(PasswordEncoder passwordEncoder) {
        // The prefix {noop} is there because I use the new DelegatingPasswordEncoder

        String password = passwordEncoder.encode("user");
        // password = "{noop}user";
        UserDetails user = User.builder()
                .username("user")
                .password(password)
                .roles("USER")
                .build();

        return new MapReactiveUserDetailsService(Collections.singleton(user));
    }

    @Bean
    @Primary
    public ReactiveUserDetailsService dbUserDetailsService() {
        return new AppUserDetailsService();
    }
}
