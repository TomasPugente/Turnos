package com.grupo12.models;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Valid
public abstract class PersonDTO {

    // Pongo Integer para poder diferenciar entre: null → está creando un nuevo
    // cliente.
    // valor distinto de null → está editando uno existente
    protected Integer idPerson;

    @NotBlank(message = "Debe ingresar su nombre")
    protected String name;

    // para asegurar de que solo sean numeros
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos numéricos")
    protected String dni;

    @NotNull(message = "Debe ingresar su fecha de nacimiento")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    protected LocalDate dateOfBirth;

    @NotBlank(message = "Debe ingresar su contraseña")
    protected String password;

    @Valid
    protected ContactDTO contact;

    public PersonDTO(Integer idPerson, String name, String dni, LocalDate dateOfBirth, String password,
            ContactDTO contact) {
        super();
        this.idPerson = idPerson;
        this.name = name;
        this.dni = dni;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
        this.contact = contact;
    }

}
