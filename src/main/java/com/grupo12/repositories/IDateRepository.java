package com.grupo12.repositories;

import java.io.Serializable;

import com.grupo12.entities.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("dateRepository")
public interface IDateRepository extends JpaRepository<Date, Serializable> {

}
