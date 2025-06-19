package com.grupo12.services;

import java.util.List;
import java.util.Optional;
import com.grupo12.entities.Contact;

public interface IContactService {
    Optional<Contact> getById(Integer id);
    Contact save(Contact contact);
    public Contact insertOrUpdate(Contact contact);
    public List<Contact> getAll();
}
