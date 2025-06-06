package com.grupo12.models;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
