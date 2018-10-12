package com.silversnowsoftware.vc.di.component;

import android.app.Application;
import android.content.Context;

import com.silversnowsoftware.vc.Dagger2Application;
import com.silversnowsoftware.vc.di.ApplicationContext;
import com.silversnowsoftware.vc.di.module.ApplicationModule;
import com.silversnowsoftware.vc.ui.compression.videocompress.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by burak on 10/12/2018.
 */

@Singleton

@Component(modules = ApplicationModule.class)

public interface ApplicationComponent {


    void inject(Dagger2Application app);


    @ApplicationContext
    Context context();



}