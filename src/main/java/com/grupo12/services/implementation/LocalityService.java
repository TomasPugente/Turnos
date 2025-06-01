package com.grupo12.services.implementation;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.grupo12.entities.Locality;
import com.grupo12.models.LocalityDTO;
import com.grupo12.repositories.ILocalityRepository;
import com.grupo12.services.ILocalityService;

@Service("localityService")
public class LocalityService implements ILocalityService{
	
	private ILocalityRepository localityRepository;

	private ModelMapper modelMapper = new ModelMapper();
	
	public LocalityService(ILocalityRepository localityRepository) {
		this.localityRepository = localityRepository;
	}

	
	@Override
	public Optional<Locality> getById(int idLocality) {
		return localityRepository.findById(idLocality);
	}

	@Override
	public List<Locality> getAll() {
		return localityRepository.findAll();
	}

	@Override  
	public LocalityDTO insertOrUpdate(LocalityDTO localityDTO) {
		Locality locality = localityRepository.save(modelMapper.map(localityDTO, Locality.class));
		return modelMapper.map(locality, LocalityDTO.class);
	}

	@Override
	public boolean remove(int idLocality) {
		try {
			localityRepository.deleteById(idLocality);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public ModelMapper getModelMapper() {
		return modelMapper;
	}

	public void setModelMapper(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

}
