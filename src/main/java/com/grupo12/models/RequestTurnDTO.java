package com.grupo12.models;



import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestTurnDTO {

	@NotNull(message = "Debés ingresar tu ID de cliente.")
    private Integer clientId;
	
    @NotNull(message = "Debés seleccionar un turno.")
    @Min(value = 1, message = "Seleccioná un turno válido.")	
    private Integer turnId;
}
