package com.silversnowsoftware.vc.ui.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.VideoCompressApplication;
import com.silversnowsoftware.vc.databinding.ActivityMainBinding;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.ui.base.BaseActivity;
import com.silversnowsoftware.vc.ui.compression.permission.PermissionsActivity;
import com.silversnowsoftware.vc.ui.compression.permission.PermissionsChecker;
import com.silversnowsoftware.vc.ui.compression.videocompress.CompressListener;
import com.silversnowsoftware.vc.ui.compression.videocompress.Compressor;
import com.silversnowsoftware.vc.ui.compression.videocompress.InitListener;
import com.silversnowsoftware.vc.ui.compression.videorecord.CameraActivity;
import com.silversnowsoftware.vc.utils.ManifestUtil;
import com.silversnowsoftware.vc.utils.constants.Constants;
import com.silversnowsoftware.vc.utils.constants.Globals;
import com.silversnowsoftware.vc.utils.helpers.FileHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import me.drakeet.materialdialog.MaterialDialog;

import static com.silversnowsoftware.vc.utils.helpers.FileHelper.getFileSize;


public class MainActivity extends BaseActivity implements IMainView {


    ActivityMainBinding mBinding;
    private static final Handler handler = new Handler();

    @Inject
    IMainPresenter<IMainView> mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, getLayoutResourceId());
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);

        mBinding.btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraActivity.startActivityForResult(MainActivity.this, Constants.REQUEST_CODE_FOR_RECORD_VIDEO);
            }
        });

        mBinding.etCommand.setText(mPresenter.getCmd());
        mBinding.btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String command = mPresenter.getCmd();
                if (TextUtils.isEmpty(command)) {
                    Toast.makeText(MainActivity.this, getString(R.string.compree_please_input_command)
                            , Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(Globals.currentInputVideoPath)) {
                    Toast.makeText(MainActivity.this, R.string.no_video_tips, Toast.LENGTH_SHORT).show();
                } else {
                    File file = new File(Globals.currentOutputVideoPath);
                    if (file.exists()) {
                        file.delete();
                    }
                        mPresenter.VideoCompress();
                }
            }
        });


        PermissionsChecker mChecker = new PermissionsChecker(getApplicationContext());
        if (mChecker.lacksPermissions(ManifestUtil.PERMISSIONS)) {
            PermissionsActivity.startActivityForResult(this, Constants.REQUEST_CODE_FOR_PERMISSIONS, ManifestUtil.PERMISSIONS);
        }


    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }


    private void textAppend(String text) {
        if (!TextUtils.isEmpty(text)) {
            mBinding.tvLog.append(text + "\n");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mBinding.scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mPresenter.ActivityResult(requestCode, resultCode, data);
    }


}
