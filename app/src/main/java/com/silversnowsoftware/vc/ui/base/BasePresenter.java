package com.silversnowsoftware.vc.ui.base;

/**
 * Created by burak on 10/14/2018.
 */

public class BasePresenter <V extends IView> implements IPresenter<V> {

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
}
