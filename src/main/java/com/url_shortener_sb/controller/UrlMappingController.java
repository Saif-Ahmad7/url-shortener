package com.url_shortener_sb.controller;

import com.url_shortener_sb.dto.ClickEventDTO;
import com.url_shortener_sb.dto.UrlMappingDTO;
import com.url_shortener_sb.entity.User;
import com.url_shortener_sb.service.UrlMappingService;
import com.url_shortener_sb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/urls")
@Slf4j
public class UrlMappingController {
    @Autowired
    private  UrlMappingService urlMappingService;
    @Autowired
    private  UserService userService;

    // {"originalUrl" : "https://example.com"}
    @PostMapping("/shorten")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<UrlMappingDTO> createShortUrl(@RequestBody Map<String,String> request, Principal principal){
        String originalUrl = request.get("originalUrl");
        log.info("Principal......:{}" ,principal.getName());
     //   log.info("UserDetailsJwt ......: {},{}",userDetails.getUsername(),userDetails.getAuthorities(),userDetails.getPassword());
        log.info("Principal: {}", principal);  // This will show the complete Principal object

        User user = userService.findByUsernames(principal.getName());
        log.info("User : {}",user);
        UrlMappingDTO urlMappingDTO = urlMappingService.createShortUrl(originalUrl,user);
        return ResponseEntity.ok(urlMappingDTO);
    }

    @GetMapping("/myurls")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<UrlMappingDTO>> getUserUrls(Principal principal){
        User user = userService.findByUsernames(principal.getName());
        log.info("User : {}",user);
        List<UrlMappingDTO> urls = urlMappingService.getUrlsByUser(user);
        return ResponseEntity.ok(urls);
    }

    @GetMapping("/analytics/{shortUrl}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<ClickEventDTO>> getUrlsAnalytics(@PathVariable String shortUrl, @RequestParam("startDate")String startDate, @RequestParam("endDate")String endDate){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startDate,formatter);
        LocalDateTime end = LocalDateTime.parse(endDate,formatter);
        List<ClickEventDTO> clickEventDTOS =  urlMappingService.getClickEventsByDate(shortUrl,start,end);
        return  ResponseEntity.ok(clickEventDTOS);
    }

    @GetMapping("/totalClicks")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Map<LocalDate, Long>> getTotalClicksByDate(@RequestParam("startDate")String startDate, @RequestParam("endDate")String endDate, Principal principal){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        User user = userService.findByUsernames(principal.getName());
        LocalDate start = LocalDate.parse(startDate,formatter);
        LocalDate end = LocalDate.parse(endDate,formatter);
        Map<LocalDate,Long> totalClicksByUserAndDate =  urlMappingService.getTotalClicksByUserAndDate(user,start,end);
        return  ResponseEntity.ok(totalClicksByUserAndDate);
    }




}
