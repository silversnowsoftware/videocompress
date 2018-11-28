package com.silversnowsoftware.vc.ui.test;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;


import com.google.gson.reflect.TypeToken;
import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.ui.base.BaseActivity;
import com.silversnowsoftware.vc.ui.base.component.VideoCompressAdapter;
import com.silversnowsoftware.vc.utils.Types;
import com.silversnowsoftware.vc.utils.constants.Keys;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.silversnowsoftware.vc.utils.SharedPref.getData;


public class TestActivity extends BaseActivity {


    @BindView(R.id.VideoView)
    public VideoView vv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

       // setDefaults("key1", "selam", getApplicationContext());
        // Intent homepage = new Intent(HomeActivity.this, ShowActivity.class);
        //startActivity(homepage);

        ButterKnife.bind(this);

        List<FileModel> fileModelList = (List<FileModel>) getData(Keys.FILE_LIST_KEY, Types.getFileModelListType(), getApplicationContext());
        String srcFile = fileModelList.get(fileModelList.size()-1).getPath();
        vv.setVideoURI(Uri.parse(srcFile));
        vv.start();


    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_test;
    }
}
