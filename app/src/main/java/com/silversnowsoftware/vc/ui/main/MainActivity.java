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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import me.drakeet.materialdialog.MaterialDialog;

import static com.silversnowsoftware.vc.utils.helpers.FileHelper.getFileSize;


public class MainActivity extends BaseActivity implements IMainView {

    private Compressor mCompressor;
    ActivityMainBinding mBinding;
    private MaterialDialog mMaterialDialog;
    private Double videoLength = 0.00;
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
                   mPresenter.Compress(command);
                }
            }
        });
        mCompressor = new Compressor(this);
        mCompressor.loadBinary(new InitListener() {
            @Override
            public void onLoadSuccess() {

                textAppend(getString(R.string.compress_load_library_succeed));
            }

            @Override
            public void onLoadFail(String reason) {

                textAppend(getString(R.string.compress_load_library_failed, reason));
            }
        });


        PermissionsChecker mChecker = new PermissionsChecker(getApplicationContext());
        if (mChecker.lacksPermissions(ManifestUtil.PERMISSIONS)) {
            PermissionsActivity.startActivityForResult(this, Constants.REQUEST_CODE_FOR_PERMISSIONS, ManifestUtil.PERMISSIONS);
        }


    }
    private void execCommand(String cmd) {

        File mFile = new File(Globals.currentOutputVideoPath);
        if (mFile.exists()) {
            mFile.delete();
        }

        mCompressor.execCommand(cmd, new CompressListener() {
            @Override
            public void onExecSuccess(String message) {



            }

            @Override
            public void onExecFail(String reason) {


            }

            @Override
            public void onExecProgress(String message) {



            }
        });
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

        mPresenter.ActivityResult(requestCode,resultCode,data);
    }


    private String getProgress(String source) {
        //progress frame=   28 fps=0.0 q=24.0 size= 107kB time=00:00:00.91 bitrate= 956.4kbits/s
        Pattern p = Pattern.compile("00:\\d{2}:\\d{2}");
        Matcher m = p.matcher(source);
        if (m.find()) {
            //00:00:00
            String result = m.group(0);
            String temp[] = result.split(":");
            Double seconds = Double.parseDouble(temp[1]) * 60 + Double.parseDouble(temp[2]);

            if (0 != videoLength) {
                return seconds / videoLength + "";
            }
            return "0";
        }
        return "";
    }

}
