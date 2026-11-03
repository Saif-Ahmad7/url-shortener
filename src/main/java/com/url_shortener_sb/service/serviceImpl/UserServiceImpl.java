package com.url_shortener_sb.service.serviceImpl;

import com.url_shortener_sb.entity.User;
import com.url_shortener_sb.repository.UserRepository;
import com.url_shortener_sb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private  UserRepository userRepository;
    @Override
    public User findByUsernames(String name) {
        log.info("Finding user with name: {}", name);

        return userRepository.findByUsernameOrEmail(name,name).orElseThrow(()->
            new UsernameNotFoundException("User not found with username:" + name)
        );
    }
}
