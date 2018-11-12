package com.silversnowsoftware.vc.ui.main;

import android.content.Intent;

import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.ui.base.IBasePresenter;

/**
 * Created by burak on 10/14/2018.
 */

public interface IMainPresenter<V extends IMainView> extends IBasePresenter<V> {

    void collectFiles(Intent data);

    FileModel createFileModel(String path);
}
