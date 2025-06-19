package com.grupo12.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo12.entities.Turn;

@Repository("turnRepository")
public interface ITurnRepository extends JpaRepository<Turn, Serializable> {
    public void deleteByEmployeeIdPersonAndClientIsNull(Integer employeeId);
}
