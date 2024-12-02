package com.example.happenhere.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterDTO {
    private String city;
    private String country;
    private String[] categories;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
}
