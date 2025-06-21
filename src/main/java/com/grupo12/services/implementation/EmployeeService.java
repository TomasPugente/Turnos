package com.grupo12.services.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

import com.grupo12.converters.EmployeeConverter;
import com.grupo12.entities.Employee;
import com.grupo12.models.EmployeeDTO;
import com.grupo12.repositories.IEmployeeRepository;
import com.grupo12.services.IEmployeeService;

@Service
public class EmployeeService implements IEmployeeService{

	private final IEmployeeRepository repository;
	@Autowired
	private IEmployeeRepository employeeRepository;

	@Autowired
	private EmployeeConverter employeeConverter;

    public EmployeeService(IEmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Employee> findAll() {
        return repository.findAll();
    }

	@Override
	public Optional<Employee> getById(int idPerson) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<EmployeeDTO> getAll() {
		// TODO Auto-generated method stub
		return employeeRepository.findAll()
	            .stream()
	            .map(employeeConverter::entityToDTO)
	            .collect(Collectors.toList());
	}

	@Override
	public EmployeeDTO insertOrUpdate(EmployeeDTO employeeDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(int idPerson) {
		// TODO Auto-generated method stub
		return false;
	}
}
