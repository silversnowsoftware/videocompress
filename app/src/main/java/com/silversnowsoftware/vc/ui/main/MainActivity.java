package com.silversnowsoftware.vc.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ProgressBar;


import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.ui.base.BaseActivity;
import com.silversnowsoftware.vc.ui.compression.permission.PermissionsActivity;
import com.silversnowsoftware.vc.ui.compression.permission.PermissionsChecker;
import com.silversnowsoftware.vc.ui.list.ListActivity;
import com.silversnowsoftware.vc.utils.ManifestUtil;
import com.silversnowsoftware.vc.utils.constants.Constants;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.silversnowsoftware.vc.utils.constants.Globals.handler;


public class MainActivity extends BaseActivity implements IMainView {


    @BindView(R.id.btnRecord)
    Button btnRecord;
    @BindView(R.id.btnRun)
    Button btnRun;
    @BindView(R.id.btnChoose)
    Button btnChoose;
    @BindView(R.id.btnListFile)
    Button btnListFile;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    static ArrayList<String> MediasPaths = new ArrayList<>();



    @Inject
    IMainPresenter<IMainView> mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        handler = new Handler();
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        //progressBar.setMax(100);

        PermissionsChecker mChecker = new PermissionsChecker(getApplicationContext());
        if (mChecker.lacksPermissions(ManifestUtil.PERMISSIONS)) {
            PermissionsActivity.startActivityForResult(this, Constants.REQUEST_CODE_FOR_PERMISSIONS, ManifestUtil.PERMISSIONS);
        }
        ButterKnife.bind(this);

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mPresenter.collectFiles(data);
            //startActivity(new Intent(getApplicationContext(), ListActivity.class));
        }
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
        startActivityForResult(mediaIntent, 1);
    }

    @OnClick(R.id.btnListFile)
    void btnListFile_onClick() {
        Intent listActivity = new Intent(getApplicationContext(),ListActivity.class);
        startActivity(listActivity);
    }
}
