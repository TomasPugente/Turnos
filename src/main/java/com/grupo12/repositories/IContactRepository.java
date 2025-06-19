package com.grupo12.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.grupo12.entities.Contact;

@Repository
public interface IContactRepository extends JpaRepository<Contact, Integer> {
}
