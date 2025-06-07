package com.grupo12.security;

import com.grupo12.entities.Employee;
import com.grupo12.entities.JobFunction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class EmployeeDetails implements UserDetails{
	private int idPerson;
	private String username; //generalmente el email o un identificador unico para el login
	private String password;
	private String name;
	private Set<String> roles; 
	//constructor que toma una entidad Employee y la convierte en EmployeeDetails
	public EmployeeDetails(Employee employee) {
		this.idPerson=employee.getIdPerson();
		this.username=employee.getContact().getEmail();
		this.password=employee.getPassword();
		this.name=employee.getName();
		
		//mapea las JobFunctions a roles 
		this.roles=employee.getFunctions()!=null? employee.getFunctions().stream().map(JobFunction::getName)//obtiene el nombre de cada JobFunction
				.collect(Collectors.toSet()):Collections.emptySet();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		//Convierte los nombres de los roles a GrantedAuthority
		//Añade el prefijo "ROLE_" 
		return roles.stream().map(role-> new SimpleGrantedAuthority("ROLE_"+role.toUpperCase())).collect(Collectors.toList());
	}
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public Long getIdPerson() {
		return (long) idPerson;
	}
	public String getName() {
		return name;
	}
}
