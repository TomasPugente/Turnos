package com.grupo12.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
	
	private Integer id;
	
	@NotBlank(message = "Debe que ingresar un nombre de usuario")
    @Size(min = 4, max = 20, message = "El nombre de usuario debe tener entre 4 y 20 caracteres")
    private String username;

    @NotBlank(message = "Debe que ingresar un un email")
    @Email(message = "El email no es válido")
    private String email;
    
    @NotBlank(message = "Debe ingresar una contraseña")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
    
    @NotBlank(message = "Debe ingresar confirmar la contraseña")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String confirmPassword;
    
    private Boolean enabled;
    
    private String resetToken;
    
    
    
    /*public boolean isPasswordConfirmed() {
        return password != null && password.equals(confirmPassword);
    }*/
    
    


}
