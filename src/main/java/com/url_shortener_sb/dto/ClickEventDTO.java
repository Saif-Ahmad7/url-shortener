package com.url_shortener_sb.dto;

import com.url_shortener_sb.entity.UrlMapping;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ClickEventDTO {
    private LocalDate clickDate;
    private Long count;
}
