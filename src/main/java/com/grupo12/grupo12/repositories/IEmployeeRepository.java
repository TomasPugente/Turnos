package com.grupo12.repositories;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.grupo12.entities.Employee;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Long> {
	boolean existsByDni(String dni);
	
	Optional<Employee> findByEmail(String email);

	Optional<Employee> findById(int idPerson);

	void deleteById(int idPerson);
}
