package com.silversnowsoftware.vc.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.ui.base.BasePresenter;
import com.silversnowsoftware.vc.utils.helpers.FileHelper;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by burak on 10/14/2018.
 */

public class MainPresenter<V extends IMainView> extends BasePresenter<V>
        implements IMainPresenter<V> {


    @Inject
    public MainPresenter() {
        super();
    }
    @Override
    public String TestInject() {

        return "Test Injectt";
    }

    private Intent openFile(Activity activity, File file) {
        Intent intent = null;
        try {
            intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            intent.setAction(Intent.ACTION_VIEW);

            String type = FileHelper.getMIMEType(file);

            intent.setDataAndType(/*uri*/Uri.fromFile(file), type);

            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity, R.string.dont_have_app_to_open_file, Toast.LENGTH_SHORT).show();
        }
        return intent;
    }
}
