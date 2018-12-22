package com.silversnowsoftware.vc.ui.editor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.googlecode.mp4parser.authoring.Edit;
import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.model.listener.OnVideoTrimListener;
import com.silversnowsoftware.vc.model.logger.LogModel;
import com.silversnowsoftware.vc.ui.base.BaseActivity;
import com.silversnowsoftware.vc.ui.base.BaseResponse;
import com.silversnowsoftware.vc.ui.list.ListActivity;
import com.silversnowsoftware.vc.utils.Utility;
import com.silversnowsoftware.vc.utils.constants.Constants;
import com.silversnowsoftware.vc.utils.constants.Globals;
import com.silversnowsoftware.vc.utils.helpers.LogManager;

import java.util.List;

import javax.inject.Inject;

import static com.silversnowsoftware.vc.utils.helpers.FileHelper.getVideoDuration;

public class EditorActivity extends BaseActivity implements IEditorView {

    private static final String className = EditorActivity.class.getSimpleName();
    @Inject
    IEditorPresenter<IEditorView> mPresenter;
    EditorViewHolder meditorViewHolder;
    FileModel result = null;

    OnVideoTrimListener mOnVideoTrimListener = new OnVideoTrimListener() {
        @Override
        public void onTrimStarted() {
            showProgressDialog(EditorActivity.this, getString(R.string.video_trim));
        }

        @Override
        public void getResult(Uri uri) {

            dismissProgressDialog();

            finish();

            result.setPath(uri.getPath());
            result.setVideoLength(Utility.ConvertToVideoTime(Integer.valueOf(String.valueOf(getVideoDuration(EditorActivity.this, result.getPath())))));

            mPresenter.updateFileModel(result);

            redirectToActivity(ListActivity.class);
        }

        @Override
        public void cancelAction() {
            mPresenter.removeLastVideo();
            dismissProgressDialog();
        }

        @Override
        public void onError(String message) {
            mPresenter.removeLastVideo();
            dismissProgressDialog();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        meditorViewHolder = new EditorViewHolder(this);
        getActivityComponent().inject(this);
        try {

            mPresenter.onAttach(this);
            MobileAds.initialize(this, "ca-app-pub-9069451453527664~1459246129");
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("B3E228E2A3DF6402D6DCF40712D066F6").build();
            meditorViewHolder.adViewEditor.loadAd(adRequest);
            meditorViewHolder.adViewEditor.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    Log.i("ADS:","onAdLoaded");
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    Log.i("ADS:","onAdFailedToLoad");
                }

                @Override
                public void onAdOpened() {
                    Log.i("ADS:","onAdOpened");
                }

                @Override
                public void onAdLeftApplication() {
                    Log.i("ADS:","onAdLeftApplication");
                }

                @Override
                public void onAdClosed() {
                    Log.i("ADS:","onAdClosed");
                }
            });




            mPresenter.setViewHolder();
            mPresenter.setExoPlayer();
            mPresenter.setVideoPrepared();
            mPresenter.customRangeSeekBarNewInit();
            mPresenter.seekBarVideoInit();



        } catch (Exception ex) {

            LogManager.Log(className, ex);
        }

        meditorViewHolder.btnCompress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    result = mPresenter.processFile();
                    if (!result.getIsCompress())
                        showToastMethod("Compress işlemi yapılmadı...");

                    mPresenter.addFileModel(result);

                    mPresenter.trimVideo(mOnVideoTrimListener);


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
}
