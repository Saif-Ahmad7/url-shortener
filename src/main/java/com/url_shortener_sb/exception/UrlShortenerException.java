package com.url_shortener_sb.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class UrlShortenerException extends RuntimeException{

    private HttpStatus status;
    private String message;

    public UrlShortenerException(HttpStatus status, String message) {
        super(message);  // Call the parent constructor with the message
        this.status = status;
        this.message = message;
    }
}
