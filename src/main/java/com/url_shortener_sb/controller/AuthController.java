package com.url_shortener_sb.controller;

import com.url_shortener_sb.config.JwtAuthenticationResponse;
import com.url_shortener_sb.config.JwtUtils;
import com.url_shortener_sb.dto.LoginDto;
import com.url_shortener_sb.dto.RegisterRequest;
import com.url_shortener_sb.entity.Role;
import com.url_shortener_sb.entity.User;
import com.url_shortener_sb.repository.RoleRepository;
import com.url_shortener_sb.repository.UserRepository;
import com.url_shortener_sb.utils.Roles;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private  JwtUtils jwtUtils;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  RoleRepository roleRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;

    @PostMapping("/public/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        log.info("Login attempt for user: {}", registerRequest.getUsername());

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setName(registerRequest.getName());
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

//        Set<Role> roles = new HashSet<>();
//
//        // Ensure registerRequest.getRoles() is a List or Set of role names
//        if (registerRequest.getRoles() != null && !registerRequest.getRoles().isEmpty()) {
//            for (String roleName : registerRequest.getRoles()) {
//                try {
//                    // Convert role name to Enum (Roles is an Enum, so we get its value)
//                    Roles roleEnum = Roles.valueOf(roleName.toUpperCase());
//
//                    // Fetch the role from the repository or create a new one if not found
//                    Role role = roleRepository.findByName(roleEnum)
//                            .orElseThrow(() -> new RuntimeException("Role " + roleEnum + " not found"));
//
//                    roles.add(role); // Add the role to the user's roles set
//                } catch (IllegalArgumentException e) {
//                    // Handle invalid role name if it doesn't exist in the Roles enum
//                    return new ResponseEntity<>("Invalid role: " + roleName, HttpStatus.BAD_REQUEST);
//                }
//            }
//        } else {
//            // Default to USER role if no roles are provided
//            Role defaultRole = roleRepository.findByName(Roles.USER)
//                    .orElseThrow(() -> new RuntimeException("Role USER not found"));
//            roles.add(defaultRole);
//        }
            Role role = roleRepository.findByName("ROLE_USER").orElseThrow(()->
                new RuntimeException("Roles not found")
            );

        // Assign roles to the user
        user.setRoles(Collections.singleton(role));

        // Save the user to the database
        userRepository.save(user);

        return ResponseEntity.ok("User Registered Successfully");
    }

    @PostMapping("/public/login")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@RequestBody LoginDto loginRequest) {
        log.info("Login attempt for user: {}", loginRequest.getUsernameOrEmail());

        Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsernameOrEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
//            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String jwt = jwtUtils.generateToken(authentication);
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));

    }


}
