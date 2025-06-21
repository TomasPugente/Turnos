package com.grupo12.services;

import java.util.List;
import java.util.Optional;

import com.grupo12.entities.Employee;
import com.grupo12.models.EmployeeDTO;

public interface IEmployeeService {

	public Optional<Employee> getById(int idPerson);

	public List<EmployeeDTO> getAll();
	
	public EmployeeDTO insertOrUpdate(EmployeeDTO employeeDTO);
	
	public boolean remove(int idPerson);
	
	List<Employee> findAll();
	
}
