package com.planner.removal.removalplanner.Helpers;

import android.content.Context;
import android.os.Build;

import com.planner.removal.removalplanner.BuildConfig;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskFormater {

    public static Locale currentLocal;
    private static NumberFormat numberFormat;

    public static void setCurrentLocale(Context context){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            currentLocal = context.getResources().getConfiguration().getLocales().get(0);
        } else{
            currentLocal = context.getResources().getConfiguration().locale;
        }

        numberFormat = NumberFormat.getCurrencyInstance(currentLocal);
        numberFormat.setMinimumFractionDigits(2);

        currentLocal = Locale.getDefault();
        if (BuildConfig.DEBUG) {
            for (Locale loc : Locale.getAvailableLocales()) {
                if (loc.getISO3Language().equals("deu") && loc.getCountry().equalsIgnoreCase("DE")) {
                    Locale.setDefault(loc);
                    break;
                }
            }
        }
    }

    public static String formatDateToSring(Date date) {
        if(date.getTime() == Long.MAX_VALUE)
            return "";

        DateFormat dfTime;
        if(date.getHours() == 0 && date.getMinutes() == 0) {
            dfTime = getShortDateInstance(currentLocal);
        } else {
            dfTime = getShortDateTimeInstanceWithoutYears(currentLocal);
        }
        return dfTime.format(date);
    }

    public static DateFormat getShortDateInstance(Locale locale) {
        return DateFormat.getDateInstance(DateFormat.SHORT, locale);
    }

    public static DateFormat getShortDateInstanceWithoutYears(Locale locale) {
        SimpleDateFormat sdf = (SimpleDateFormat) getShortDateInstance(locale);
        sdf.applyPattern(sdf.toPattern().replaceAll("y+", ""));
        return sdf;
    }

    public static DateFormat getShortDateTimeInstance(Locale locale) {
        return DateFormat.getDateTimeInstance(DateFormat.DATE_FIELD, DateFormat.SHORT, locale);
    }

    public static DateFormat getShortDateTimeInstanceWithoutYears(Locale locale) {
        SimpleDateFormat sdf = (SimpleDateFormat) getShortDateTimeInstance(locale);
        sdf.applyPattern(sdf.toPattern().replaceAll("y+", ""));
        return sdf;
    }

    public static String intDecimalsToString(Long cent) {

        if(cent == 0)
            return "";

        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setRoundingMode(RoundingMode.HALF_UP);
        double d = (cent.doubleValue()) / 100;
        return getNumberFormat().format(d);
    }

    public static String intSigToString(Long cent) {

        if(cent == 0)
            return "";

        numberFormat.setMaximumFractionDigits(0);
        numberFormat.setRoundingMode(RoundingMode.DOWN);
        double d = (cent.doubleValue()) / 100;
        return transformCurrencySymbol(numberFormat.format(d), true, true);
    }

    public static String intFractionsToString(Long cent) {

        if(cent == 0)
            return "";

        NumberFormat basicNumberFormat = NumberFormat.getInstance();
        basicNumberFormat.setMaximumFractionDigits(2);
        basicNumberFormat.setRoundingMode(RoundingMode.HALF_UP);

        double d = Math.abs(cent.doubleValue()) % 100;
        String noCurrency = transformCurrencySymbol(basicNumberFormat.format(d), true, false);
        String noCurrencyDoubleDigit = noCurrency.length() == 1 ? "0" + noCurrency : noCurrency;
        return noCurrencyDoubleDigit;
    }

    private static String transformCurrencySymbol(String number, boolean removeTrailing, boolean addLeading) {
        if(number.isEmpty()) {
            return number;
        }

        if(removeTrailing && number.contains(numberFormat.getCurrency().getSymbol())) {
            number = number.replace("\u00A0","").replace(numberFormat.getCurrency().getSymbol(), "");
        }

        if(addLeading && !number.contains(numberFormat.getCurrency().getSymbol())) {
            number = numberFormat.getCurrency().getSymbol() + " " + number;
        }

        return number;
    }

    public static NumberFormat getNumberFormat() {
        return numberFormat;
    }
}
