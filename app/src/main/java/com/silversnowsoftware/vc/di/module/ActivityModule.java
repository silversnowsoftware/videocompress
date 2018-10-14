package com.silversnowsoftware.vc.di.module;

import com.silversnowsoftware.vc.di.PerActivity;
import com.silversnowsoftware.vc.ui.main.IMainPresenter;
import com.silversnowsoftware.vc.ui.main.IMainView;
import com.silversnowsoftware.vc.ui.main.MainPresenter;

import dagger.Provides;

/**
 * Created by burak on 10/14/2018.
 */

public class ActivityModule {

    @Provides
    @PerActivity
    IMainPresenter<IMainView> provideMainPresenter(MainPresenter<IMainView> presenter) {
        return presenter;
    }
}
