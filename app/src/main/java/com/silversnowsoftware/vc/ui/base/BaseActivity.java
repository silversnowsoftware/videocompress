package com.silversnowsoftware.vc.ui.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.VideoCompressApplication;
import com.silversnowsoftware.vc.di.component.ActivityComponent;
import com.silversnowsoftware.vc.di.component.DaggerActivityComponent;
import com.silversnowsoftware.vc.di.module.ActivityModule;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.ui.editor.EditorDialogFragment;
import com.silversnowsoftware.vc.ui.main.MainActivity;
import com.silversnowsoftware.vc.utils.constants.Constants;
import com.silversnowsoftware.vc.utils.constants.Globals;
import com.silversnowsoftware.vc.utils.helpers.LogManager;

import java.util.List;

import retrofit.RestAdapter;

/**
 * Created by burak on 10/8/2018.
 */

public class BaseActivity extends AppCompatActivity implements IBaseView {

    private ActivityComponent mActivityComponent;
    private ProgressDialog mProgressDialog;
    private ProgressDialog progress;

    protected Context getContext() {
        return getApplicationContext();
    }

    protected List<FileModel> getSelectedFiles() {
        return Globals.selectedFiles;
    }

    protected RestAdapter restAdapter;

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

    public void redirectToActivity(final Class<?> activityClass, long delayMillis) {
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getContext(), activityClass);
                    redirectToActivity(MainActivity.class);
                    startActivity(intent);

                }
            }, delayMillis);
        } catch (Exception ex) {


        }

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

    public void alertDialog(final Activity context, final String title, final String message) {

        if (!isFinishing()) {
            new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    public ProgressDialog progressBarDialog(Context context) {
        progress = new ProgressDialog(context);
        progress.setMessage(getString(R.string.progressing));
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(false);
        progress.setCancelable(false);
        progress.setProgress(0);
        return progress;
    }

}
