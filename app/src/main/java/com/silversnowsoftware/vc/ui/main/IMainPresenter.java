package com.silversnowsoftware.vc.ui.main;

import com.silversnowsoftware.vc.ui.base.IPresenter;

/**
 * Created by burak on 10/14/2018.
 */

public interface IMainPresenter <V extends IMainView> extends IPresenter<V> {
    String TestInject();
}
