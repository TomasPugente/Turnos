package com.grupo12.services;

import com.grupo12.entities.JobFunction;

import java.util.List;
import java.util.Optional;


public interface IJobFunctionService {

    public List<JobFunction> findAll();
    public Optional<JobFunction> findById(Integer id);
}