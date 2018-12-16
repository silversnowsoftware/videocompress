package com.silversnowsoftware.vc;

import android.app.Application;

import com.silversnowsoftware.vc.di.component.ApplicationComponent;
//import com.silversnowsoftware.vc.di.component.DaggerApplicationComponent;
import com.silversnowsoftware.vc.di.component.DaggerApplicationComponent;
import com.silversnowsoftware.vc.di.module.ApplicationModule;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.model.logger.LogModel;
import com.silversnowsoftware.vc.utils.SharedPref;
import com.silversnowsoftware.vc.utils.Utility;
import com.silversnowsoftware.vc.utils.constants.Constants;
import com.silversnowsoftware.vc.utils.constants.Globals;
import com.silversnowsoftware.vc.utils.helpers.LogHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by burak on 10/12/2018.
 */

public class VideoCompressApplication extends Application {
    private static final String className = VideoCompressApplication.class.getSimpleName();
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Globals.selectedFiles = new ArrayList<FileModel>();

            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this)).build();
            mApplicationComponent.inject(this);
            SharedPref.Clear(this);
        } catch (Exception ex) {

            LogHelper.Log(className, ex);
        }
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }


    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}