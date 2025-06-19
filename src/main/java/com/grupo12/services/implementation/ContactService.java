package com.grupo12.services.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo12.entities.Contact;
import com.grupo12.repositories.IContactRepository;
import com.grupo12.services.IContactService;

import jakarta.transaction.Transactional;

@Service("contactService")
public class ContactService implements IContactService {

    @Autowired
    private IContactRepository contactRepository;

    @Override
    @Transactional
    public Contact insertOrUpdate(Contact contact) {
        // Si tiene ID, es una actualización; si no, es una inserción
        return contactRepository.save(contact);
    }
    
    @Override
    public Optional<Contact> getById(Integer id) {
        return contactRepository.findByIdContact(id);
    }


    @Override
    @Transactional
    public Contact save(Contact contact) {
        return contactRepository.save(contact);
    }


    @Override
    @Transactional
	public List<Contact> getAll() {
		return contactRepository.findAll();
	}

}
