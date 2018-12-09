package com.silversnowsoftware.vc.ui.splash;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.ui.base.BaseActivity;
import com.silversnowsoftware.vc.ui.main.MainActivity;

public class SplashActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                redirectToActivity(MainActivity.class);
                finish();
            }
        }, 3000);

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_splash;
    }


}
