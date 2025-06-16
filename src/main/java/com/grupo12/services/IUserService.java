package com.grupo12.services;

import com.grupo12.entities.User;

public interface IUserService {
    User save(User user);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User findByEmail(String email);
}
