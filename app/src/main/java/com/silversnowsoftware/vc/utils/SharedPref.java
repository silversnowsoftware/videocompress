package com.silversnowsoftware.vc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.silversnowsoftware.vc.model.FileModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by burak on 11/12/2018.
 */

public class SharedPref {


    public static void putData(String key, Object object, Context context) {
        try {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            Gson gson = new Gson();
            String value = gson.toJson(object);
            editor.putString(key, value);
            editor.commit();
        } catch (Exception ex) {

        }
    }


    public static Object getData(String key, Type type, Context context) {
        Object obj = null;

        try {
            Gson gson = new Gson();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String json = preferences.getString(key, "");

            obj = (Object) gson.fromJson(json, type);

        } catch (Exception ex) {

        }
        return obj;
    }


    public static String getData(String key, Context context) {
        String value = null;
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            value = preferences.getString(key, "");

        } catch (Exception ex) {

        }
        return value;
    }

    public static void Clear(Context context) {
        try {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            preferences.edit().clear().commit();
        } catch (Exception ex) {

        }
    }

    public static void RemoveKey(String key, Context context) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            preferences.edit().remove(key).commit();

        } catch (Exception ex) {

        }
    }
}
