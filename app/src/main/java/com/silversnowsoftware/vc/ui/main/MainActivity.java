package com.silversnowsoftware.vc.ui.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.databinding.ActivityMainBinding;
import com.silversnowsoftware.vc.ui.base.BaseActivity;
import com.silversnowsoftware.vc.ui.compression.permission.PermissionsActivity;
import com.silversnowsoftware.vc.ui.compression.permission.PermissionsChecker;
import com.silversnowsoftware.vc.ui.compression.videocompress.CompressListener;
import com.silversnowsoftware.vc.ui.compression.videocompress.Compressor;
import com.silversnowsoftware.vc.ui.compression.videocompress.InitListener;
import com.silversnowsoftware.vc.ui.compression.videorecord.CameraActivity;
import com.silversnowsoftware.vc.utils.ManifestUtil;
import com.silversnowsoftware.vc.utils.constants.Constants;
import com.silversnowsoftware.vc.utils.helpers.FileHelper;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import me.drakeet.materialdialog.MaterialDialog;

import static com.silversnowsoftware.vc.utils.helpers.FileHelper.getFileSize;


public class MainActivity extends BaseActivity implements IMainView {
    private final String TAG = getClass().getSimpleName();

    private Compressor mCompressor;
    ActivityMainBinding mBinding;
    //    private String currentInputVideoPath = "/mnt/sdcard/videokit/in.mp4";
    private String currentInputVideoPath = "";
    private String currentOutputVideoPath = "/mnt/sdcard/videokit/out.mp4";

    String cmd = "-y -i " + currentInputVideoPath + " -strict -2 -vcodec libx264 -preset ultrafast " +
            "-crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s 640x480 -aspect 16:9 " + currentOutputVideoPath;


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
        showToastMethod(mPresenter.TestInject());
        mBinding.btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraActivity.startActivityForResult(MainActivity.this, Constants.REQUEST_CODE_FOR_RECORD_VIDEO);
            }
        });

        mBinding.etCommand.setText(cmd);
        mBinding.btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String command = mBinding.etCommand.getText().toString();
                if (TextUtils.isEmpty(command)) {
                    Toast.makeText(MainActivity.this, getString(R.string.compree_please_input_command)
                            , Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(currentInputVideoPath)) {
                    Toast.makeText(MainActivity.this, R.string.no_video_tips, Toast.LENGTH_SHORT).show();
                } else {
                    File file = new File(currentOutputVideoPath);
                    if (file.exists()) {
                        file.delete();
                    }
                    execCommand(command);
                }
            }
        });
        mCompressor = new Compressor(this);
        mCompressor.loadBinary(new InitListener() {
            @Override
            public void onLoadSuccess() {
                Log.v(TAG, "load library succeed");
                textAppend(getString(R.string.compress_load_library_succeed));
            }

            @Override
            public void onLoadFail(String reason) {
                Log.i(TAG, "load library fail:" + reason);
                textAppend(getString(R.string.compress_load_library_failed, reason));
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


    private void execCommand(String cmd) {

        File mFile = new File(currentOutputVideoPath);
        if (mFile.exists()) {
            mFile.delete();
        }

        mCompressor.execCommand(cmd, new CompressListener() {
            @Override
            public void onExecSuccess(String message) {
                Log.i(TAG, "success " + message);
                textAppend(getString(R.string.compress_succeed));
                Toast.makeText(getApplicationContext(), R.string.compress_succeed, Toast.LENGTH_SHORT).show();
                String result = getString(R.string.compress_result_input_output, currentInputVideoPath
                        , getFileSize(currentInputVideoPath), currentOutputVideoPath, getFileSize(currentOutputVideoPath));
                textAppend(result);
                mMaterialDialog = new MaterialDialog(MainActivity.this)
                        .setTitle(getString(R.string.compress_succeed))
                        .setMessage(result)
                        .setPositiveButton(getString(R.string.open_video), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    startActivity(FileHelper.openFile(new File(currentOutputVideoPath)));
                                }catch(Exception ex) {
                                    showToastMethod(ex.getMessage());
                                };

                                mMaterialDialog.dismiss();
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                            }
                        });
                mMaterialDialog.show();

            }

            @Override
            public void onExecFail(String reason) {
                Log.i(TAG, "fail " + reason);
                textAppend(getString(R.string.compress_failed, reason));
                mMaterialDialog = new MaterialDialog(MainActivity.this)
                        .setTitle(getString(R.string.compress_failed))
                        .setMessage(getString(R.string.compress_failed))
                        .setPositiveButton(getString(R.string.confirm), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                            }
                        });
                mMaterialDialog.show();
            }

            @Override
            public void onExecProgress(String message) {
                Log.i(TAG, "progress " + message);
                textAppend(getString(R.string.compress_progress, message));
                Log.v(TAG, getString(R.string.compress_progress, getProgress(message)));


            }
        });
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
        if (requestCode == Constants.REQUEST_CODE_FOR_PERMISSIONS) {

            if (PermissionsActivity.PERMISSIONS_DENIED == resultCode) {

                finish();
            } else if (PermissionsActivity.PERMISSIONS_GRANTED == resultCode) {
                //权限被授予
                //do nothing
            }
        } else if (requestCode == Constants.REQUEST_CODE_FOR_RECORD_VIDEO) {

            if (resultCode == Constants.RESULT_CODE_FOR_RECORD_VIDEO_SUCCEED) {

                String videoPath = data.getStringExtra(Constants.INTENT_EXTRA_VIDEO_PATH);
                if (!TextUtils.isEmpty(videoPath)) {
                    currentInputVideoPath = videoPath;
                    MediaMetadataRetriever retr = new MediaMetadataRetriever();
                    retr.setDataSource(currentInputVideoPath);
                    String time = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    //7680
                    try {
                        videoLength = Double.parseDouble(time) / 1000.00;
                    } catch (Exception e) {
                        e.printStackTrace();
                        videoLength = 0.00;
                    }
                    Log.v(TAG, "videoLength = " + videoLength + "s");
                    refreshCurrentPath();
                }
            } else if (resultCode == Constants.RESULT_CODE_FOR_RECORD_VIDEO_FAILED) {

                currentInputVideoPath = "";
            }
        }

    }

    private void refreshCurrentPath() {
        mBinding.tvVideoFilePath.setText(getString(R.string.path, currentInputVideoPath, getFileSize(currentInputVideoPath)));
        cmd = "-y -i " + currentInputVideoPath + " -strict -2 -vcodec libx264 -preset ultrafast " +
                "-crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s 480x320 -aspect 16:9 " + currentOutputVideoPath;
        mBinding.etCommand.setText(cmd);
        mBinding.tvLog.setText("");
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
            Log.v(TAG, "current second = " + seconds);
            if (0 != videoLength) {
                return seconds / videoLength + "";
            }
            return "0";
        }
        return "";
    }

}
