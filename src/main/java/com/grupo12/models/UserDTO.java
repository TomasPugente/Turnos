package com.grupo12.models;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserDTO {
    @NotNull(message = "You must provide a username")
    private String username;

    @NotNull(message = "You must provide an email")
    private String email;
    @NotNull(message = "You must provide a password")
    private String password;
    @NotNull(message = "You must provide a password")
    private String confirmPassword;
}
