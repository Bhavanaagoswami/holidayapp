package com.example.holidayapp.controller;

import com.example.holidayapp.model.CountryHolidaysDto;
import com.example.holidayapp.model.HolidayCountPerCountryDto;
import com.example.holidayapp.model.HolidayDto;
import com.example.holidayapp.service.HolidayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Holiday Controller for Rest API :
 * Find Holidays with country code
 * Find Holidays With date and country code
 *
 */
@RestController
@RequestMapping("/api/holidays")
@Slf4j
public class HolidayController {
    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    /**
     * This method responsible to return holidays with country code and date
     * @param countryCode country code
     * @return List of HolidayDto
     */
    @Operation(
            summary = "Create Get API for Holiday Details",
            description = "Get API for last 3 celebrated holidays for given country code."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            )
    }
    )
    @GetMapping("/last3/{countryCode}")
    public ResponseEntity<List<HolidayDto>> getLast3HolidaysWithCountryCode(@PathVariable String countryCode) {
        return new ResponseEntity<>(holidayService.getLast3(countryCode),
                HttpStatus.OK);
    }


    /**
     * This method responsible to return holidays workingDayHolidays with country code using given country code and date.
     * @param year year
     * @param countryCodes country code
     * @return List of HolidayDto
     */
    @Operation(
            summary = "Create Get API to get the count of holiday",
            description = "Create Get API to get the count of holiday comes in weekdays for given year and country code"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            )
    })
    @GetMapping("/counts")
    public ResponseEntity<List<HolidayCountPerCountryDto>> getCountOfHolidays(@RequestParam int year,
                                                                              @NonNull @RequestParam List<String> countryCodes) {
        return new ResponseEntity<>(holidayService.getHolidayNotOnWeekend(year, countryCodes),
        HttpStatus.OK);
    }

    /**
     * This method return Common holiday list for 2 given country code and year.
     * @param year year
     * @param countryCodeFirst first country code
     * @param countryCodeSecond second country code
     * @return CountryHolidaysDto list of Holidays
     */
    @Operation(
            summary = "Create Get API to get common holidays between 2 country code",
            description = "This method return Common holiday list for 2 given country code and year."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            )
    })
    @GetMapping("/commonHolidays")
    public ResponseEntity<List<CountryHolidaysDto>> getCommonHolidays(@RequestParam int year,
                                                                      @Valid @RequestParam String countryCodeFirst,
                                                                      @Valid @RequestParam String countryCodeSecond) {
        return new ResponseEntity<>(holidayService.getCommonHolidays(year,countryCodeFirst,countryCodeSecond),
        HttpStatus.OK);
    }


}
