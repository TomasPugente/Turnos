package com.grupo12.controllers;

import com.grupo12.converters.PersonaConverter;
import com.grupo12.converters.TurnConverter;
import com.grupo12.entities.Client;
import com.grupo12.entities.Person;
import com.grupo12.entities.TurnStatus;
import com.grupo12.exceptions.TurnNotFoundException;
import com.grupo12.helpers.ViewRouteHelper;
import com.grupo12.entities.Turn;
import com.grupo12.models.ClientDTO;
import com.grupo12.models.PersonDTO;
import com.grupo12.models.TurnDTO;
import com.grupo12.models.TurnUpdateDTO;
import com.grupo12.services.implementation.ClientService;
import com.grupo12.services.implementation.PersonService;
import com.grupo12.services.implementation.TurnService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;
    private final PersonaConverter personConverter;

    public PersonController(PersonService personService, PersonaConverter personConverter) {
        this.personService = personService;
        this.personConverter = personConverter;
    }


    @GetMapping
    public String listTurns(Model model) {
        List<PersonDTO> turns = personService.getAll().stream()
                .map(personConverter::entityToDTO)
                .collect(Collectors.toList());
        model.addAttribute("persons", turns);
        return "person/list"; // este HTML
    }

    // Mostrar el formulario para crear o editar
    @GetMapping({ "/form", "/form/{id}" })
    public String showForm(@PathVariable(required = false) Integer id, Model model) {
        if (id != null) {
            Optional<Person> person = personService.findById(id);
            if (person.isPresent()) {

                model.addAttribute("personDTO", personConverter.entityToDTO(person.get())); // para editar
                model.addAttribute("statusOptions", TurnStatus.values());
            } else {
                return "redirect:/persons";
            }

        } else {
            model.addAttribute("personDTO", new PersonDTO()); // para nuevo
        }
        return "person/form"; // templates/client/form.html
    }

    @PostMapping("/save")
    public String saveClient(@Valid @ModelAttribute("personDTO") PersonDTO personDTO, BindingResult bindingResult,
                             Model model) {

        personService.insertOrUpdate(personDTO);
        return "redirect:/persons";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        personService.remove(id);

        return "redirect:/persons";
    }
}
