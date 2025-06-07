package com.grupo12.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


import com.grupo12.services.implementation.UserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	http.csrf(csrf->csrf.disable()).authorizeHttpRequests(authorize->authorize.requestMatchers("/public/**").permitAll().requestMatchers("/admin/**").hasRole("ADMIN").anyRequest().authenticated()).formLogin(Customizer.withDefaults()).httpBasic(Customizer.withDefaults());
        return http.build();
    }

}
