package com.silversnowsoftware.vc.ui.main;

import com.silversnowsoftware.vc.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by burak on 10/14/2018.
 */

public class MainPresenter <V extends IMainView> extends BasePresenter<V>
        implements IMainPresenter<V>  {

    @Inject
    public MainPresenter() {
        super();
    }
    @Override
    public String TestInject() {
        return  "Test Injectt";
    }
}
