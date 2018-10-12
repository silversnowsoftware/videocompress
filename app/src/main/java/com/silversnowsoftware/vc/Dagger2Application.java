package com.silversnowsoftware.vc;

import android.app.Application;
import com.silversnowsoftware.vc.di.component.ApplicationComponent;
import com.silversnowsoftware.vc.di.component.DaggerApplicationComponent;
import com.silversnowsoftware.vc.di.module.ApplicationModule;


/**
 * Created by burak on 10/12/2018.
 */

public class Dagger2Application extends Application {


    private ApplicationComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeInjector();

    }

    private void initializeInjector(){
        appComponent =  DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getAppComponent(){
        return appComponent;
    }
}