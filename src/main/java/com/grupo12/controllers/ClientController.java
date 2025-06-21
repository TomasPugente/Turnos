package com.grupo12.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.grupo12.entities.User;
import com.grupo12.models.ClientDTO;
import com.grupo12.models.ContactDTO;
import com.grupo12.models.LocalityDTO;
import com.grupo12.models.UserDTO;
import com.grupo12.models.UserDTOForm;
import com.grupo12.services.IClientService;
import com.grupo12.services.ILocalityService;
import com.grupo12.services.IUserService;

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
    @Qualifier("userService")
    private IUserService userService;

    @Autowired
    @Qualifier("clientConverter")
    private ClientConverter clientConverter;

    @GetMapping
    public String listClients(Model model) {
        List<ClientDTO> clientDTOs = clientService.getAll().stream()
                .map(client -> clientConverter.entityToDTO(client))
                .collect(Collectors.toList());

        model.addAttribute("clients", clientDTOs);
        
     // Obtenemos el usuario logueado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> userOpt = userService.findByUsername(username);

        boolean hasClient = false;
        if (userOpt.isPresent()) {
            hasClient = clientService.existsByUser(userOpt.get());
        }

        model.addAttribute("hasClient", hasClient);
       
        return "client/list";
    }

    // Mostrar el formulario para crear o editar
    @GetMapping({ "/form", "/form/{id}" })
    public String showForm(@PathVariable(required = false) Integer id, Model model) {
        if (id != null) {
            Optional<Client> client = clientService.getByIdWithUser(id);
            if (client.isPresent()) {
                model.addAttribute("clientDTO", clientConverter.entityToDTO(client.get())); // editar
            } else {
                return "redirect:/clients";
            }
        } else {
            // ✅ Obtenemos el usuario logueado
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            // ✅ Buscamos al usuario en la base
            Optional<User> userOpt = userService.findByUsername(username);
            if (userOpt.isEmpty()) {
                throw new RuntimeException("Usuario autenticado no encontrado");
            }

            User user = userOpt.get();

            // ✅ Creamos el UserDTO con los datos del usuario logueado
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setEnabled(true);

            // ✅ Creamos el ClientDTO con ese UserDTO
            ClientDTO clientDTO = new ClientDTO();
            clientDTO.setUser(userDTO);
            
            // ✅ Agregar contacto con localidad vacía
            ContactDTO contact = new ContactDTO();
            // Opcional: no hagas set de campos que no tienen valor todavía
            contact.setLocality(new LocalityDTO());
            clientDTO.setContact(contact);
            contact.setLocality(new LocalityDTO());
            
           

            

            model.addAttribute("clientDTO", clientDTO); // para nuevo
        }

        model.addAttribute("localities", localityService.getAll());
        return "client/form";
    }

    @PostMapping("/save")
    public String saveClient(@Valid @ModelAttribute("clientDTO") ClientDTO clientDTO, BindingResult bindingResult,
            Model model) {
        if (clientDTO.getIdPerson() == null
                || !clientService.isSameClientDni(clientDTO.getIdPerson(), clientDTO.getDni())) {
            if (clientService.existsByDni(clientDTO.getDni())) {
                bindingResult.rejectValue("dni", "duplicateDni", "El DNI ya está registrado");
            }
        }

        if (bindingResult.hasErrors()) {
        	bindingResult.getAllErrors().forEach(err -> System.out.println(err.toString()));
            model.addAttribute("localities", localityService.getAll());
            return "client/form";
        }
        // Paso 1: Buscamos el usuario ya existente
        Optional<User> userOpt = userService.findByUsername(clientDTO.getUser().getUsername());
        if (userOpt.isEmpty()) {
            model.addAttribute("error", "No se encontró el usuario asociado");
            model.addAttribute("localities", localityService.getAll());
            return "client/form";
        }

        User user = userOpt.get();

        // Paso 2: Convertimos el DTO a entidad Client
        Client client = clientConverter.DTOToEntity(clientDTO);

        // Paso 3: Establecemos la relación de ambos lados
        client.setUser(user);
        user.setPerson(client); // ⚠️ Esto es clave para que se guarde el idPerson correctamente
        
        
        clientService.insertOrUpdate(clientDTO);
        return "redirect:/clients";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
      clientService.remove(id); 
      return "redirect:/clients";
    }
     

}
