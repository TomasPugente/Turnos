package com.grupo12.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
public class LocalityDTO {

	@NotNull(message = "Debe seleccionar una localidad")
	@Min(value = 1, message = "Debe seleccionar una localidad válida")
	private Integer idLocality;
 
	
	private String name;

	public LocalityDTO(Integer idLocality, String name) {
		this.idLocality = idLocality;
		this.name = name;
	}
}
