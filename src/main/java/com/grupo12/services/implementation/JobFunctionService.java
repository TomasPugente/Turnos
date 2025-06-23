package com.grupo12.services.implementation;

import com.grupo12.entities.JobFunction;
import com.grupo12.repositories.IEmployeeRepository;
import com.grupo12.repositories.IJobFunctionRepository;
import com.grupo12.services.IJobFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("jobFunctionService")
public class JobFunctionService  implements IJobFunctionService {

    @Autowired
    @Qualifier("jobFunctionRepository")
    private IJobFunctionRepository jobFunctionRepository;

    @Override
    public List<JobFunction> findAll() {
        return jobFunctionRepository.findAll();
    }

    @Override
    public Optional<JobFunction> findById(Integer id) {
        return jobFunctionRepository.findById(id);
    }
}