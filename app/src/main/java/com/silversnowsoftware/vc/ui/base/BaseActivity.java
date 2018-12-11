package com.silversnowsoftware.vc.ui.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.silversnowsoftware.vc.VideoCompressApplication;
import com.silversnowsoftware.vc.di.component.ActivityComponent;
import com.silversnowsoftware.vc.di.component.DaggerActivityComponent;
import com.silversnowsoftware.vc.di.module.ActivityModule;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.ui.editor.EditorActivity;
import com.silversnowsoftware.vc.ui.main.MainActivity;
import com.silversnowsoftware.vc.utils.SharedPref;
import com.silversnowsoftware.vc.utils.Types;
import com.silversnowsoftware.vc.utils.constants.Globals;
import com.silversnowsoftware.vc.utils.constants.Keys;

import java.util.List;

/**
 * Created by burak on 10/8/2018.
 */

public class BaseActivity extends AppCompatActivity implements IBaseView {

    private ActivityComponent mActivityComponent;
    private ProgressDialog mProgressDialog;

    private Context getContext() {
        return getApplicationContext();
    }

    protected  List<FileModel> getSelectedFiles() {
        return Globals.selectedFiles;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((VideoCompressApplication) getApplication()).getComponent())
                .build();

    }

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    protected int getLayoutResourceId() {
        return 0;
    }

    public void showToastMethod(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void redirectToActivity(final Class<?> activityClass) {
        Intent intent = new Intent(getContext(), activityClass);
        startActivity(intent);

    }

    @Override
    public void showProgressDialog(Context context, String title) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle(title);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }


}
