package com.grupo12.services.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.grupo12.converters.ClientConverter;
import com.grupo12.entities.Client;
import com.grupo12.entities.UserRole;
import com.grupo12.entities.Contact;
import com.grupo12.entities.User;
import com.grupo12.models.ClientDTO;
import com.grupo12.models.UserDTO;
import com.grupo12.repositories.IClientRepository;
import com.grupo12.repositories.IUserRepository;
import com.grupo12.services.IClientService;
import com.grupo12.services.IContactService;
import com.grupo12.services.IUserService;

import jakarta.transaction.Transactional;

@Service("clientService")
public class ClientService implements IClientService {

	@Autowired
	@Qualifier("clientRepository")
	private IClientRepository clientRepository;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("contactService")
	private IContactService contactService;

	@Autowired
	@Qualifier("clientConverter")
	private ClientConverter clientConverter;

	private final BCryptPasswordEncoder pe = new BCryptPasswordEncoder();


	@Override
	public Optional<Client> getById(int idPerson) { 
		return clientRepository.findById(idPerson);
													
	}

	@Override
	public List<Client> getAll() {
		return clientRepository.findAll();
	}
	
	
	//Crea o modifica el cliente
	@Override
	public void insertOrUpdate(ClientDTO clientDTO) {
	    Client client;

	    if (clientDTO.getIdPerson() != null) {
	        // Cliente existente: cargamos y actualizamos
	        client = clientRepository.findById(clientDTO.getIdPerson()).orElseThrow();
	        clientConverter.updateEntityFromDTO(client, clientDTO);
	    } else {
	        // Nuevo cliente: convertimos DTO a entidad
	        client = clientConverter.DTOToEntity(clientDTO);
	        client.setUser(null);  // IMPORTANTE: quitar usuario por ahora
	    }

	    // ✅ Manejar User si viene en el DTO
	    if (clientDTO.getUser() != null) {
	        Optional<User> existingUser = userService.findByEmail(clientDTO.getUser().getEmail());

	        if (existingUser.isEmpty()) {
	            User user = clientConverter.userToEntity(clientDTO.getUser());
	            User savedUser = userService.insertOrUpdate(user);
	            client.setUser(savedUser);
	        } else {
	            client.setUser(existingUser.get());
	        }
	    }

	    // ✅ Guardar el cliente por primera vez (si es nuevo) para obtener ID

	    // 1. Guardar cliente sin usuario para obtener ID si es nuevo
	    if (client.getIdPerson() == null) {
	        client = clientRepository.save(client);
	    }

	    // 2. Manejar User si viene en el DTO
	    if (clientDTO.getUser() != null) {
	        Optional<User> existingUserByEmail = userService.findByEmail(clientDTO.getUser().getEmail());
	        User user;

	        if (existingUserByEmail.isPresent()) {
	            user = existingUserByEmail.get();
	            // Actualizar atributos del usuario existente
	            user.setUsername(clientDTO.getUser().getUsername());
	            user.setPassword(clientDTO.getUser().getPassword());
	            user.setEnabled(clientDTO.getUser().getEnabled());
	            user.setResetToken(clientDTO.getUser().getResetToken());
	            user.setUpdatedAt(LocalDateTime.now());
	        } else {
	        	//Crea un nuevo usuario
	            user = clientConverter.userToEntity(clientDTO.getUser());
	            user.setCreatedAt(LocalDateTime.now());
	    		UserRole defaultRole = new UserRole(user, "ROLE_USER");
	    		user.getUserRoles().add(defaultRole);
	        }

	        // Asociar user y client (cliente ya guardado con id)
	        user.setPerson(client);
	        client.setUser(user);

	        // Guardar usuario (insert o update)
	        user = userService.insertOrUpdate(user);

	        // Guardar cliente actualizado con user asociado
	        client = clientRepository.save(client);
	    }

	    // 3. Generar código cliente si está vacío
	    if (client.getCode() == null || client.getCode().isEmpty()) {
	        String generatedCode = "CLT" + String.format("%05d", client.getIdPerson());
	        client.setCode(generatedCode);
	        clientRepository.save(client);
	    }
	}

	@Override
	public boolean remove(int idPerson) {
		try {
			clientRepository.deleteById(idPerson);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public boolean existsByDni(String dni) {
		return clientRepository.existsByDni(dni);
	}

	@Override
	public boolean isSameClientDni(Integer idPerson, String dni) {
		Optional<Client> existingClient = clientRepository.findByDni(dni);

		if (existingClient.isEmpty()) {
			return false;
		}

		// Compara si el cliente con ese DNI es el mismo que se está editando
		return existingClient.get().getIdPerson().equals(idPerson);
	}


	public Client save(Client client) {
	    // ⚠️ Asegurate de no hacer esto si getUser() es null
	    if (client.getUser() != null) {
	        System.out.println(client.getUser().getPassword());
			client.getUser().setPassword(pe.encode(client.getUser().getPassword()));
			client.getUser().setEnabled(true);
			System.out.println("Saving user: " + client.getUser().getUsername());
			UserRole defaultRole = new UserRole(client.getUser(), "ROLE_USER");
			client.getUser().getUserRoles().add(defaultRole);
	    }
	    
            client = clientRepository.save(client);
	    //  Generar código cliente si está vacío
	    if (client.getCode() == null || client.getCode().isEmpty()) {
	        String generatedCode = "CLT" + String.format("%05d", client.getIdPerson());
	        client.setCode(generatedCode);
	        clientRepository.save(client);
	    }
	    

		return clientRepository.save(client);
	}

	@Override
	public Optional<Client> getByUsername(String username) {
		return clientRepository.findByUserUsername(username);
	}

	@Override
	public Optional<Client> getByIdWithUser(Integer id) {
		return clientRepository.getByIdWithUser(id);
	}

	@Override
	public boolean existsByUser(User user) {
		return clientRepository.findByUser(user).isPresent();
	}
	
	@Override
	public Optional<Client> getByUsername(User user) {
		return clientRepository.findByUserUsername(user.getUsername());
	}

	@Override
	public void delete(Client client) {
		clientRepository.delete(client);
		
	}

}
