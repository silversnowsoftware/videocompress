package com.silversnowsoftware.vc.ui.base;

/**
 * Created by burak on 10/14/2018.
 */

public interface IPresenter<V extends IView> {
    void onAttach(V mvpView);

    void onDetach();

}
