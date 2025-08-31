package com.example.holidayapp.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.holidayapp.model.HolidayDto;
import com.example.holidayapp.model.HolidayDtoFactory;
import com.example.holidayapp.service.HolidayService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;



@WebMvcTest(HolidayControllerTest.class)
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
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/holidays/last3/AT");

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void getCountOfHolidays() {
        when(holidayService.getHolidayNotOnWeekend(2025, List.of("DE","FR")))
                .thenReturn(HolidayDtoFactory.countHolidaysWithOutWeekend());
        // Send request as body to /last3/{countryCode}
        /*RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/holidays/counts")
                .accept(MediaType.APPLICATION_JSON)
                .content(c);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());*/
    }

    @Test
    void getDuplicateHolidays() {
    }
}