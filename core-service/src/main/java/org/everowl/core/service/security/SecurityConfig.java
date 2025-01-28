package org.everowl.core.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * The SecurityConfiguration class is responsible for configuring the security settings of the application.
 * It provides the necessary configurations for authentication, authorization, and session management.
 * This class uses Spring Security's new lambda DSL for configuring security rules.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final JwtAuthFilter jwtAuthFilter;

    /**
     * Configures the SecurityFilterChain bean to define the security rules for the application.
     *
     * @param http The HttpSecurity object to be configured
     * @return The configured SecurityFilterChain
     * @throws Exception If an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                // Disable CSRF protection
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // Configure authorization rules
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        // Permit requests to specific authentication endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/customer/**").hasRole("CUSTOMER")
                        .requestMatchers("/api/staff/**").hasRole("STAFF")
                        .requestMatchers("/api/owner/**").hasRole("OWNER")
                        // Require authentication for all other requests
                        .anyRequest()
                        .authenticated()
                )
                // Configure session management
                .sessionManagement(session -> session
                        // Use stateless session management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Set the authentication provider
                .authenticationProvider(authenticationProvider)
                // Configure logout behavior
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        // Clear the security context after logout
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));

        return http.build();
    }
}