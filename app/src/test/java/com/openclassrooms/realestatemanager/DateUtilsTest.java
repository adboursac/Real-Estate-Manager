package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class DateUtilsTest {

    private LocalDate localDate = LocalDate.of(2022, 1,1);
    private final String  stringDate = "01/01/2022";

    @Test
    public void dateToString_test() {
        assertEquals(stringDate, Utils.dateToString(localDate));
    }

    @Test
    public void stringToDate_test() {
        assertEquals(localDate.getDayOfMonth(), Utils.stringToDate(stringDate,null).getDayOfMonth());
        assertEquals(localDate.getMonth(), Utils.stringToDate(stringDate, null).getMonth());
        assertEquals(localDate.getYear(), Utils.stringToDate(stringDate,null).getYear());
        assertEquals(localDate.getYear(), Utils.stringToDate("wrong format", localDate).getYear());
    }
}
