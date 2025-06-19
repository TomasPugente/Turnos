package com.grupo12.services;


import java.util.List;
import java.util.Optional;
import com.grupo12.entities.Contact;

public interface IContactService {
    public Contact save(Contact contact);
    public Optional<Contact> getById(Integer id);
    public Contact insertOrUpdate(Contact contact);
    public List<Contact> getAll();
}

