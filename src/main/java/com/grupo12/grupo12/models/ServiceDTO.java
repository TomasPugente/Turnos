package com.grupo12.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {
	private int id;
	private String name;
	private String description;
	private int durationMinutes;
	
}
