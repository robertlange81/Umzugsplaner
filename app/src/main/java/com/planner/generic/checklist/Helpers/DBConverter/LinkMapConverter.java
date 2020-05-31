package com.planner.generic.checklist.Helpers.DBConverter;

import android.arch.persistence.room.TypeConverter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class LinkMapConverter {
    @TypeConverter
    public static TreeMap<String, String> fromString(String val) {
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(val);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TreeMap<String, String> map = new TreeMap<String, String>();
        Iterator<String> keys = jsonObj.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            String value = null;
            try {
                value = jsonObj.get(key).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            map.put(key, value);
        }

        return map;
    }

    @TypeConverter
    public static String fromStringMap(Map<String, String> map) {
        JSONObject obj =new JSONObject(map);
        return obj.toString();
    }
}