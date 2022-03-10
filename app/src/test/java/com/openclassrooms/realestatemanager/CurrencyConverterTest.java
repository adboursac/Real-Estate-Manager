package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CurrencyConverterTest {

    private final int euro = 100;
    private final int dollar = 123;

    @Test
    public void convertEuroToDollar_test() {
        assertEquals(dollar, Utils.convertEuroToDollar(euro));
    }

    @Test
    public void convertDollarToEuro_test() {
        assertEquals(euro, Utils.convertDollarToEuro(dollar));
    }
}