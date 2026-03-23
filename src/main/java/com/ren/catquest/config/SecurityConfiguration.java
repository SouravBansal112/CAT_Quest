package com.ren.catquest.config;

import com.ren.catquest.security.jwt.JwtAuthenticationFilter;
import com.ren.catquest.security.model.SecurityUser;
import com.ren.catquest.user.entity.User;
import com.ren.catquest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(); // industry standard
//    }
//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder() {
        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            boolean result = super.matches(rawPassword, encodedPassword);
            System.out.println("Password match: " + result + " | raw: " + rawPassword + " | encoded: " + encodedPassword);
            return result;
        }
    };
}
@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    AuthenticationManager delegate = config.getAuthenticationManager();

    return authentication -> {
        System.out.println("Authenticating: " + authentication.getPrincipal());
        var result = delegate.authenticate(authentication);
        System.out.println("Authentication result: " + result.isAuthenticated());
        return result;
    };
}
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",   // login, register
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/",
                                "/api/jobs"
                        ).permitAll()

                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}

