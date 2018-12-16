package com.silversnowsoftware.vc.ui.splash;

import android.os.Bundle;
import android.os.Handler;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.logger.LogModel;
import com.silversnowsoftware.vc.ui.base.BaseActivity;
import com.silversnowsoftware.vc.ui.main.MainActivity;
import com.silversnowsoftware.vc.utils.Utility;
import com.silversnowsoftware.vc.utils.constants.Constants;
import com.silversnowsoftware.vc.utils.helpers.LogHelper;

public class SplashActivity extends BaseActivity {
    private static final String className = SplashActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    redirectToActivity(MainActivity.class);
                    finish();
                }
            }, 3000);
        } catch (Exception ex) {

            LogHelper.Log(className, ex);
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_splash;
    }


}
