package com.grupo12.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import com.grupo12.entities.Employee;

public interface IEmployeeRepository extends JpaRepository<Employee, Serializable> {

}
