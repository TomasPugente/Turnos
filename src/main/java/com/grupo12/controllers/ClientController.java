package com.grupo12.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import com.grupo12.converters.ClientConverter;
import com.grupo12.entities.Client;
import com.grupo12.models.ClientDTO;
import com.grupo12.services.IClientService;
import com.grupo12.services.ILocalityService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    @Qualifier("clientService")
    private IClientService clientService;
    
    @Autowired
    @Qualifier("localityService")
    private ILocalityService localityService;
    
	@Autowired
	@Qualifier("clientConverter")
	private ClientConverter clientConverter;

    @GetMapping
    public String listClients(Model model) {
        List<ClientDTO> clientDTOs = clientService.getAll().stream()
                .map(client -> clientConverter.entityToDTO(client))
                .collect(Collectors.toList());

            model.addAttribute("clients", clientDTOs);
            return "client/list";
    }

    /*@GetMapping("/new")
    public String newClientForm(Model model) {
        model.addAttribute("client", new ClientDTO());
        model.addAttribute("localities", localityService.getAll());
        return "client/new";
    }*/
    
    /*@GetMapping("/form")
    public String showClientForm(@RequestParam(name = "id", required = false) Integer id, Model model) {
        if (id != null) {
            Optional<Client> client = clientService.getById(id);
            if (client.isPresent()) {
                model.addAttribute("client", clientConverter.entityToDTO(client.get()));
            } else {
                return "redirect:/clients";
            }
        } else {
            model.addAttribute("client", new ClientDTO());
        }

        model.addAttribute("localities", localityService.getAll());
        return "client/form";
    }*/
    

    // Mostrar el formulario para crear o editar
    @GetMapping({"/form", "/form/{id}"})
    public String showForm(@PathVariable(required = false) Integer id, Model model) {
        if (id != null) {
        	Optional<Client> client = clientService.getById(id);
        	if (client.isPresent()) {
        	    model.addAttribute("clientDTO", clientConverter.entityToDTO(client.get())); // para editar
        	} else {
        	    return "redirect:/clients";
        	}
        	
        } else {
            model.addAttribute("clientDTO", new ClientDTO()); // para nuevo
        }
        model.addAttribute("localities", localityService.getAll());
        return "client/form"; // templates/client/form.html
    }

    @PostMapping("/save")
    public String saveClient(@Valid  @ModelAttribute("clientDTO") ClientDTO clientDTO,  BindingResult bindingResult, Model model) {
    	if (clientDTO.getIdPerson() == null || !clientService.isSameClientDni(clientDTO.getIdPerson(), clientDTO.getDni())) {
	        if (clientService.existsByDni(clientDTO.getDni())) {
	            bindingResult.rejectValue("dni", null, "El DNI ya está registrado");
	        }
    	}
        
    	if (bindingResult.hasErrors()) {
    		model.addAttribute("localities", localityService.getAll());
    		return "client/form";
    	}
        clientService.insertOrUpdate(clientDTO);
        return "redirect:/clients";
    }
    
    /*@GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        Optional<Client> client = clientService.getById(id);
        if (client.isPresent()) {
            ClientDTO dto = clientConverter.entityToDTO(client.get());
            model.addAttribute("client", dto);
            return "client/edit";
        }
        return "redirect:/clients";
    }*/

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        clientService.remove(id);
        return "redirect:/clients";
    }

}

