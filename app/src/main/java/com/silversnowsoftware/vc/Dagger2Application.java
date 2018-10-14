package com.silversnowsoftware.vc;

import android.app.Application;
import com.silversnowsoftware.vc.di.component.ApplicationComponent;
//import com.silversnowsoftware.vc.di.component.DaggerApplicationComponent;
import com.silversnowsoftware.vc.di.component.DaggerApplicationComponent;
import com.silversnowsoftware.vc.di.module.ApplicationModule;


/**
 * Created by burak on 10/12/2018.
 */

public class Dagger2Application extends Application {


    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();

        mApplicationComponent.inject(this);


    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }


    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}