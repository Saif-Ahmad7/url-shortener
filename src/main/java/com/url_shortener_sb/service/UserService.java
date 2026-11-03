package com.url_shortener_sb.service;

import com.url_shortener_sb.entity.User;

public interface UserService {
    User findByUsernames(String name);
}
