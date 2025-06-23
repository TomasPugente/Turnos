package com.grupo12.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.grupo12.converters.ClientConverter;
import com.grupo12.entities.Client;
import com.grupo12.entities.User;
import com.grupo12.models.ClientDTO;
import com.grupo12.models.ContactDTO;
import com.grupo12.models.LocalityDTO;
import com.grupo12.models.UserDTO;
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

    //Muestra los datos del usuario registrado y logueado 
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

    // CU013 - Agregar/Modificar/Eliminar Cliente
    //Agrega el cliente si lo habiamos eliminado anteriormente
    // Para NUEVO cliente (sin ID)
    @GetMapping("/form")
    public String showNewForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) throw new RuntimeException("Usuario autenticado no encontrado");

        User user = userOpt.get();
        // ✅ Creamos el ClientDTO con ese UserDTO
        ClientDTO clientDTO = new ClientDTO();

     
        if (clientDTO.getUser() == null) {
            clientDTO.setUser(new UserDTO());
        }
        clientDTO.getUser().setId(user.getId()); // O lo que uses para vincular
        ContactDTO contact = new ContactDTO();
        clientDTO.setContact(contact);
        contact.setLocality(new LocalityDTO());

        model.addAttribute("clientDTO", clientDTO);
        model.addAttribute("localities", localityService.getAll());
        return "client/form";
    }

    // CU006 - Modificar mis datos
    //Modifica los datos del cliente
    // Para EDITAR cliente (con ID)
    @GetMapping("/form/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        Optional<Client> client = clientService.getByIdWithUser(id);
        if (client.isEmpty()) return "redirect:/clients";

        ClientDTO clientDTO = clientConverter.entityToDTO(client.get());
        model.addAttribute("clientDTO", clientDTO);
        model.addAttribute("localities", localityService.getAll());
        return "client/form";
    }


    //Cuando clickeamos en guardar cliente va a guardarlo en la base de datos
    @PostMapping("/save")
    public String saveClient(@Valid @ModelAttribute("clientDTO") ClientDTO clientDTO, BindingResult bindingResult,
            Model model) {
    	
    	System.out.println("entro a saveClient");
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
        
        System.out.println("Por entrar a insertOrUdateClient");
        clientService.insertOrUpdate(clientDTO);
        return "redirect:/clients";
    }
   
    
    //Elimina el cliente pero no el usuario
    // Para ELIMINAR cliente (con ID)
    @GetMapping("/delete/{id}")
    public String remove(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Client> clientOpt = clientService.getById(id);
            if (clientOpt.isPresent()) {
                Client client = clientOpt.get();

                User user = client.getUser();
                if (user != null) {
                    user.setPerson(null); // desvincular User
                }

                clientService.delete(client); // gracias a Cascade y orphanRemoval se borran los turns también

                redirectAttributes.addFlashAttribute("successMessage", "Cliente y sus turnos eliminados correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Cliente no encontrado.");
            }
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error al intentar eliminar el cliente.");
            ex.printStackTrace();
        }
        return "redirect:/clients";
    }

     

}
