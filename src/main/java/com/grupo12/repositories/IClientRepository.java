package com.grupo12.repositories;

import java.io.Serializable;
import java.util.Optional;

import com.grupo12.models.ClientDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo12.entities.Client;

@Repository("clientRepository")
public interface IClientRepository extends JpaRepository<Client, Serializable> {
	
	boolean existsByDni(String dni);
	
	Optional<Client> findByDni(String dni);;

	Optional<Client> findByIdPerson(Integer idPerson);
	
}
