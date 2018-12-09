package com.silversnowsoftware.vc.ui.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.StringRes;

/**
 * Created by burak on 10/14/2018.
 */

public interface IBaseView {


    void showToastMethod(String message);

    void redirectToActivity(Class<?> activityClass);

    void showProgressDialog(Context context, String title);

    void dismissProgressDialog();

}
