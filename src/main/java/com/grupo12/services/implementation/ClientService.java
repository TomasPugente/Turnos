package com.grupo12.services.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.grupo12.converters.ClientConverter;
import com.grupo12.entities.Client;
import com.grupo12.entities.Contact;
import com.grupo12.entities.User;
import com.grupo12.models.ClientDTO;
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


	@Override
	public Optional<Client> getById(int idPerson) { // Excepcion para hacer despues
		return clientRepository.findById(idPerson); // Optional<Client> client = findById(idPerson);
													// .orElseThrow(() -> new RuntimeException("Cliente con id " +
													// idPerson + " no encontrado"));
	}

	@Override
	public List<Client> getAll() {
		return clientRepository.findAll();
	}

	@Override
	public void insertOrUpdate(ClientDTO clientDTO) {
	    Client client;

	    if (clientDTO.getIdPerson() != null) {
	        client = clientRepository.findById(clientDTO.getIdPerson()).orElseThrow();
	        clientConverter.updateEntityFromDTO(client, clientDTO);
	    } else {
	        client = clientConverter.DTOToEntity(clientDTO);
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
	    if (client.getIdPerson() == null) {
	        client = clientRepository.save(client); // ahora tiene ID
	    }

	    // ✅ Generar código si está vacío
	    if (client.getCode() == null || client.getCode().isEmpty()) {
	        String generatedCode = "CLT" + String.format("%05d", client.getIdPerson());
	        client.setCode(generatedCode);
	    }

	    clientRepository.save(client); // segunda vez: ahora guarda con el código
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

	@Override
	public Optional<Client> getByUsername(User user) {
		return clientRepository.findByUserUsername(user.getUsername());
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
		return clientRepository.existsByUser(user);
	}

}
