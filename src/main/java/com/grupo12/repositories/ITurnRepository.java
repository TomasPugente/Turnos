package com.grupo12.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo12.entities.Date;
import com.grupo12.entities.Turn;

public interface ITurnRepository extends JpaRepository<Turn, Serializable>{
    List<Turn> findByDate(Date date);
    
    List<Turn> findByState(String state);
}
