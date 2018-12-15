package com.silversnowsoftware.vc.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.ProgressBar;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.ui.base.IBaseViewHolder;
import com.silversnowsoftware.vc.ui.list.ListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hakan on 17.11.2018.
 */

public class MainViewHolder {
    Activity mView;

    @BindView(R.id.btnRecord)
    Button btnRecord;
    @BindView(R.id.btnChoose)
    Button btnChoose;
    @BindView(R.id.btnListFile)
    Button btnListFile;


    public MainViewHolder(Activity activity) {
        mView = (Activity) activity;
        ButterKnife.bind(this,  mView);
    }


    @OnClick(R.id.btnChoose)
    void btnChoose_onClick() {
        Intent mediaIntent = new Intent(
                Intent.ACTION_GET_CONTENT
                //,Uri.parse(Environment.DIRECTORY_DCIM)
        );
        // mediaIntent.setType("*/*");
        mediaIntent.setType("video/*");
        mediaIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"video/*"});
        mediaIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        mView.startActivityForResult(mediaIntent, 1);
    }

    @OnClick(R.id.btnListFile)
    void btnListFile_onClick() {
        Intent listActivity = new Intent(mView,ListActivity.class);
        mView.startActivity(listActivity);
    }
}
