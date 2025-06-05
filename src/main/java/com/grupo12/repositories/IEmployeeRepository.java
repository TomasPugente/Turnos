package com.grupo12.repositories;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo12.entities.Employee;

public interface IEmployeeRepository extends JpaRepository<Employee, Serializable> {
    boolean existsByDni(String dni);

    Optional<Employee> findByDni(String dni);;
}
