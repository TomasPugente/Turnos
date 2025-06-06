package com.grupo12.security;

import com.grupo12.entities.Employee;
import com.grupo12.repositories.IEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeUserDetailsService implements UserDetailsService{
	@Autowired
	private IEmployeeRepository employeeRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Employee employee=employeeRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("Empleado no encontrado con email: "+email));
		return new EmployeeDetails(employee);
	}
	
}
