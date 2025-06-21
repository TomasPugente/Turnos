package com.grupo12.services.implementation;

import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo12.converters.ServicesConverter;
import com.grupo12.models.ServiceDTO;


import com.grupo12.repositories.IServiceRepository;
import com.grupo12.services.IServiceService;

//import com.grupo12.converters.ServicesConverter;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ServiceService implements IServiceService{

	@Autowired
	private IServiceRepository serviceRepository;
	
	@Autowired
	private ServicesConverter serviceConverter;
	
	@Override
	public ServiceDTO createService(ServiceDTO serviceDTO) {
		// TODO Auto-generated method stub
		com.grupo12.entities.ServiceEntity service=serviceConverter.toEntity(serviceDTO);
		com.grupo12.entities.ServiceEntity savedService=serviceRepository.save(service);

		return serviceConverter.toDTO(savedService);
	}
	
	@Override
	public Optional<ServiceDTO>  getServiceEntityByIdService(Long id){
		return serviceRepository.findById(id).map(serviceConverter::toDTO);
	}
	
	@Override
	public List<ServiceDTO> getAllServices() {
		// TODO Auto-generated method stub
		return serviceRepository.findAll().stream().map(serviceConverter::toDTO).collect(Collectors.toList());
	}

	@Override
	public ServiceDTO updateService(Long id, ServiceDTO serviceDTO) {
		// TODO Auto-generated method stub
		com.grupo12.entities.ServiceEntity existingService=serviceRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Servicio no encontrado con ID: "+id));
		existingService.setName(serviceDTO.getName());
		existingService.setDetail(serviceDTO.getDescription());
		existingService.setDurationMinutes(serviceDTO.getDurationMinutes());
		com.grupo12.entities.ServiceEntity updateService=serviceRepository.save(existingService);
		return serviceConverter.toDTO(updateService);
	}
	
	@Override
	public void deleteService(Long id) {
		// TODO Auto-generated method stub
		serviceRepository.deleteById(id);

	}

	

}
