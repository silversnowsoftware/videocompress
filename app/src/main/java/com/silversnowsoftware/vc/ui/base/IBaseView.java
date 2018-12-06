package com.silversnowsoftware.vc.ui.base;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;

/**
 * Created by burak on 10/14/2018.
 */

public interface IBaseView {
    void showLoading();

    void hideLoading();

    void openActivityOnTokenExpire();

    void onError(@StringRes int resId);

    void onError(String message);

    void showMessage(String message);

    void showMessage(@StringRes int resId);

    boolean isNetworkConnected();

    void hideKeyboard();

    void RedirectToActivity(Class<?> activityClass);


}
