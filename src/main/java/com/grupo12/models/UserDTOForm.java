package com.grupo12.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTOForm {

	private Integer id;
	
    @NotNull(message = "Debe que ingresar un nombre de usuario")
    @Size(min = 4, max = 20, message = "El nombre de usuario debe tener entre 4 y 20 caracteres")
    private String username;

    @NotNull(message = "Debe que ingresar un un email")
    @Email(message = "El email no es válido")
    private String email;
    
    @NotNull(message = "Debe que ingresar una contraseña")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    private Boolean enabled;
    
    
}
