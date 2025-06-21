package com.grupo12.repositories;

import com.grupo12.entities.JobFunction;
import com.grupo12.entities.Locality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("jobFunctionRepository")
public interface IJobFunctionRepository  extends JpaRepository<JobFunction, Integer> {
}