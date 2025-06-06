package com.grupo12.services.implementation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.grupo12.entities.UserRole;
import com.grupo12.repositories.IUserRepository;

import lombok.NoArgsConstructor;

@Service("userService")
@NoArgsConstructor
public class UserService implements UserDetailsService {
	
    @Autowired
    @Qualifier("userRepository")
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.grupo12.entities.User user = userRepository.FindByUsernameAndFetchRolesEagerly(username);
        return buildUser(user, buildGrantedAuthorities(user.getUserRoles()));
    }

    private User buildUser(com.grupo12.entities.User user,
            List<GrantedAuthority> grantedAuthorities) {
        return new User(user.getUsername(), user.getPassword(),
                user.getEnabled(), true, true, true,
                grantedAuthorities);
    }

    private List<GrantedAuthority> buildGrantedAuthorities(Set<UserRole> userRoles) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        for (UserRole userRole : userRoles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(userRole.getRole()));
        }
        return new ArrayList<>(grantedAuthorities);
    }
}
