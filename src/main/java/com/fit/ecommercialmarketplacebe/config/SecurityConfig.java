package com.fit.ecommercialmarketplacebe.config;

import com.fit.ecommercialmarketplacebe.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired private JwtAuthenticationFilter jwtAuthFilter;
    @Autowired private UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .authorizeHttpRequests(auth -> auth
                        // 1. PUBLIC
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/files/images/**").permitAll()
                        .requestMatchers("/images/**").permitAll()

                        // 2. SELLER (Admin)
                        .requestMatchers("/api/orders/admin/**").hasRole("SELLER") // Nắm bắt /admin, /admin/{id}
                        .requestMatchers("/api/products/seller").hasRole("SELLER")
                        .requestMatchers(HttpMethod.POST, "/api/products").hasRole("SELLER")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("SELLER") // Đặt PUT/DELETE chung chung ở dưới
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("SELLER")
                        .requestMatchers(HttpMethod.POST, "/api/files/upload").hasRole("SELLER")
                        .requestMatchers("/api/seller/**").hasRole("SELLER")
                        .requestMatchers("/api/analytics/**").hasRole("SELLER")
                        .requestMatchers("/api/users/**").hasRole("SELLER")

                        // 3. BUYER
                        .requestMatchers(HttpMethod.POST, "/api/orders/checkout").hasRole("BUYER")
                        .requestMatchers(HttpMethod.GET, "/api/orders/history").hasRole("BUYER")
                        .requestMatchers("/api/cart/**").hasRole("BUYER")
                        .requestMatchers(HttpMethod.GET, "/api/payment/methods").hasRole("BUYER")
                        .requestMatchers("/api/buyer/**").hasRole("BUYER")

                        .requestMatchers(HttpMethod.GET, "/api/orders/{orderId}").hasRole("BUYER")

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

