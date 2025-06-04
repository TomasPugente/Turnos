package com.grupo12.models;

import jakarta.validation.constraints.Min;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
public class LocalityDTO {

	@Min(value = 1, message = "Debe seleccionar una localidad válida")
    private Integer idLocality;

    private String name;

	public LocalityDTO(Integer idLocality, String name) {
		this.idLocality = idLocality;
		this.name = name;
	}
}
