package com.silversnowsoftware.vc.ui.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

/**
 * Created by burak on 10/14/2018.
 */

public interface IBasePresenter<V extends IBaseView> {
    void onAttach(V mvpView);

    void onDetach();

    public V getView();

    public void putData(String key, Object object, Context context);

    public <T extends Class<?>> T getData(String key, Class<?> cls, Context context);

    public String getData(String key, Context context);
}
