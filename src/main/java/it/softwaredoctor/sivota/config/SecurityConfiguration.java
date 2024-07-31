package it.softwaredoctor.sivota.config;

import it.softwaredoctor.sivota.service.CustomUserDetailsService;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

//@Configuration
//@RequiredArgsConstructor
////@EnableWebSecurity
//@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
//public class SecurityConfiguration {
//
////    @Autowired
////    private UserDetailsService userDetailsService;
//
//    @Bean
//    BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
////    @Bean
////    public AnonymousAuthenticationFilter anonymousAuthenticationFilter() {
////        return new AnonymousAuthenticationFilter("anonymous");
////    }
//
//
////    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
////    }
//
//
////    protected void configure(HttpSecurity http) throws Exception {
////         http.csrf(AbstractHttpConfigurer::disable);
////
////    }
//
//
////    @Bean
////    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////        http
////
////                .authorizeHttpRequests((authorize) -> authorize
////                        .requestMatchers(HttpMethod.POST, "/api/v1/**").permitAll()
////                        .anyRequest().authenticated()
////                )
////                .sessionManagement(Customizer.withDefaults())
////                .httpBasic(Customizer.withDefaults());
////
////
////        return http.build();
////    }
//
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http
//                .authorizeHttpRequests(authz -> {
//                    authz
////                            .requestMatchers("/api/v1/votazione/**").authenticated()
//                            .requestMatchers("/api/v1/user/**").permitAll()
////                            .requestMatchers(HttpMethod.POST, "/api/v1/user/login/**").permitAll()
//                            .anyRequest().authenticated();
//                })
////                .exceptionHandling(config -> config
////                        .defaultAuthenticationEntryPointFor(
////                                new Http403ForbiddenEntryPoint(),
////                                new AntPathRequestMatcher("/**"))
////                )
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .cors(cors -> cors.disable())
//                .csrf(AbstractHttpConfigurer::disable)
//                .httpBasic(Customizer.withDefaults())
//                .rememberMe(rememberMe -> rememberMe
//                        .tokenValiditySeconds(3600)
//                        .userDetailsService(userDetailsService)
//                )
//                .build();
//    }
//

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

//    private final UserDetailsService userDetailsService;

    @Bean
    public RequestMatcher notXmlHttpRequestMatcher() {
        return new NegatedRequestMatcher(new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"));
    }

//    @Bean
//    public AuthenticationManager authenticationManager() {
//        return new ProviderManager(Collections.singletonList(authenticationProvider()));
//    }
//
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//        authProvider.setHideUserNotFoundExceptions(false);
//        return authProvider;
//    }

//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//        authProvider.setHideUserNotFoundExceptions(false);
//        return authProvider;
//    }

protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
}


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(notXmlHttpRequestMatcher()).permitAll()
//                        .requestMatchers(HttpMethod.POST,"/api/v1/user/**").permitAll()
                                .requestMatchers("/api/v1/user").permitAll()
                                .requestMatchers("api/v1/user/login").permitAll()
                                .requestMatchers("/api/v1/votazione/view").permitAll()
                                .requestMatchers("api/v1/user/logout").authenticated()
                        .requestMatchers("/api/v1/votazione/**").authenticated()
                        .anyRequest().authenticated()

                )

//                .authenticationProvider(authenticationProvider())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                        .maximumSessions(1)
//                        .expiredUrl("/login?expired=true")
                       )
                .cors(cors -> cors.disable())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(withDefaults())
//                .formLogin(withDefaults())
                .rememberMe(rememberMe -> rememberMe
                        .tokenValiditySeconds(60)
                        .userDetailsService(customUserDetailsService)
                )  .logout(logout -> logout
//                                .logoutRequestMatcher(new AntPathRequestMatcher("/api/v1/user/logout")).permitAll()
//                        .logoutUrl("/api/v1/user/logout") // URL per il logout
//                        .logoutSuccessUrl("/login?logout") // Redirect dopo il logout
//                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID") // Se si utilizza HttpSession, invalidarla
                )
                .build();
    }
//    @Bean
//    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
//        StrictHttpFirewall firewall = new StrictHttpFirewall();
//        firewall.setAllowUrlEncodedPercent(true);
//        firewall.setAllowUrlEncodedSlash(true);
//        firewall.setAllowUrlEncodedPeriod(true);
//        firewall.setAllowSemicolon(true);
//        return firewall;
//    }

}





