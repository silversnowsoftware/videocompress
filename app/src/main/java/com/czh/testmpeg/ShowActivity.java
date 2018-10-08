package com.czh.testmpeg;

import android.os.Bundle;

import com.czh.testmpeg.base.BaseActivity;

public class ShowActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        showToastMethod(getDefaults("key1", getApplicationContext()));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_show;
    }
}
