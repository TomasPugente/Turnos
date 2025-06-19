package com.grupo12.repositories;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.grupo12.entities.Employee;

@Repository("employeeRepository")
public interface IEmployeeRepository extends JpaRepository<Employee, Serializable> {
    public Optional<Employee> findByCuit(String cuit);

    public void deleteByCuit(String cuit);
}
