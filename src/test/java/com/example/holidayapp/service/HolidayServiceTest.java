package com.example.holidayapp.service;

import com.example.holidayapp.exception.HolidayApiException;
import com.example.holidayapp.model.CountryHolidaysDto;
import com.example.holidayapp.model.HolidayCountPerCountryDto;
import com.example.holidayapp.model.HolidayDto;
import com.example.holidayapp.model.HolidayDtoFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HolidayServiceTest {

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private HolidayService holidayService;

    private final String BASE_URL = "https://date.nager.at/Api/v3/PublicHolidays";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        holidayService = new HolidayService(restTemplate, BASE_URL);
    }

    @Test
    void getLast3() {
        when(restTemplate.getForObject(BASE_URL +"/"+ LocalDate.now().getYear() + "/AT",
                HolidayDto[].class)).thenReturn(HolidayDtoFactory.getHolidayDtoList());
        List<HolidayDto> result = holidayService.getLast3("AT");
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getLocalName()).isEqualTo("Holiday1");
        assertThat(result.get(1).getLocalName()).isEqualTo("Holiday2");
        assertThat(result.get(2).getLocalName()).isEqualTo("Holiday3");

    }

    @Test
    void getLast3ForEmptyResponse() {
        when(restTemplate.getForObject(BASE_URL + "/" + LocalDate.now().getYear() + "/AT",
                HolidayDto[].class)).thenReturn(null);
        assertThatThrownBy(()-> holidayService.getLast3("AT"),
                "Exception",
                HolidayApiException.class);
    }

    @Test
    void getHolidayCountNotOnWeekend() {

        when(restTemplate.getForObject(BASE_URL + "/2025/DE",
                HolidayDto[].class)).thenReturn(HolidayDtoFactory.getHolidayDtoForCountryCodeDeCount());

        when(restTemplate.getForObject(BASE_URL+ "/2025/FR",
                HolidayDto[].class)).thenReturn(HolidayDtoFactory.getHolidayDtoForCountryCodeFrCount());

        List<HolidayCountPerCountryDto> result = holidayService.getHolidayNotOnWeekend(2025, List.of("DE","FR"));
        assertThat(result).hasSize(2);
        assertThat(result.get(0).workingDayHolidays()).isGreaterThanOrEqualTo(result.get(1).workingDayHolidays());
    }

    @Test
    void getHolidayCountNullFromApiCall() {

        when(restTemplate.getForObject(BASE_URL + "/2025/DE",
                HolidayDto[].class)).thenReturn(null);

        //This is coming on sunday.
        when(restTemplate.getForObject(BASE_URL+ "/2025/FR",
                HolidayDto[].class)).thenReturn(HolidayDtoFactory.getHolidayDtoForCountryCodeFrCount());

        List<HolidayCountPerCountryDto> result = holidayService.getHolidayNotOnWeekend(2025, List.of("DE","FR"));
        assertThat(result).hasSize(2);
        assertThat(result.getFirst().workingDayHolidays()).isEqualTo(0);
        assertThat(result.getLast().workingDayHolidays()).isEqualTo(0);
    }

    @Test
    void getCommonHolidays() {
        when(restTemplate.getForObject(BASE_URL + "/2025/DE",
                HolidayDto[].class)).thenReturn(HolidayDtoFactory.getHolidayDtoForDe());

        when(restTemplate.getForObject(BASE_URL + "/2025/FR",
                HolidayDto[].class)).thenReturn(HolidayDtoFactory.getHolidayDtoForFr());

        List<CountryHolidaysDto> result = holidayService
                .getCommonHolidays(2025, "DE","FR");
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().localNameFirstCountry()).isEqualTo("Epiphany");
        assertThat(result.getFirst().localNameSecondCountry()).isEqualTo("Fête du Travail");

    }

    @Test
    void getCommonHolidaysForNullResponse() {
        when(restTemplate.getForObject(BASE_URL+ "/2025/DE",
                HolidayDto[].class)).thenReturn(null);

        when(restTemplate.getForObject(BASE_URL + "/2025/FR",
                HolidayDto[].class)).thenReturn(HolidayDtoFactory.getHolidayDtoForFr());
        assertThatThrownBy(()-> holidayService
                        .getCommonHolidays(2025, "DE","FR"),
                "Exception",
                HolidayApiException.class);
    }

    @Test
    void getHolidayDtoThrowException() {
        when(restTemplate.getForObject(BASE_URL+ "/2025/DE",
                HolidayDto[].class)).thenThrow(new RestClientResponseException("Internal Server Error",
                HttpStatus.BAD_GATEWAY.value(),"failed!!",null,null,null));
        assertThatThrownBy(()-> holidayService.getLast3("DE"), "Error", HolidayApiException.class);
    }
}