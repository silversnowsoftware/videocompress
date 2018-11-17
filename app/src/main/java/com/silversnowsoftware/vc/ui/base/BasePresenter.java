package com.silversnowsoftware.vc.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.silversnowsoftware.vc.data.db.DbFileModel;
import com.silversnowsoftware.vc.data.db.IRepository;
import com.silversnowsoftware.vc.model.FileModel;

import java.util.ArrayList;

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



    protected Context getContext() {
        return ((Activity) getView()).getApplicationContext();
    }


    public IRepository<FileModel> DbFileModel() {
        return new DbFileModel(getContext());
    }
}
