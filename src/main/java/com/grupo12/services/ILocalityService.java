package com.grupo12.services;

import java.util.List;
import java.util.Optional;

import com.grupo12.entities.Locality;
import com.grupo12.models.LocalityDTO;

public interface ILocalityService {

	public List<Locality> getAll();

	public LocalityDTO insertOrUpdate(LocalityDTO localityDTO);

	public boolean remove(int idLocality);

	public Optional<Locality> getById(Integer idLocality);

	public Optional<Locality> findById(Integer idLocality);

}
