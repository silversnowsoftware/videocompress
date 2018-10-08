package com.silversnowsoftware.vc.ui.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends BaseActivity {

    @BindView(R.id.firstBtn)
    Button first_Button;

    @OnClick(R.id.firstBtn)
    void Selam() {
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
        return R.layout.activity_test;
    }
}
