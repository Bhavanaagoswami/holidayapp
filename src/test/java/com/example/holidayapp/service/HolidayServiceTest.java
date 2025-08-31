package com.example.holidayapp.service;

import com.example.holidayapp.model.CountryHolidaysDto;
import com.example.holidayapp.model.HolidayCountPerCountryDto;
import com.example.holidayapp.model.HolidayDto;
import com.example.holidayapp.model.HolidayDtoFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class HolidayServiceTest {

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private HolidayService holidayService;

    private final String BASE_URL = "https://date.nagar.at/api/v3";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getLast3() {


        when(restTemplate.getForObject(BASE_URL+ "PublicHolidays/" + LocalDate.now().getYear() + "/AT",
                HolidayDto[].class)).thenReturn(HolidayDtoFactory.getHolidayDtoList());
        List<HolidayDto> result = holidayService.getLast3("AT");
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getLocalName()).isEqualTo("Holiday1");
        assertThat(result.get(1).getLocalName()).isEqualTo("Holiday2");
        assertThat(result.get(2).getLocalName()).isEqualTo("Holiday3");

    }

    @Test
    void getHolidayCountNotOnWeekend() {

        when(restTemplate.getForObject(BASE_URL+ "PublicHolidays/2025/DE",
                HolidayDto[].class)).thenReturn(HolidayDtoFactory.getHolidayDtoForCountryCodeDeCount());

        when(restTemplate.getForObject(BASE_URL+ "PublicHolidays/2025/FR",
                HolidayDto[].class)).thenReturn(HolidayDtoFactory.getHolidayDtoForCountryCodeFrCount());

        List<HolidayCountPerCountryDto> result = holidayService.getHolidayNotOnWeekend(2025, List.of("DE","FR"));
        assertThat(result).hasSize(2);
        assertThat(result.get(0).workingDayHolidays()).isGreaterThanOrEqualTo(result.get(1).workingDayHolidays());
    }

    @Test
    void getDuplicateHolidays() {
        when(restTemplate.getForObject(BASE_URL+ "PublicHolidays/2025/DE",
                HolidayDto[].class)).thenReturn(HolidayDtoFactory.getHolidayDtoForDe());

        when(restTemplate.getForObject(BASE_URL+ "PublicHolidays/2025/FR",
                HolidayDto[].class)).thenReturn(HolidayDtoFactory.getHolidayDtoForFr());

        List<CountryHolidaysDto> result = holidayService
                .getDuplicateHolidays(2025, "DE","FR");
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().getLocalNameCountryFirst()).isEqualTo("Epiphany");
        assertThat(result.getFirst().getLocalNameCountrySecond()).isEqualTo("Fête du Travail");

    }
}