package com.silversnowsoftware.vc.ui.base;

import android.content.Context;

/**
 * Created by burak on 10/14/2018.
 */

public interface IBasePresenter<V extends IBaseView> {
    void onAttach(V mvpView);

    void onDetach();

    public V getView();

     

}
