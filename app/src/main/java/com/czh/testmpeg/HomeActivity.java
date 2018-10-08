package com.czh.testmpeg;

import android.content.Intent;
import android.os.Bundle;

import com.czh.testmpeg.base.BaseActivity;

public class HomeActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        setDefaults("key1", "selam", getApplicationContext());
        Intent homepage = new Intent(HomeActivity.this, ShowActivity.class);
        startActivity(homepage);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home;
    }
}
