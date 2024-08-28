package com.app.weblab5.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user1")
                        .password("pass")
                        .roles("ADMIN", "USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/home").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/rent_car").permitAll()
                        .requestMatchers(HttpMethod.POST, "/register_proc").permitAll()
                        .requestMatchers("/rental_car_list").hasRole("USER")
                        .requestMatchers("/cars").permitAll()
                        .requestMatchers(HttpMethod.POST, "/cars_add").permitAll()
                        .requestMatchers(HttpMethod.POST, "/cars_edit").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/cars_del/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/release_proc").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/request_proc").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/bill_proc").hasRole("USER")
                        .requestMatchers("/rental_car_list").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin((form) -> form
                        .loginPage("/login").permitAll().defaultSuccessUrl("/home")
                        .successHandler(successHandler())
                )
                .logout((logout) -> logout.permitAll().logoutSuccessUrl("/home"));
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
