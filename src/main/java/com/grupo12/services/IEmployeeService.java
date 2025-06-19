package com.grupo12.services;

import java.util.List;
import java.util.Optional;

import com.grupo12.entities.Employee;
import com.grupo12.models.EmployeeDTO;

public interface IEmployeeService {

	public Optional<Employee> getByCuit(String cuit);

	public List<Employee> getAll();

	public void insertOrUpdate(EmployeeDTO employeeDTO);

	public boolean remove(Integer id);
}
