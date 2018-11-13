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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String value = gson.toJson(object);
        editor.putString(key, value);
        editor.commit();
    }


    public static  <T>Class<T> getData(String key,  Class<T> type, Context context) {
        Gson gson = new Gson();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = preferences.getString(key, "");
        Class<T> obj = null;
        Type listType = new TypeToken<T>(){}.getType();
        obj = (Class<T>) gson.fromJson(json, listType);
        return obj;
    }

    public static  <T>ArrayList<T> getDataList(String key,  Class<T> type, Context context) {
        Gson gson = new Gson();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = preferences.getString(key, "");
        ArrayList<T> obj = null;
        Type listType = new TypeToken<ArrayList<T>>(){}.getType();
        obj = (ArrayList<T>) gson.fromJson(json, listType);
        return obj;
    }


    public static String getData(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = preferences.getString(key, "");
        return value;
    }

}
