package com.grupo12.repositories;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.grupo12.entities.Client;
import com.grupo12.entities.User;


@Repository("clientRepository")
public interface IClientRepository extends JpaRepository<Client, Serializable> {
	
	boolean existsByDni(String dni);
	
	boolean existsByUser(User user);
	
	Optional<Client> findByDni(String dni);
	
	Optional<Client> findByUserUsername(String username);
	
    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.user WHERE c.idPerson = :id")
    Optional<Client> getByIdWithUser(@Param("id") Integer id);
	
	
}
