package com.silversnowsoftware.vc.ui.test;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;


import com.google.gson.reflect.TypeToken;
import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.ui.base.BaseActivity;
import com.silversnowsoftware.vc.ui.base.component.VideoCompressAdapter;
import com.silversnowsoftware.vc.utils.constants.Keys;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

import static com.silversnowsoftware.vc.utils.SharedPref.getData;

public class TestActivity extends BaseActivity {




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

       // setDefaults("key1", "selam", getApplicationContext());
        // Intent homepage = new Intent(HomeActivity.this, ShowActivity.class);
        //startActivity(homepage);

        ButterKnife.bind(this);



    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_test;
    }
}
