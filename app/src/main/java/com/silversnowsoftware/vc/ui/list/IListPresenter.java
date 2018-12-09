package com.silversnowsoftware.vc.ui.list;

import android.app.Activity;
import android.view.View;

import com.silversnowsoftware.vc.ui.base.IBasePresenter;

/**
 * Created by burak on 11/16/2018.
 */

public interface IListPresenter<V extends IListView> extends IBasePresenter<V> {
    public void fillListView();
    public void setViewHolder();

}
