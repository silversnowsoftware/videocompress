package com.silversnowsoftware.vc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

/**
 * Created by burak on 11/12/2018.
 */

public class SharedPref {


    public static void putData(String key, Object object, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String value = gson.toJson(object);
        editor.putString(key, value);
        editor.commit();
    }


    public static  <T extends Class<?>> T getData(String key, Class<?> cls, Context context) {
        Gson gson = new Gson();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = preferences.getString(key, "");

        T obj = null;
        obj = (T) gson.fromJson(json, cls);
        if (obj == null) {
            try {
                obj = (T) cls.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }


    public static String getData(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = preferences.getString(key, "");
        return value;
    }

}
