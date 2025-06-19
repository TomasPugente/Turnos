package com.grupo12.services.implementation;

import java.util.List;
import java.util.Optional;

import com.grupo12.repositories.ITurnRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.grupo12.converters.EmployeeConverter;
import com.grupo12.entities.Employee;
import com.grupo12.models.EmployeeDTO;
import com.grupo12.repositories.IEmployeeRepository;
import com.grupo12.services.IEmployeeService;
import org.springframework.stereotype.Service;

@Service("employeeService")
public class EmployeeService implements IEmployeeService {

	@Autowired
	@Qualifier("employeeRepository")
	private IEmployeeRepository employeeRepository;

	@Autowired
	@Qualifier("employeeConverter")
	private EmployeeConverter employeeConverter;

	@Autowired
	@Qualifier("turnRepository")
	private ITurnRepository turnRepository;

	@Override
	public Optional<Employee> getByCuit(String cuit) {
		return employeeRepository.findByCuit(cuit);
	}

	@Override
	public List<Employee> getAll() {
		return employeeRepository.findAll();
	}

	@Override
	public void insertOrUpdate(EmployeeDTO employeeDTO) {
		employeeRepository.save(employeeConverter.DTOToEntity(employeeDTO));
	}

	@Transactional
	@Override
	public boolean remove(Integer id) {
		try {
			turnRepository.deleteByEmployeeIdPersonAndClientIsNull(id);
			employeeRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			return false;
		}

	}
}
