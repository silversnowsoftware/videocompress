package com.silversnowsoftware.vc.di.module;

import android.support.v7.app.AppCompatActivity;

import com.silversnowsoftware.vc.di.PerActivity;
import com.silversnowsoftware.vc.ui.editor.EditorPresenter;
import com.silversnowsoftware.vc.ui.editor.IEditorPresenter;
import com.silversnowsoftware.vc.ui.editor.IEditorView;
import com.silversnowsoftware.vc.ui.main.IMainPresenter;
import com.silversnowsoftware.vc.ui.main.IMainView;
import com.silversnowsoftware.vc.ui.main.MainPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by burak on 10/14/2018.
 */
@Module
public class ActivityModule {

    private AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    @Provides
    @PerActivity
    IMainPresenter<IMainView> provideMainPresenter(MainPresenter<IMainView> presenter) {
        return presenter;
    }
    @Provides
    @PerActivity
    IEditorPresenter<IEditorView> provideEditorPresenter(EditorPresenter<IEditorView> presenter) {
        return presenter;
    }
}
