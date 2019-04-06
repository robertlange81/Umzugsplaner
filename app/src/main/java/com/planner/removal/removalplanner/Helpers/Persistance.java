package com.planner.removal.removalplanner.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class Persistance {
    public enum SaveType {
        Sort(0);

        private int value;

        SaveType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static boolean SaveSetting(SaveType saveType, int value, Activity activity) {

        try {
            SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(saveType.toString(), value);
            editor.commit();
            return true;
        } catch (Exception ex) {
            Log.e("Save setting", ex.getMessage());
        }

        return false;
    }

    public static int GetSetting(SaveType saveType, Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getInt(saveType.toString(), 0);
    }
}
