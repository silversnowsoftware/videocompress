package com.silversnowsoftware.vc.ui.main;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.ui.base.BaseActivity;

import javax.inject.Inject;

public class Main2Activity extends BaseActivity implements IMainView {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main2;
    }
}
