package com.grupo12.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.grupo12.entities.User;

@Repository("userRepository")
public interface IUserRepository extends JpaRepository<User, Serializable> {
	
	@Query("Select u from User u where u.username = (:username)")
	public abstract User FindByUsernameAndFetchRolesEagerly(@Param("username") String username);

}
