/*package com.grupo12.services.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.grupo12.converters.EmployeeConverter;
import com.grupo12.entities.Employee;
import com.grupo12.models.EmployeeDTO;
import com.grupo12.repositories.IEmployeeRepository;
import com.grupo12.services.IEmployeeService;

public class EmployeeService implements IEmployeeService {

	@Autowired
	@Qualifier("employeeRepository")
	private IEmployeeRepository employeeRepository;

	@Autowired
	@Qualifier("employeeConverter")
	private EmployeeConverter employeeConverter;

	@Override
	public Optional<Employee> getById(int idPerson) { // throws RuntimeException, Excepcion para hacer despues
		return employeeRepository.findById(idPerson);// Optional<Client> client = findById(idPerson);
														// .orElseThrow(() -> new RuntimeException("Cliente con id " +
														// idPerson + " no encontrado"));
	}

	@Override
	public List<Employee> getAll() {
		return employeeRepository.findAll();
	}

	@Override
	public EmployeeDTO insertOrUpdate(EmployeeDTO employeeDTO) {
		Employee employee = employeeRepository.save(employeeConverter.DTOToEntity(employeeDTO));
		return employeeConverter.entityToDTO(employee);
	}

	@Override
	public boolean remove(int idPerson) {
		try {
			employeeRepository.deleteById(idPerson);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public List<Employee> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}*/
