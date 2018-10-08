package com.czh.testmpeg;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.czh.testmpeg.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.firstBtn)
    Button first_Button;
    @OnClick(R.id.firstBtn) void Selam(){
        showToastMethod("Selam");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        setDefaults("key1", "selam", getApplicationContext());
       // Intent homepage = new Intent(HomeActivity.this, ShowActivity.class);
        //startActivity(homepage);

        ButterKnife.bind(this);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home;
    }
}