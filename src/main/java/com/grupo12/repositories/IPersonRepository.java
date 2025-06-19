package com.grupo12.repositories;

import com.grupo12.entities.Locality;
import com.grupo12.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("personRepository")
public interface IPersonRepository extends JpaRepository<Person, Integer> {
}
