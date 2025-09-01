package com.example.holidayapp.service;

import static java.util.Objects.isNull;

import com.example.holidayapp.exception.HolidayApiException;
import com.example.holidayapp.model.CountryHolidaysDto;
import com.example.holidayapp.model.HolidayCountPerCountryDto;
import com.example.holidayapp.model.HolidayDto;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


/**
 * Holiday Service for all holiday related responses.
 */
@Service
@Slf4j
public class HolidayService {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public HolidayService(RestTemplate restTemplate,
                          @Value("${holidayapp.external.api.base_url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    /**
     * Given a country code to return last 3 celebrated holidays.
     *
     * @param countryCode string
     * @return List of HolidayDto
     */
    public List<HolidayDto> getLast3(String countryCode) {
        int currentYear = LocalDate.now().getYear();
        HolidayDto[] holidayDtoList = getHolidayDtos(currentYear, countryCode);
        if (isNull(holidayDtoList)) {
            log.error("External API is not returning any data for country code:%s".formatted(countryCode));
            throw new HolidayApiException("External API is not returning any data for country code:"
                    + countryCode);
        }
        return Arrays.stream(holidayDtoList)
                .filter(holidayDto -> holidayDto.getDate() != null)
                .filter(holiday -> holiday.getDate().isBefore(LocalDate.now()))
                .sorted(Comparator.comparing(HolidayDto::getDate).reversed())
                .limit(3)
                .collect(Collectors.toList());

    }

    /**
     * Given year and country Codes,return public holidays for each country, not falling on weekends.
     *
     * @param year         int
     * @param countryCodes list of countries
     * @return List of HolidayDtoForCountPerCountry
     */
    public List<HolidayCountPerCountryDto> getHolidayNotOnWeekend(int year, List<String> countryCodes) {
        List<CompletableFuture<HolidayCountPerCountryDto>> futureResult =
                countryCodes.stream().map(code -> getHolidayCount(year, code)).toList();

        return futureResult.stream().map(CompletableFuture::join)
                .sorted(Comparator.comparing(HolidayCountPerCountryDto::workingDayHolidays)
                        .reversed()).toList();
    }

    /**
     * This method return common holidays for given 2 countries.
     *
     * @param year          year
     * @param countryFirst  country code
     * @param countrySecond country code
     * @return CountryHolidaysDto list of countries holidays
     */
    public List<CountryHolidaysDto> getCommonHolidays(int year, String countryFirst, String countrySecond) {
        HolidayDto[] holidayDtoForFirst = getHolidayDtos(year, countryFirst);
        HolidayDto[] holidayDtoForSecond = getHolidayDtos(year, countrySecond);

        if (isNull(holidayDtoForFirst) || isNull(holidayDtoForSecond)) {
            log.error("External API is not returning any data for country code " + countryFirst
                    + "or" + countrySecond);
            throw new HolidayApiException("External API is not returning any" +
                    "data for country code or " + countryFirst + "or" + countrySecond);
        }
        Map<LocalDate, HolidayDto> mapFirst = Arrays.stream(holidayDtoForFirst)
                .collect(Collectors.toMap(HolidayDto::getDate, Function.identity()));
        return Arrays.stream(holidayDtoForSecond).filter(h ->
                        mapFirst.containsKey(h.getDate()))
                .map(h -> new CountryHolidaysDto(h.getDate(),
                        mapFirst.get(h.getDate()).getLocalName(),
                        h.getLocalName()))
                .collect(Collectors.toList());
    }

    /**
     * Async method to get the count for each country code.
     *
     * @param year        year
     * @param countryCode country code
     * @return CompletableFuture of HolidayCountPerCountryDto
     */
    @Async
    private CompletableFuture<HolidayCountPerCountryDto> getHolidayCount(int year, String countryCode) {
        HolidayDto[] holidayDtoList = getHolidayDtos(year, countryCode);
        if (isNull(holidayDtoList)) {
            return CompletableFuture.completedFuture(new HolidayCountPerCountryDto(countryCode, 0));
        }
        long count = Arrays.stream(holidayDtoList).filter(holidayDto ->
                        (holidayDto.getDate().getDayOfWeek() != DayOfWeek.SATURDAY
                                && holidayDto.getDate().getDayOfWeek() != DayOfWeek.SUNDAY))
                .count();
        return CompletableFuture.completedFuture(new HolidayCountPerCountryDto(countryCode, count));
    }

    /**
     * Common method to get data from api.
     *
     * @param year    year
     * @param country country
     * @return HolidayDto list
     */

    private HolidayDto[] getHolidayDtos(int year, String country) {
        try {
            String url = baseUrl + "/" + year + "/" + country;
            return restTemplate.getForObject(url, HolidayDto[].class);
        } catch (RestClientException ex) {
            throw new HolidayApiException("Failed to fetch holidays from external API.", ex);
        }
    }
}