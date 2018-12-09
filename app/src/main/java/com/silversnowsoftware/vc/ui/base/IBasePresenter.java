package com.silversnowsoftware.vc.ui.base;

import android.content.Context;

import com.silversnowsoftware.vc.data.db.IRepository;
import com.silversnowsoftware.vc.model.FileModel;

/**
 * Created by burak on 10/14/2018.
 */

public interface IBasePresenter<V extends IBaseView> {
    void onAttach(V mvpView);

    void onDetach();

    public V getView();

    IRepository<FileModel> getRepositoryFileModel();

}
