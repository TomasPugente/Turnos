package com.grupo12.config;

import com.grupo12.security.UserDetailsServiceImpl; // Este es el bueno

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }
    /*
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }*/
  /*  @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/images/**", "/vendor/**").permitAll()
                .requestMatchers("/login", "/loginprocess").permitAll()
               // .requestMatchers("/turns/enable/**", "/turns/estado", "/turns/recordatorios").hasRole("EMPLOYEE")
                .requestMatchers("/turns/enable/**", "/turns/estado", "/turns/recordatorios").permitAll()
                .requestMatchers("/web/turns/**").permitAll()//cambiar a hasRole(employee) se pone all para poder hacer pruebas
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/loginprocess")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/loginsuccess", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/logout")
                .permitAll()
            )
            .build();
    }*/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable()) // opcional, pero útil para formularios sin token
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // 🔓 PERMITIR TODO
            )
            .formLogin(form -> form.disable()) // 🚫 DESACTIVAR LOGIN
            .logout(logout -> logout.disable()) // 🚫 DESACTIVAR LOGOUT
            .build();
    }
}
