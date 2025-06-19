package com.grupo12.services;
import java.util.List;
import java.util.Optional;
import com.grupo12.entities.Contact;

public interface IContactService {

	Contact insertOrUpdate(Contact contact);
    Contact save(Contact contact);
	Optional<Contact> getById(Integer id);
    public List<Contact> getAll();
}

