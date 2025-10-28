package com.fit.ecommercialmarketplacebe.config;

import com.fit.ecommercialmarketplacebe.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;

import java.util.List;

@Configuration
public class SecurityConfig {
    @Autowired private JwtAuthenticationFilter jwtAuthFilter;
    @Autowired private UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/cart/items").hasRole("BUYER")
                        .requestMatchers(HttpMethod.PUT, "/api/cart/items/**").hasRole("BUYER")
                        .requestMatchers(HttpMethod.DELETE, "/api/cart/items/**").hasRole("BUYER")
                        .requestMatchers(HttpMethod.GET, "/api/cart").hasRole("BUYER")
                        .requestMatchers(HttpMethod.POST, "/api/orders/checkout").hasRole("BUYER")
                        .requestMatchers(HttpMethod.GET, "/api/payment/methods").hasRole("BUYER")

                        .requestMatchers(HttpMethod.GET, "/api/orders").hasRole("SELLER")
                        .requestMatchers(HttpMethod.GET, "/api/orders/buyer").hasRole("BUYER")
                        .requestMatchers(HttpMethod.GET, "/api/orders/**").hasAnyRole("BUYER", "SELLER")
                        .requestMatchers(HttpMethod.DELETE, "/api/orders/**").hasRole("SELLER")
                        .requestMatchers("/api/seller/**").hasRole("SELLER")
                        .requestMatchers("/api/buyer/**").hasRole("BUYER")
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    @Bean public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


}

