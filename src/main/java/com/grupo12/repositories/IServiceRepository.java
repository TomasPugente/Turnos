package com.grupo12.repositories;

import com.grupo12.entities.ServiceEntity;
import com.grupo12.entities.Turn;
import com.grupo12.models.TurnDTO;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IServiceRepository extends JpaRepository<ServiceEntity, Long> {

	Optional<ServiceEntity> findByIdService(Long serviceId);

	//Optional<ServiceDTO> findById(Integer serviceId);

	//Optional<Turn> findById(Long serviceId);

}
