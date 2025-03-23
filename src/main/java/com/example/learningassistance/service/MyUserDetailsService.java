package com.example.learningassistance.service;

import com.example.learningassistance.model.User;
import com.example.learningassistance.model.UserPrincipal;
import com.example.learningassistance.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = repo.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("User does not exist!");
        }

        return new UserPrincipal(user);
    }
}
