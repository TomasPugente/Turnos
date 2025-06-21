package com.grupo12.services;

import com.grupo12.models.ServiceDTO;
import java.util.List;
import java.util.Optional;

public interface IServiceService {
	ServiceDTO createService(ServiceDTO serviceDTO);
	Optional<ServiceDTO> getServiceEntityByIdService(Long id);
	List<ServiceDTO>getAllServices();
	ServiceDTO updateService(Long id,ServiceDTO serviceDTO);
	void deleteService(Long id);
}
