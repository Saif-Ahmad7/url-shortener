package com.url_shortener_sb.service.serviceImpl;

import com.url_shortener_sb.entity.Role;
import com.url_shortener_sb.entity.User;
import com.url_shortener_sb.repository.UserRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private  UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        log.info("UserEmail: {}", usernameOrEmail);  // Corrected the logging syntax
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username or email :- " + usernameOrEmail));
        return new org.springframework.security.core.userdetails.User( user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));

    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
       return roles.stream().map(role ->
           new SimpleGrantedAuthority(role.getName())
       ).collect(Collectors.toList());
    }

//    public static UserDetailsServiceImpl build(User user){
//        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRoles().toString());
//        return new UserDetailsServiceImpl(user.getId(),user.getUsername(),user.getEmail(),user.getPassword(), Collections.singletonList(authority));
//
//    }

//    @Override
//    public Collection<? extends GrantedAuthority> grantedAuthorities(){
//        return authorities;
//    }

//    @Override
//    public String getPassword(){
//        return password;
//    }
//
//    @Override
//    public String getUsername(){
//        return username;
//    }

}

