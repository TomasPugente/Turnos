package com.grupo12.services.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.grupo12.converters.ClientConverter;
import com.grupo12.entities.Client;
import com.grupo12.models.ClientDTO;
import com.grupo12.repositories.IClientRepository;
import com.grupo12.services.IClientService;

import jakarta.transaction.Transactional;

@Service("clientService")
public class ClientService implements IClientService {

	
	@Autowired
	@Qualifier("clientRepository")
	private IClientRepository clientRepository;

	@Autowired
	@Qualifier("clientConverter")
	private ClientConverter clientConverter;
	
	@Override
	public Optional<Client> getById(int idPerson) { //Excepcion para hacer despues
		return clientRepository.findById(idPerson); // Optional<Client> client = findById(idPerson);
		                                            //.orElseThrow(() -> new RuntimeException("Cliente con id " + idPerson + " no encontrado"));
	}

	@Override
	public List<Client> getAll() {
		return clientRepository.findAll();
	}

	/*@Override
	public void insertOrUpdate(ClientDTO clientDTO) {
	    if (clientDTO.getIdPerson() != null) {
	        // actualizar
	        Client existing = clientRepository.findById(clientDTO.getIdPerson()).orElseThrow();
	        // actualizar campos manualmente...
	        clientRepository.save(existing);
	    } else {
	        // nuevo
	        Client client = clientConverter.DTOToEntity(clientDTO);
	        clientRepository.save(client);
	    }
	}*/
	
	@Override
	@Transactional
	public void insertOrUpdate(ClientDTO clientDTO) {
	    Client client;

	    if (clientDTO.getIdPerson() != null) {
	        // Buscar y actualizar el cliente existente
	        client = clientRepository.findById(clientDTO.getIdPerson()).orElseThrow();
	        clientConverter.updateEntityFromDTO(client, clientDTO);
	    } else {
	        // Crear un nuevo cliente desde el DTO
	        client = clientConverter.DTOToEntity(clientDTO);
	        client = clientRepository.save(client); // primer save para obtener ID
	    }

	    // Generar código si está vacío
	    if (client.getCode() == null || client.getCode().isEmpty()) {
	        String generatedCode = "CLT" + String.format("%05d", client.getIdPerson());
	        client.setCode(generatedCode);
	        clientRepository.save(client); // actualizar con el código generado
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


}
