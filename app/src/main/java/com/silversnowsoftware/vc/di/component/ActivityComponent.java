package com.silversnowsoftware.vc.di.component;

import com.silversnowsoftware.vc.di.PerActivity;
import com.silversnowsoftware.vc.di.module.ActivityModule;
import com.silversnowsoftware.vc.ui.editor.EditorActivity;
import com.silversnowsoftware.vc.ui.main.Main2Activity;
import com.silversnowsoftware.vc.ui.main.MainActivity;

import dagger.Component;

/**
 * Created by burak on 10/14/2018.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);
    void inject(Main2Activity activity);
    void inject(EditorActivity activity);
}
