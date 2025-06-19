package com.grupo12.services;

import java.util.List;
import java.util.Optional;

import com.grupo12.entities.Client;
import com.grupo12.entities.User;
import com.grupo12.models.ClientDTO;

public interface IClientService {

	public Optional<Client> getById(int idPerson);

	public List<Client> getAll();

	public void insertOrUpdate(ClientDTO clientDTO);

	public boolean remove(int idPerson);

	public boolean existsByDni(String dni);
	
	public boolean existsByUser(User user);

	public boolean isSameClientDni(Integer idPerson, String dni);

	Optional<Client> getByUsername(String username);

	public Optional<Client> getByUsername(User user);
	
	Optional<Client> getByIdWithUser(Integer id);
}
