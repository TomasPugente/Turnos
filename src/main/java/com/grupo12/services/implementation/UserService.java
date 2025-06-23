package com.grupo12.services.implementation;

import java.time.LocalDateTime;
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
import com.grupo12.models.ClientDTO;
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

    /*@Override
    public User insertOrUpdate(User user) {
        // Validar si username ya existe y no es el mismo usuario
        Optional<User> existingByUsername = userRepository.findByUsername(user.getUsername());
        if (existingByUsername.isPresent() && 
            (user.getId() == null || !existingByUsername.get().getId().equals(user.getId()))) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        // Validar si email ya existe y no es el mismo usuario
        Optional<User> existingByEmail = userRepository.findByEmail(user.getEmail());
        if (existingByEmail.isPresent() && 
            (user.getId() == null || !existingByEmail.get().getId().equals(user.getId()))) {
            throw new RuntimeException("El email ya está en uso");
        }

        // Actualizar usuario existente por ID
        if (user.getId() != null) {
            Optional<User> existingOpt = userRepository.findById(user.getId());
            if (existingOpt.isPresent()) {
                User existing = existingOpt.get();
                existing.setUsername(user.getUsername());
                existing.setEmail(user.getEmail());
                existing.setPassword(user.getPassword()); // ⚠️ asegurate de hashear si corresponde
                existing.setEnabled(user.getEnabled());
                existing.setResetToken(user.getResetToken());
                existing.setUpdatedAt(LocalDateTime.now());
                existing.setPerson(user.getPerson()); // 👈 importante si hay relación con cliente/persona
                return userRepository.save(existing);
            }
        }

        // Si es nuevo o no se encontró por ID
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }*/
    
    
    @Override
    public User insertOrUpdate(User user) {
    	System.out.println(">>> Entró al insertOrUpdate de userservice");
        if (user.getId() != null) {
            User existing = userRepository.findById(user.getId()).orElseThrow();
            existing.setUsername(user.getUsername());
            existing.setEmail(user.getEmail());
            existing.setPassword(pe.encode(user.getPassword()));
            existing.setEnabled(user.getEnabled());
            existing.setResetToken(user.getResetToken());
            existing.setUpdatedAt(LocalDateTime.now());
            existing.setPerson(user.getPerson());
            existing.setEnabled(true);
            return userRepository.save(existing);
        } else {
            Optional<User> existingByEmail = userRepository.findByEmail(user.getEmail());
            if (existingByEmail.isPresent()) {
                throw new RuntimeException("El email ya está registrado");
            }
            user.setCreatedAt(LocalDateTime.now());
            user.setPassword(pe.encode(user.getPassword()));
            
            
            /*if (user.getUserRoles() == null) {
                user.setUserRoles(new HashSet<>());
            }

            // Agregar ROLE_USER si aún no lo tiene
            boolean hasRole = user.getUserRoles().stream()
                .anyMatch(r -> r.getRole().equals("ROLE_USER"));

            if (!hasRole) {
                UserRole defaultRole = new UserRole(user, "ROLE_USER");
                user.getUserRoles().add(defaultRole);
            }*/
            System.out.println(">>> Entró al userRepository.save");
            return userRepository.save(user);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByResetToken(String resetToken) {
        return userRepository.findByResetToken(resetToken);
    }


	@Override
	public Optional<User> findByEmailAndUsername(String email, String username) {
		return userRepository.findByEmailAndUsername(email, username);
	}



}

