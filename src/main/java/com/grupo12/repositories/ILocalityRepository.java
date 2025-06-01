package com.grupo12.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.grupo12.entities.Locality;

@Repository
public interface ILocalityRepository extends JpaRepository<Locality, Integer> {
}
