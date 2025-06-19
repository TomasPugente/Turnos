package com.grupo12.controllers;

import com.grupo12.converters.EmployeeConverter;
import com.grupo12.entities.Client;
import com.grupo12.entities.JobFunction;
import com.grupo12.entities.TurnStatus;
import com.grupo12.helpers.ViewRouteHelper;
import com.grupo12.entities.Employee;
import com.grupo12.models.ClientDTO;
import com.grupo12.models.EmployeeDTO;
//import com.grupo12.models.EmployeeUpdateDTO;
import com.grupo12.services.implementation.ClientService;
import com.grupo12.services.implementation.EmployeeService;
import com.grupo12.services.implementation.JobFunctionService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final JobFunctionService jobFunctionService;
    private final EmployeeConverter employeeConverter;

    public EmployeeController(EmployeeService employeeService, EmployeeConverter employeeConverter,  JobFunctionService jobFunctionService) {
        this.employeeService = employeeService;
        this.employeeConverter = employeeConverter;
        this.jobFunctionService = jobFunctionService;
    }


    @GetMapping
    public String listEmployees(Model model) {
        List<EmployeeDTO> turns = employeeService.getAll().stream()
                .map(employeeConverter::entityToDTO)
                .collect(Collectors.toList());
        model.addAttribute("employees", turns);
        return "employee/list";
    }

    // Mostrar el formulario para crear o editar
    @GetMapping({ "/form", "/form/{cuit}" })
    public String showForm(@PathVariable(required = false) String cuit, Model model) {
        if (cuit != null) {
            Optional<Employee> employee = employeeService.getByCuit(cuit);
            if (employee.isPresent()) {
                model.addAttribute("employeeDTO", employeeConverter.entityToDTO(employee.get())); // para editar
                Set<Integer> assignedFunctionIds = employee.get().getFunctions()
                        .stream()
                        .map(JobFunction::getIdJobFunction)
                        .collect(Collectors.toSet());
                System.out.println(assignedFunctionIds);
                model.addAttribute("assignedFunctionIds", assignedFunctionIds);
            } else {
                return "redirect:/employees";
            }

        } else {
            model.addAttribute("employeeDTO", new EmployeeDTO()); // para nuevo
            Set<Integer> set  = new HashSet<>();
            model.addAttribute("assignedFunctionIds", set);
        }

        List<JobFunction> jobFunctions = jobFunctionService.findAll();
        model.addAttribute("allFunctions", jobFunctions);

        return "employee/form";
    }

    @PostMapping("/save")
    public String saveClient(@Valid @ModelAttribute("employeeDTO") EmployeeDTO employeeDTO,
                             @RequestParam(name = "functionIds", required = false) List<Integer> functionIds) {

        System.out.println(functionIds);
        Set<JobFunction> functions = functionIds.stream()
                .map(id -> jobFunctionService.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        employeeDTO.setFunctions(functions);
        employeeService.insertOrUpdate(employeeDTO);
        return "redirect:/employees";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        if (!employeeService.remove(id)) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el empleado con ID " + id);
        } else {
            redirectAttributes.addFlashAttribute("success", "Empleado eliminado correctamente.");
        }
        return "redirect:/employees";
    }

}
