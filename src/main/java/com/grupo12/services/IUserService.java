package com.grupo12.services;

import java.util.List;
import java.util.Optional;

import com.grupo12.entities.Client;
import com.grupo12.entities.Employee;
import com.grupo12.entities.User;


public interface IUserService {
    User save(User user);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User findByResetToken(String resetToken);
    
    Optional<User> findByUsername(String username);
    
    User insertOrUpdate(User user);

	Optional<User> findByEmail(String email);
    
    Optional<User> findByEmailAndUsername(String email, String username);
    
    public User registerWithDefaultRole(User user);

    public List<Employee> findAllEmployees();
    
}

