package com.example.holidayapp.model;

import java.time.LocalDate;

/**
 * This class is Response DTO for all the holiday API.
 */

public record CountryHolidaysDto(LocalDate date, String localNameFirstCountry, String localNameSecondCountry) {}


