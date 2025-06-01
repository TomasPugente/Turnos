package com.grupo12.models;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContactDTO {

    private int idContact;

    @NotBlank(message = "Debe ingresar la calle")
    private String street;
    
    @Pattern(regexp = "\\d{4}", message = "El numero de la direccion debe tener 4 dígitos numéricos")
    private String number;
    
    @NotBlank(message = "Debe ingresar su email")
    private String email;
    
    @Pattern(regexp = "\\d{10}", message = "el numero de celular debe tener 10 digitos numericos")
    private String phone;

    @Valid
    @NotNull(message = "Debe seleccionar una localidad")
    private LocalityDTO locality;

	public ContactDTO(int idContact, String street, String number, String email, String phone, LocalityDTO locality) {
		this.idContact = idContact;
		this.street = street;
		this.number = number;
		this.email = email;
		this.phone = phone;
		this.locality = locality;
	}
}
