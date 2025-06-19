/*package com.grupo12.converters;

import com.grupo12.entities.Service;
import com.grupo12.models.ServiceDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServicesConverter {
	@Autowired
	private ModelMapper modelMapper;
	public ServiceDTO toDTO(Service service) {
		return modelMapper.map(service, ServiceDTO.class);
	}
	public Service toEntity(ServiceDTO serviceDTO) {
		return modelMapper.map(serviceDTO, Service.class);
	}
}*/
