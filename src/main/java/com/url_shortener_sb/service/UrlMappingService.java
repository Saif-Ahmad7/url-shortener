package com.url_shortener_sb.service;

import com.url_shortener_sb.dto.ClickEventDTO;
import com.url_shortener_sb.dto.UrlMappingDTO;
import com.url_shortener_sb.entity.UrlMapping;
import com.url_shortener_sb.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface UrlMappingService {
    UrlMappingDTO createShortUrl(String originalUrl, User user);

    List<UrlMappingDTO> getUrlsByUser(User user);

    List<ClickEventDTO> getClickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end);

    Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate start, LocalDate end);

    UrlMapping getOriginalUrl(String shortUrl);
}
