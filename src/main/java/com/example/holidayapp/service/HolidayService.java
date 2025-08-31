package com.example.holidayapp.service;

import com.example.holidayapp.model.CountryHolidaysDto;
import com.example.holidayapp.model.HolidayCountPerCountryDto;
import com.example.holidayapp.model.HolidayDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Holiday Service for all holiday related responses.
 */
@Service
@Slf4j
public class HolidayService {

    private final RestTemplate restTemplate;
    private static final String API_BASE_URL = "https://date.nager.at/Api/v3";

    public HolidayService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Given a country code to return last 3 celebrated holidays.
     * @param countryCode string
     * @return List of HolidayDto
     */
    public List<HolidayDto> getLast3(String countryCode) {
        int currentYear = LocalDate.now().getYear();
        HolidayDto[] holidayDtoList = getHolidayDtos(currentYear, countryCode);
        if(holidayDtoList.length == 0)
            return Collections.emptyList();
        return Arrays.stream(holidayDtoList)
                .filter(Objects::nonNull)
                //.filter(holidayDto->holidayDto.getDate() != null)
                .filter(holiday-> holiday.getDate().isBefore(LocalDate.now()))
                .sorted(Comparator.comparing(HolidayDto::getDate).reversed())
                .limit(3)
                .collect(Collectors.toList());

    }

    /**
     * Given year and country Codes,return public holidays for each country, not falling on weekends.
     * @param year int
     * @param countryCodes list of countries
     * @return List of HolidayDtoForCountPerCountry
     */
    public List<HolidayCountPerCountryDto> getHolidayNotOnWeekend(int year, List<String> countryCodes) {
        List<HolidayCountPerCountryDto> result = new ArrayList<>();
        for(String countryCode : countryCodes) {
            HolidayDto[] holidayDtoList = getHolidayDtos(year, countryCode);
            if(nonNull(holidayDtoList)){
                long count = Arrays.stream(holidayDtoList).filter(holidayDto ->
                    (holidayDto.getDate().getDayOfWeek() != DayOfWeek.SATURDAY
                            && holidayDto.getDate().getDayOfWeek() != DayOfWeek.SUNDAY))
                        .count();
                result.add(new HolidayCountPerCountryDto(countryCode, count));
            } else {
                log.info("Api does not have data for country code {}", countryCode);
                throw new RuntimeException("Api does not have data for country code " + countryCode);
            }
        }
        return result.stream().sorted(Comparator.comparing(HolidayCountPerCountryDto::workingDayHolidays)
                        .reversed()).collect(Collectors.toList());
    }

    /**
     * This method return duplicate holidays for given 2 countries.
     * @param year year
     * @param countryFirst country code
     * @param countrySecond country code
     * @return CountryHolidaysDto list of countries holidays
     */
    public List<CountryHolidaysDto> getDuplicateHolidays(int year, String countryFirst, String countrySecond) {
        HolidayDto[] holidayDtoForFirst = getHolidayDtos(year, countryFirst);
        HolidayDto[] holidayDtoForSecond = getHolidayDtos(year, countrySecond);

        if(isNull(holidayDtoForFirst) || isNull(holidayDtoForSecond))
            return Collections.emptyList();
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
     * Common method to get data from api.
     * @param year year
     * @param country country
     * @return HolidayDto list
     */

    private HolidayDto[] getHolidayDtos(int year, String country) {
        String url = API_BASE_URL + "/PublicHolidays/" + year + "/" + country;
        return restTemplate.getForObject(url, HolidayDto[].class);
    }
}