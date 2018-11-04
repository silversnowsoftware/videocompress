package com.silversnowsoftware.vc.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.silversnowsoftware.vc.data.db.DbFileModel;
import com.silversnowsoftware.vc.data.db.IRepository;
import com.silversnowsoftware.vc.model.FileModel;

/**
 * Created by burak on 10/14/2018.
 */

public class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {

    private V mView;

    @Override
    public void onAttach(V view) {
        mView = view;
    }

    @Override
    public void onDetach() {

    }

    @Override
    public V getView() {
        return mView;
    }

    @Override
    public void putData(String key, Object object, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String value = gson.toJson(object);
        editor.putString(key, value);
        editor.commit();
    }

    @Override
    public <T extends Class<?>> T getData(String key, Class<?> cls, Context context) {
        Gson gson = new Gson();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = preferences.getString(key, "");
        T obj = (T) gson.fromJson(json, cls);
        return obj;
    }

    @Override
    public String getData(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = preferences.getString(key, "");
        return value;
    }

    public IRepository<FileModel> DbFileModel() {
        return new DbFileModel(((Activity) getView()).getApplicationContext());
    }
}
