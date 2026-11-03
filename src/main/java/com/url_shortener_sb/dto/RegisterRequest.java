package com.url_shortener_sb.dto;

import lombok.*;

import java.util.Set;

@Data
public class RegisterRequest {

    private String name;
    private String username;
    private String email;
    private String password;
    private Set<String> roles;


}
