package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.openclassrooms.realestatemanager.MainApplication;
import com.openclassrooms.realestatemanager.R;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    /**
     * Conversion of a price of a property (Dollars to Euros)
     *
     * @param dollars amount in dollars to be converted
     * @return amount converted into dollars
     */
    public static int convertDollarToEuro(int dollars) {
        return (int) Math.round(dollars * 0.812);
    }

    /**
     * Conversion of a price of a property (Euros to Dollars)
     *
     * @param euro amount in euros to be converted
     * @return amount converted into euros
     */
    public static int convertEuroToDollar(int euro) {
        return (int) Math.round((float) euro / 0.812);
    }

    /**
     * Converting today's date to a more appropriate format
     *
     * @return date in dd/MM/yyyy format
     */
    public static String getTodayDate() {
        return LocalDate.now().format(getDefaultDateTimeFormatter());
    }

    /**
     * Network connection check
     *
     * @param context Context
     * @return true if wifi is enabled, false instead
     */
    public static Boolean isInternetAvailable(Context context) {
        Context applicationContext = context.getApplicationContext();
        WifiManager wifi = (WifiManager) applicationContext.getSystemService(Context.WIFI_SERVICE);
        if (wifi == null) return false;
        return wifi.isWifiEnabled();
    }

    /**
     * Provide a default date format in dd/MM/yyyy format
     *
     * @return DateTimeFormatter with dd/MM/yyyy format
     */
    public static DateTimeFormatter getDefaultDateTimeFormatter() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    /**
     * Generate a dollar price formatted string form given number
     *
     * @param number number to convert to price String
     * @return example : 1000 integer value will return string $1'000
     */
    public static String dollarString(int number) {
        String formattedNumber = NumberFormat.getNumberInstance(Locale.US).format(number);
        return "$" + formattedNumber;
    }

    /**
     * Generate string from given square meters surface.
     *
     * @param surface surface value
     * @return value followed by sq m
     */
    public static String surfaceString(int surface) {
        Context context = MainApplication.getApplication();
        return String.format(context.getString(R.string.placeholder_surface), surface);
    }

    /**
     * Secure integer string
     *
     * @param integer surface value
     * @return string representation of given integer
     */
    public static String integerString(int integer) {
        Context context = MainApplication.getApplication();
        return String.format(context.getString(R.string.placeholder_int), integer);
    }

    /**
     * Convert date string to LocalDate object
     *
     * @param dateString  string date in dd/MM/yyyy format
     * @param defaultDate default localDate to return if conversion fail
     * @return LocalDate with dd/MM/yyyy format
     */
    public static LocalDate stringToDate(String dateString, LocalDate defaultDate) {
        LocalDate date;
        try {
            date = LocalDate.parse(dateString, getDefaultDateTimeFormatter());
        } catch (Exception e) {
            date = defaultDate;
        }
        return date;
    }

    /**
     * Convert given LocalDate to string in dd/MM/yyyy format
     *
     * @param date date to convert
     * @return string in dd/MM/yyyy format
     */
    public static String dateToString(LocalDate date) {
        return date.format(getDefaultDateTimeFormatter());
    }

}
