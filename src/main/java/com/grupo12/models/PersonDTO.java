package com.grupo12.models;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@Valid
@Getter
@Setter
public abstract class PersonDTO {

    // Pongo Integer para poder diferenciar entre: null → está creando un nuevo
    // cliente.
    // valor distinto de null → está editando uno existente
    protected Integer idPerson;

    @NotBlank(message = "Debe ingresar su nombre")
    protected String name;

    @NotBlank(message = "Debe ingresar su apellido")
    protected String surname;

    // para asegurar de que solo sean numeros
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos numéricos")
    protected String dni;

    @NotNull(message = "Debe ingresar su fecha de nacimiento")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    protected LocalDate dateOfBirth;

    @Valid
    protected UserDTO user;

    @Valid
    protected ContactDTO contact;


    public PersonDTO(Integer idPerson, String name, String dni, LocalDate dateOfBirth, ContactDTO contact) {
        super();
    }
    
    public PersonDTO(Integer idPerson, String name, String surname, String dni, LocalDate dateOfBirth, ContactDTO contact, UserDTO user) {
        this.idPerson = idPerson;
        this.name = name;
        this.surname = surname;
        this.dni = dni;
        this.dateOfBirth = dateOfBirth;
        this.contact = contact;
        this.user = user;
    }

}
