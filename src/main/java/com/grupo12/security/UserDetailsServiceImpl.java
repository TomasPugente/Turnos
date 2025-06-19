package com.grupo12.security;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.grupo12.entities.User;
import com.grupo12.repositories.IUserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .FindByUsernameAndFetchRolesEagerly(username); // asegúrate de que esta consulta haga un JOIN FETCH
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        Set<GrantedAuthority> authorities = user.getUserRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getEnabled(),
                true,   // accountNonExpired
                true,   // credentialsNonExpired
                true,   // accountNonLocked
                authorities
        );
    }
}

