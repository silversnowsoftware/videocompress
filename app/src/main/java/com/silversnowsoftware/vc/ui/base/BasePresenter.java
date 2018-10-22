package com.silversnowsoftware.vc.ui.base;

import android.app.Activity;

import com.silversnowsoftware.vc.data.db.DbFileModel;
import com.silversnowsoftware.vc.data.db.IRepository;
import com.silversnowsoftware.vc.model.FileModel;

/**
 * Created by burak on 10/14/2018.
 */

public class BasePresenter<V extends IView> implements IPresenter<V> {

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

    public IRepository<FileModel> DbFileModel() {
        return new DbFileModel(((Activity) getView()).getApplicationContext());
    }
}
