package com.grupo12;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestBCryptPasswordEncoder {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "user"; // Puedes cambiar esto por cualquier contraseña
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
    }
}
