package com.grupo12.services;

import java.util.Optional;

import com.grupo12.entities.Client;
import com.grupo12.entities.User;

public interface IUserService {
    User save(User user);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    
    Optional<Client> getByUser(User user);
    
    Optional<User> findByUsername(String username);
    
    User insertOrUpdate(User user);

	Optional<User> findByEmail(String email);


    //User findByEmail(String email);

    User findByResetToken(String resetToken);

}

