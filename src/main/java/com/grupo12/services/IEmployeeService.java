package com.grupo12.services;

import java.util.List;
import java.util.Optional;

import com.grupo12.entities.Employee;
import com.grupo12.models.EmployeeDTO;

public interface IEmployeeService {

	public List<Employee> getAll();
	
	public void insertOrUpdate(EmployeeDTO employeeDTO);

	Optional<Employee> getByCuit(String cuit);

	boolean remove(Integer id);

	
}
