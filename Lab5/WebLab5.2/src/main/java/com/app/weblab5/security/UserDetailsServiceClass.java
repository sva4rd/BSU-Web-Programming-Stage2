package com.app.weblab5.security;

import com.app.weblab5.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.app.weblab5.model.User;

@Service
public class UserDetailsServiceClass implements UserDetailsService {

    private final UserRepository userRepo;

    public UserDetailsServiceClass(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        System.out.println(user);
        if (user != null)
            return user;
        throw new UsernameNotFoundException("User '" + username + "' not found");
    }
}
