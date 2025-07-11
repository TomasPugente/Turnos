package com.grupo12.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo12.entities.Locality;

@Repository("localityRepository")
public interface ILocalityRepository extends JpaRepository<Locality, Integer> {

    public  Optional<Locality>  findByName(String name);

    public boolean existsByName(String name);
}
