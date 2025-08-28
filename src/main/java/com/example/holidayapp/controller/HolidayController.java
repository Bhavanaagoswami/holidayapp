package com.example.holidayapp.controller;

import com.example.holidayapp.model.PublicHolidayDetailsRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Holiday Controller for Rest API :
 * Find Holidays with country code
 * Find Holidays With date and country code
 *
 */
@RestController
@RequestMapping("api/holidays")
public class HolidayController {

    /**
     * This method responsible to return holidays with country code and date
     * @param countrycode
     * @return
     */
    @RequestMapping("/{dateAndCountry}")
    public ResponseEntity<PublicHolidayDetailsRequest> getHolidayDetailsWithCountryCode(@PathVariable String countrycode) {
        //this.countrycode = countrycode;
        return null;
    }
}
