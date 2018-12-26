package com.silversnowsoftware.vc.ui.editor;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.model.listener.ICustomListener;
import com.silversnowsoftware.vc.model.listener.OnVideoTrimListener;
import com.silversnowsoftware.vc.operations.compressor.FileCompressor;
import com.silversnowsoftware.vc.ui.base.BaseActivity;
import com.silversnowsoftware.vc.ui.list.ListActivity;
import com.silversnowsoftware.vc.ui.main.MainActivity;
import com.silversnowsoftware.vc.utils.Utility;
import com.silversnowsoftware.vc.utils.constants.Globals;
import com.silversnowsoftware.vc.utils.enums.FileStatusEnum;
import com.silversnowsoftware.vc.utils.helpers.LogManager;

import javax.inject.Inject;

import static com.silversnowsoftware.vc.utils.helpers.FileHelper.getVideoDuration;

public class EditorActivity extends BaseActivity implements IEditorView {

    private static final String className = EditorActivity.class.getSimpleName();
    @Inject
    IEditorPresenter<IEditorView> mPresenter;
    EditorViewHolder meditorViewHolder;
    FileModel fileModel = null;

    OnVideoTrimListener mOnVideoTrimListener = new OnVideoTrimListener() {
        @Override
        public void onTrimStarted() {
            mPresenter.showEditorProgressDialog(getFragmentManager());
        }

        @Override
        public void getResult(Uri uri) {
            fileModel.setFileStatus(FileStatusEnum.SUCCESS);
            fileModel.setPath(uri.getPath());
            fileModel.setVideoLength(Utility.ConvertToVideoTime(Integer.valueOf(String.valueOf(getVideoDuration(EditorActivity.this, fileModel.getPath())))));
            mPresenter.updateFileModel(fileModel);

            //  mPresenter.dismissEditorProgressDialog();
           // finish();
            //if (!fileModel.getIsCompress())
              //      redirectToActivity(ListActivity.class);
        }

        @Override
        public void cancelAction() {
            fileModel.setFileStatus(FileStatusEnum.CANCELED);
            mPresenter.updateFileModel(fileModel);
            mPresenter.dismissEditorProgressDialog();
            redirectToMainActivity();
        }

        @Override
        public void onError(String message) {
            fileModel.setFileStatus(FileStatusEnum.ERROR);
            mPresenter.updateFileModel(fileModel);
            mPresenter.dismissEditorProgressDialog();
            redirectToMainActivity();
        }
    };
    ICustomListener mCustomListener = new ICustomListener() {
        @Override
        public void onSuccess(Double rate) {

            fileModel.setFileStatus(FileStatusEnum.SUCCESS);
            fileModel.setVideoLength(Utility.ConvertToVideoTime(Integer.valueOf(String.valueOf(getVideoDuration(EditorActivity.this, fileModel.getPath())))));
            fileModel.setPath(Globals.currentOutputVideoPath + fileModel.getName());
            mPresenter.updateFileModel(fileModel);
        }

        @Override
        public void onProgress(Double rate) {
            if (rate > 0) {

            }
        }

        @Override
        public void onFailure(String error) {

            fileModel.setFileStatus(FileStatusEnum.ERROR);
            fileModel.setPath(Globals.currentOutputVideoPath + fileModel.getName());
            fileModel.setVideoLength(Utility.ConvertToVideoTime(Integer.valueOf(String.valueOf(getVideoDuration(EditorActivity.this, fileModel.getPath())))));
            mPresenter.updateFileModel(fileModel);

            showToastMethod(getString(R.string.compression_failed));
            redirectToMainActivity();

        }
    };
    AdListener mAdListener = new AdListener() {

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        meditorViewHolder = new EditorViewHolder(this);
        getActivityComponent().inject(this);

        meditorViewHolder.btnCompress.setAlpha(.5f);
        meditorViewHolder.btnClear.setAlpha(.5f);
        meditorViewHolder.btnCompress.setEnabled(false);
        meditorViewHolder.btnClear.setEnabled(false);

        try {

            mPresenter.onAttach(this);
            MobileAds.initialize(this, "ca-app-pub-9069451453527664~1459246129");
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("B3E228E2A3DF6402D6DCF40712D066F6").build();
            meditorViewHolder.adViewEditor.loadAd(adRequest);
            meditorViewHolder.adViewEditor.setAdListener(mAdListener);


            mPresenter.setViewHolder();
            mPresenter.setExoPlayer();
            mPresenter.customRangeSeekBarNewInit();
            mPresenter.seekBarVideoInit();


        } catch (Exception ex) {

            LogManager.Log(className, ex);
        }

        meditorViewHolder.btnCompress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    fileModel = mPresenter.processFile();

                    if (!fileModel.getIsCompress() && !fileModel.getIsCrop()) {
                        alertDialog(EditorActivity.this, getString(R.string.Alert), getString(R.string.not_crop_or_compress));
                        return;
                    }

                    mPresenter.addFileModel(fileModel);
                    if (fileModel.getIsCrop())
                        mPresenter.trimVideo(mOnVideoTrimListener);

                    if (fileModel.getIsCompress()) {
                        if (fileModel.getFileStatus() == FileStatusEnum.PREPEARING) {
                            fileModel.setFileStatus(FileStatusEnum.PROGRESSING);
                            FileCompressor fc = new FileCompressor(EditorActivity.this);
                            fc.Compress(fileModel);
                            fileModel.getCustomListener(mCustomListener);
                        }else if (fileModel.getFileStatus() == FileStatusEnum.CANCELED ||
                                  fileModel.getFileStatus() == FileStatusEnum.ERROR ||
                                  fileModel.getFileStatus() == FileStatusEnum.NONE){

                            redirectToMainActivity();
                        }
                    }

                } catch (Exception ex) {

                    LogManager.Log(className, ex);
                }

            }
        });

        meditorViewHolder.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.setDefaultEditor();
            }
        });

        meditorViewHolder.imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.playPauseVideo();
            }
        });

        meditorViewHolder.exoPlayerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mPresenter.playPauseVideo();
                return false;
            }
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_editor;
    }

    private void redirectToMainActivity() {
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    redirectToActivity(MainActivity.class);

                }
            }, 2000);
        } catch (Exception ex) {

            LogManager.Log(className, ex);
        }
    }
}
