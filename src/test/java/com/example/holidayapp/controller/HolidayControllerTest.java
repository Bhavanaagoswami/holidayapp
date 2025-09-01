package com.example.holidayapp.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.holidayapp.model.HolidayDtoFactory;
import com.example.holidayapp.service.HolidayService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(HolidayController.class)
class HolidayControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockitoBean
    public HolidayService holidayService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getHolidayDetailsWithCountryCode() throws Exception {
        when(holidayService.getLast3("AT"))
                .thenReturn(List.of(HolidayDtoFactory.last3HolidaysResponse()));
        // Send request as body to /last3/{countryCode}
        MvcResult result = mockMvc.perform(get("/api/holidays/last3/AT"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].localName").value("Holiday1"))
                .andExpect(jsonPath("$[1].localName").value("Holiday2"))
                .andExpect(jsonPath("$[2].localName").value("Holiday3")).andReturn();
    }

    @Test
    void getCountOfHolidays() throws Exception {
        when(holidayService.getHolidayNotOnWeekend(2025, List.of("DE", "FR")))
                .thenReturn(HolidayDtoFactory.countHolidaysWithOutWeekend());
        // Send request as body to /counts
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/holidays/counts")
                .param("year", "2025")
                .param("countryCodes", "DE,FR")
                .accept(MediaType.APPLICATION_JSON);


        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].countryCode").value("DE"))
                .andExpect(jsonPath("$[0].workingDayHolidays").value("10"))
                .andExpect(jsonPath("$[1].countryCode").value("FR"))
                .andExpect(jsonPath("$[1].workingDayHolidays").value("5"))
                .andReturn();
    }

    @Test
    void getCommonHolidays() throws Exception {

        when(holidayService.getCommonHolidays(2025, "DE", "FR"))
                .thenReturn(HolidayDtoFactory.commonHolidays());
        // Send request as body to /commonHolidays for 2 countries
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/holidays/commonHolidays")
                .param("year", "2025")
                .param("countryCodeFirst", "DE")
                .param("countryCodeSecond", "FR")
                .accept(MediaType.APPLICATION_JSON);


        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].date").value("2025-04-22"))
                .andExpect(jsonPath("$[0].localNameFirstCountry").value("New Year event"))
                .andExpect(jsonPath("$[0].localNameSecondCountry").value("Passe"))
                .andReturn();

    }
}