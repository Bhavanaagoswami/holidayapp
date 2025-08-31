package com.example.holidayapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayDto {
    private String countryCode;
    private String name;
    private LocalDate date;
    private boolean global;
    private String localName;
    private List<String> types;
}
