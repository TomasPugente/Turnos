package com.grupo12.services;

import com.grupo12.entities.ServiceEntity;
import com.grupo12.models.ServiceDTO;
import java.util.List;
import java.util.Optional;

public interface IServiceService {
	ServiceDTO createService(ServiceDTO serviceDTO);
	Optional<ServiceDTO> getServiceById(Long id);
	List<ServiceDTO>getAllServices();
	ServiceDTO updateService(Long id,ServiceDTO serviceDTO);
	void deleteService(Long id);
	List<ServiceDTO> getAll();
	List<ServiceDTO> getAllDTO();
}
