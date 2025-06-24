package com.grupo12.repositories;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.grupo12.entities.Employee;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Integer> {
	boolean existsByDni(String dni);
	
	Optional<Employee> findByUserEmail(String email);

	Optional<Employee> findById(int idPerson);// se hereda de jparepository

	void deleteById(int idPerson);

    Optional<Employee> findByDni(String dni);

	Optional<Employee> findByCuit(String cuit);
}
