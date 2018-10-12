package com.silversnowsoftware.vc.di.module;

import android.app.Application;
import android.content.Context;

import com.silversnowsoftware.vc.di.ApplicationContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by burak on 10/12/2018.
 */

@Module

public class ApplicationModule {


    private final Application mApplication;


    public ApplicationModule(Application application) {

        mApplication = application;

    }


    @Provides
    @ApplicationContext
    Context provideContext() {

        return mApplication;

    }
}