package com.grupo12.repositories;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.grupo12.entities.Client;
import com.grupo12.entities.Person;
import com.grupo12.entities.User;

@Repository("userRepository")
public interface IUserRepository extends JpaRepository<User, Serializable> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userRoles WHERE u.username = :username")
    public abstract User FindByUsernameAndFetchRolesEagerly(@Param("username") String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    
    Optional<Client> findByUsername(User user);
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);

	public abstract User findByResetToken(String resetToken);

	Optional<User> findByEmailAndUsername(String email, String username);

}
