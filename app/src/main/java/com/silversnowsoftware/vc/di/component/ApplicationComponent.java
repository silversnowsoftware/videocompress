package com.silversnowsoftware.vc.di.component;

import android.content.Context;

import com.silversnowsoftware.vc.VideoCompressApplication;
import com.silversnowsoftware.vc.di.ApplicationContext;
import com.silversnowsoftware.vc.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by burak on 10/12/2018.
 */

@Singleton

@Component(modules = ApplicationModule.class)

public interface ApplicationComponent {


    void inject(VideoCompressApplication app);


    @ApplicationContext
    Context context();



}