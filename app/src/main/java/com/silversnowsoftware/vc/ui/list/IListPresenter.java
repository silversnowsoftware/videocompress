package com.silversnowsoftware.vc.ui.list;

import android.app.Activity;
import android.view.View;

import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.ui.base.BaseResponse;
import com.silversnowsoftware.vc.ui.base.IBasePresenter;

import java.util.List;

/**
 * Created by burak on 11/16/2018.
 */

public interface IListPresenter<V extends IListView> extends IBasePresenter<V> {
    public void fillListView();

    public void setViewHolder();

    public BaseResponse deleteSelectedFile(FileModel fileModel);
}
