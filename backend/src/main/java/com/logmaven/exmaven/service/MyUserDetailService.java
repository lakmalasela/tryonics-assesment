package com.logmaven.exmaven.service;

import com.logmaven.exmaven.entity.Role;
import com.logmaven.exmaven.entity.User;
import com.logmaven.exmaven.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MyUserDetailService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userDetails = userRepository.findUserByUsername(username);
        if(userDetails == null){
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        System.out.println(username);
        System.out.println(userDetails);

        Set<GrantedAuthority> roleset = new HashSet<>();
        for(Role role: userDetails.getRoles()){
            roleset.add(new SimpleGrantedAuthority(role.getName()));
        }
        List<GrantedAuthority> authorities = new ArrayList<>(roleset);
        System.out.println("AUTH "+authorities);
        return new org.springframework.security.core.userdetails.User(userDetails.getUsername(),userDetails.getPassword(),true,true,true,true,authorities);
    }
}
