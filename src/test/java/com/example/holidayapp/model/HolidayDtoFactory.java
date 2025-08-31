package com.example.holidayapp.model;

import java.time.LocalDate;
import java.util.List;

public class HolidayDtoFactory {

    public static HolidayDto[] getHolidayDtoList() {
        HolidayDto h1 = new HolidayDto();
        h1.setDate(LocalDate.now().minusDays(2));
        h1.setLocalName("Holiday1");
        h1.setCountryCode("AT");

        HolidayDto h2 = new HolidayDto();
        h2.setDate(LocalDate.now().minusDays(3));
        h2.setLocalName("Holiday2");
        h2.setCountryCode("AT");

        HolidayDto h3 = new HolidayDto();
        h3.setDate(LocalDate.now().minusDays(22));
        h3.setLocalName("Holiday3");
        h3.setCountryCode("AT");

        HolidayDto h4 = new HolidayDto();
        h4.setDate(LocalDate.now().minusDays(30));
        h4.setLocalName("Holiday4");
        h4.setCountryCode("AT");

        HolidayDto h5 = new HolidayDto();
        h5.setDate(LocalDate.now().plusDays(5));
        h5.setLocalName("Holiday5");
        h5.setCountryCode("AT");
        HolidayDto[] list = new HolidayDto[] {h1,h2,h3,h4,h5};
        return list;
    }

    public static HolidayDto[] getHolidayDtoForCountryCodeDeCount() {
        HolidayDto h1 = new HolidayDto();
        h1.setDate(LocalDate.of(2025,7,3));
        h1.setCountryCode("DE");

        HolidayDto h2 = new HolidayDto();
        h2.setDate(LocalDate.of(2025,7,5));
        h2.setCountryCode("DE");

        HolidayDto[] list = new HolidayDto[] {h1,h2};
        return list;
    }

    public static HolidayDto[] getHolidayDtoForCountryCodeFrCount() {
        HolidayDto h1 = new HolidayDto();
        h1.setDate(LocalDate.of(2025,7,5));
        h1.setCountryCode("FR");

        HolidayDto[] list = new HolidayDto[] {h1};
        return list;
    }

    public static HolidayDto[] getHolidayDtoForDe() {
        LocalDate date = LocalDate.of(2025, 12,25);

        HolidayDto deHoliday = new HolidayDto();
        deHoliday.setDate(date);
        deHoliday.setLocalName("Epiphany");

        return new HolidayDto[] {deHoliday,deHoliday};
    }

    public static HolidayDto[] getHolidayDtoForFr() {
        LocalDate date = LocalDate.of(2025, 12,25);

        HolidayDto frHoliday = new HolidayDto();
        frHoliday.setDate(date);
        frHoliday.setLocalName("Fête du Travail");

        HolidayDto[] list = new HolidayDto[] {frHoliday};
        return list;
    }

    public static HolidayDto[] last3HolidaysResponse() {
        HolidayDto h1 = new HolidayDto();
        h1.setDate(LocalDate.now().minusDays(2));
        h1.setLocalName("Holiday1");
        h1.setCountryCode("AT");

        HolidayDto h2 = new HolidayDto();
        h2.setDate(LocalDate.now().minusDays(3));
        h2.setLocalName("Holiday2");
        h2.setCountryCode("AT");

        HolidayDto h3 = new HolidayDto();
        h3.setDate(LocalDate.now().minusDays(22));
        h3.setLocalName("Holiday3");
        h3.setCountryCode("AT");
        HolidayDto[] list = new HolidayDto[] {h1,h2,h3};
        return list;
    }

    public static List<HolidayCountPerCountryDto> countHolidaysWithOutWeekend() {
        HolidayCountPerCountryDto h1 = new HolidayCountPerCountryDto("DE", 1);
        HolidayCountPerCountryDto h2 = new HolidayCountPerCountryDto("FE", 1);
        return List.of(h1, h2);
    }

}
