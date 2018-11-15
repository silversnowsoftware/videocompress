package com.silversnowsoftware.vc.ui.list;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.ui.base.BaseActivity;
import com.silversnowsoftware.vc.ui.base.component.VideoCompressAdapter;
import com.silversnowsoftware.vc.utils.constants.Keys;

import java.util.ArrayList;
import java.util.List;

import static com.silversnowsoftware.vc.utils.SharedPref.getData;


public class ListActivity extends BaseActivity {


    ListView lvFileModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        lvFileModel = (ListView) findViewById(R.id.lvFileModel);


        List<FileModel> files = (List<FileModel>) getData(Keys.FILE_LIST_KEY, new TypeToken<ArrayList<FileModel>>() {
        }.getType(), getApplicationContext());

        VideoCompressAdapter videoCompressAdapter = new VideoCompressAdapter(getApplicationContext(), R.layout.file_model_list, files);
        lvFileModel = (ListView) findViewById(R.id.lvFileModel);
        lvFileModel.setAdapter(videoCompressAdapter);

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_list;
    }


}
