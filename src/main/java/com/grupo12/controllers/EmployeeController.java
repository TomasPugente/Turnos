package com.grupo12.controllers;


import com.grupo12.entities.Employee;
import com.grupo12.services.IEmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final IEmployeeService service;

    public EmployeeController(IEmployeeService service) {
        this.service = service;
    }

    // Endpoint GET: Listar todos los empleados
    @GetMapping
    public List<Employee> findAll() {
        return service.findAll();
    }
}
