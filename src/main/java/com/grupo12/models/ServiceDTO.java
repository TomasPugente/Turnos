package com.grupo12.models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServiceDTO {
	private int idService;
	private String name;
	private String description;
	private int durationMinutes;
	
}