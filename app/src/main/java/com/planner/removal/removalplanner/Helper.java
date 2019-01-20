package com.planner.removal.removalplanner;

import android.content.Context;
import android.os.Build;
import android.os.LocaleList;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class Helper {

    public static Locale currentLocal;


    public static void setCurrentLocale(Context context){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            currentLocal = context.getResources().getConfiguration().getLocales().get(0);
        } else{
            currentLocal = context.getResources().getConfiguration().locale;
        }

        /*
        if (BuildConfig.DEBUG) {
            for (Locale loc : Locale.getAvailableLocales()) {
                if (loc.getISO3Language().equals("deu") && loc.getCountry().equalsIgnoreCase("DE")) {
                    Locale.setDefault(loc);
                }
            }

            currentLocal = Locale.getDefault();
        }
        */
    }

    public static String formatDateTo(Date date) {
        if(date.getTime() == Long.MAX_VALUE)
            return "";

        DateFormat dfTime;
        if(date.getHours() == 0 && date.getMinutes() == 0) {
            dfTime = DateFormat.getDateInstance(DateFormat.SHORT, currentLocal);
        } else {
            dfTime = DateFormat.getDateTimeInstance(DateFormat.DATE_FIELD, DateFormat.SHORT, currentLocal);
        }

        return dfTime.format(date);
    }

    public static String intCentToString(Long cent) {

        if(cent == 0)
            return "";

        NumberFormat nf = NumberFormat.getCurrencyInstance(currentLocal);
        nf.setCurrency(Currency.getInstance(currentLocal));

        double d = (cent.doubleValue()) / 100;
        return nf.format(d);
    }
}
