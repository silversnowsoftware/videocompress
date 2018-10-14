package com.silversnowsoftware.vc.ui.base;

/**
 * Created by burak on 10/14/2018.
 */

public class BasePresenter <V extends IView> implements IPresenter<V> {

    private V mMvpView;

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void onDetach() {

    }
}
