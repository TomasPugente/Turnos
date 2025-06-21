package com.grupo12.services.implementation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.grupo12.entities.Client;
import com.grupo12.entities.User;
import com.grupo12.entities.UserRole;
import com.grupo12.repositories.IUserRepository;
import com.grupo12.services.IUserService;

@Service("userService")
public class UserService implements UserDetailsService, IUserService {

    @Autowired
    @Qualifier("userRepository")
    private IUserRepository userRepository;

    private final BCryptPasswordEncoder pe = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.FindByUsernameAndFetchRolesEagerly(username);
        
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con nombre de usuario: " + username);
        }
       
        return buildUser(user, buildGrantedAuthorities(user.getUserRoles()));     
    }

    private org.springframework.security.core.userdetails.User buildUser(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getEnabled(),
                true, true, true,
                authorities);
    }

    private List<GrantedAuthority> buildGrantedAuthorities(Set<UserRole> roles) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (UserRole role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return new ArrayList<>(authorities);
    }

    @Override
    public User save(User user) {
        user.setPassword(pe.encode(user.getPassword()));
        user.setEnabled(true);
        user.setEmail(user.getEmail());
        
        if (user.getPerson() == null) {
            throw new IllegalArgumentException("User must be associated with a Person (e.g., Client)");
        }
        
        if (user.getUserRoles() == null) {
            user.setUserRoles(new HashSet<>());
        }

        
        // Agregar ROLE_USER si aún no lo tiene
        boolean hasRole = user.getUserRoles().stream()
            .anyMatch(r -> r.getRole().equals("ROLE_USER"));

        if (!hasRole) {
            UserRole defaultRole = new UserRole(user, "ROLE_USER");
            user.getUserRoles().add(defaultRole);
        }
        
        //user.setPerson(user.getPerson());
        return userRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /*@Override
    public Optional<Client> getByUser(User user) {
        return userRepository.findByUser(user);
    }*/

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User insertOrUpdate(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByResetToken(String resetToken) {
        return userRepository.findByResetToken(resetToken);
    }
}


