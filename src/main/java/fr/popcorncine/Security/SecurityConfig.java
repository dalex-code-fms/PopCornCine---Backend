package fr.popcorncine.Security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Configure request authorization
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/users/register").permitAll() // Public access to register
                        .requestMatchers("/api/users/login").permitAll()
                        .requestMatchers("/api/users/confirm").permitAll() // Public access to login (optional)
                        .anyRequest().authenticated()                       // All other endpoints require authentication
                )
                // Disable CSRF (replacement for csrf().disable())
                .csrf(csrf -> csrf.disable())
                // Configure HTTP Basic Authentication (replacement for httpBasic())
                .httpBasic(httpBasic -> httpBasic
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                )
                // Optional: Disable session management if this is a stateless API
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
}