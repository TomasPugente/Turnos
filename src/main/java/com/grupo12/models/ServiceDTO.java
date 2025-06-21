package com.grupo12.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {
	private int idServicio;
	private String serviceName;
	private String detail;
	private int durationMinutes;
	
}
