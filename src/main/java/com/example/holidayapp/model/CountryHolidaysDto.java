package com.example.holidayapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

/**
 * This class is Response DTO for all the holiday API.
 */

@Data
@AllArgsConstructor
public class CountryHolidaysDto {
    private LocalDate date;
    private String localNameCountryFirst;
    private String localNameCountrySecond;
}
