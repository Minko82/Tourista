package com.supercoolproject.tourista.config;

// Login security doc: https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/index.html#servlet-authentication-unpwd
// Requires https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-security/3.2.4

import com.supercoolproject.tourista.service.UserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        // Rules that allow automatic authentication
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers("/profiles/signup/**").permitAll()
                        // Checks that the pages are authenticated through the cookie
                        .anyRequest().authenticated()
                )
                // CSRF protection is automatically implemented by Spring. Must turn off for H2
                .csrf(csrf -> csrf.ignoringRequestMatchers(PathRequest.toH2Console()))
                // Iframe is automatically denied in Spring. This allows the iframe to display if
                // it's from the same domain (in our instances, anything from localhost)
                .headers(headers -> headers.frameOptions(frameOptionsConfig -> frameOptionsConfig.sameOrigin()))
                // Requires the user to be signed in to access any pages
                .formLogin((form) -> form.loginPage("/login").permitAll().defaultSuccessUrl("/"))
                .logout((logout) -> logout.permitAll())
        ;

        return http.build();
    }

    // DaoAuthenticationProvider is a simple authentication provider that uses a
    // Data Access Object (DAO) to retrieve user information from a relational database.
    @Bean
    public AuthenticationManager authenticationManager(UserService userService,
                                                       PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
