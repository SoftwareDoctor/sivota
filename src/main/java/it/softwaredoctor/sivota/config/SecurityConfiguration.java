package it.softwaredoctor.sivota.config;

import it.softwaredoctor.sivota.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final CustomUserDetailsService customUserDetailsService;


    @Bean
    public RequestMatcher notXmlHttpRequestMatcher() {
        return new NegatedRequestMatcher(new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"));
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(notXmlHttpRequestMatcher()).permitAll()
                        .requestMatchers("/api/v1/user").permitAll()
                        .requestMatchers("api/v1/user/login").permitAll()
                        .requestMatchers("/api/v1/votazione/view").permitAll()
                        .requestMatchers("api/v1/user/logout").authenticated()
                        .requestMatchers("/api/v1/votazione/**").authenticated()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .cors(cors -> cors.disable())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(withDefaults())
                .rememberMe(rememberMe -> rememberMe
                        .tokenValiditySeconds(60)
                        .userDetailsService(customUserDetailsService)
                ).logout(logout -> logout
                        .deleteCookies("JSESSIONID") // Se si utilizza HttpSession, invalidarla
                )
                .build();
    }
}





