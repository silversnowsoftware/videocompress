package com.silversnowsoftware.vc.ui.trimmer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.model.customvideoviews.BackgroundTask;
import com.silversnowsoftware.vc.model.customvideoviews.BarThumb;
import com.silversnowsoftware.vc.model.customvideoviews.CustomRangeSeekBar;
import com.silversnowsoftware.vc.model.customvideoviews.TileView;
import com.silversnowsoftware.vc.model.listener.OnRangeSeekBarChangeListener;
import com.silversnowsoftware.vc.model.listener.OnVideoTrimListener;
import com.silversnowsoftware.vc.model.logger.LogModel;
import com.silversnowsoftware.vc.utils.Types;
import com.silversnowsoftware.vc.utils.Utility;
import com.silversnowsoftware.vc.utils.constants.Constants;
import com.silversnowsoftware.vc.utils.constants.Keys;
import com.silversnowsoftware.vc.utils.helpers.LogHelper;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.silversnowsoftware.vc.utils.SharedPref.getData;
import static com.silversnowsoftware.vc.utils.helpers.FileHelper.getVideoDuration;


public class VideoTimmerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String className = VideoTimmerActivity.class.getSimpleName();
    private TextView txtVideoCancel;
    private TextView txtVideoUpload;
    private TextView txtVideoEditTitle;
    private TextView txtVideoTrimSeconds;
    private RelativeLayout rlVideoView;
    private TileView tileView;
    private CustomRangeSeekBar mCustomRangeSeekBarNew;
    private ImageView imgPlay;
    private SeekBar seekBarVideo;
    private TextView txtVideoLength;
    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;

    private int mDuration = 0;
    private int mTimeVideo = 0;
    private int mStartPosition = 0;
    private int mEndPosition = 0;
    // set your max video trim seconds
    private int mMaxDuration = 60;
    private Handler mHandler = new Handler();

    private ProgressDialog mProgressDialog;
    String srcFile;
    String dstFile;
    OnVideoTrimListener mOnVideoTrimListener = new OnVideoTrimListener() {
        @Override
        public void onTrimStarted() {
            // Create an indeterminate progress dialog
            mProgressDialog = new ProgressDialog(VideoTimmerActivity.this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setTitle("Saving....");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        public void getResult(Uri uri) {
            mProgressDialog.dismiss();
            Bundle conData = new Bundle();
            conData.putString("INTENT_VIDEO_FILE", uri.getPath());
            Intent intent = new Intent();
            intent.putExtras(conData);
            setResult(RESULT_OK, intent);
            finish();
        }

        @Override
        public void cancelAction() {
            mProgressDialog.dismiss();
        }

        @Override
        public void onError(String message) {
            mProgressDialog.dismiss();
        }
    };
    List<FileModel> fileModelList = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_timmer);
        try {


            fileModelList = (List<FileModel>) getData(Keys.FILE_LIST_KEY, Types.getFileModelListType(), getApplicationContext());
            txtVideoCancel = (TextView) findViewById(R.id.txtVideoCancel);
            txtVideoUpload = (TextView) findViewById(R.id.txtVideoUpload);
            txtVideoEditTitle = (TextView) findViewById(R.id.txtVideoEditTitle);
            txtVideoTrimSeconds = (TextView) findViewById(R.id.txtVideoTrimSeconds);
            rlVideoView = (RelativeLayout) findViewById(R.id.llVideoView);
            tileView = (TileView) findViewById(R.id.timeLineView);
            mCustomRangeSeekBarNew = ((CustomRangeSeekBar) findViewById(R.id.timeLineBar));
            imgPlay = (ImageView) findViewById(R.id.imgPlay);
            seekBarVideo = (SeekBar) findViewById(R.id.seekBarVideo);
            txtVideoLength = (TextView) findViewById(R.id.txtVideoLength);
            exoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exoVideoView);

            srcFile = fileModelList.get(0).getPath();

            dstFile = Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + new Date().getTime()
                    + Utility.VIDEO_FORMAT;


            tileView.post(new Runnable() {
                @Override
                public void run() {

                    setBitmap(Uri.parse(srcFile));
                    setExoPlayer();
                }
            });

            txtVideoCancel.setOnClickListener(this);
            txtVideoUpload.setOnClickListener(this);


            // handle your range seekbar changes
            mCustomRangeSeekBarNew.addOnRangeSeekBarListener(new OnRangeSeekBarChangeListener() {
                @Override
                public void onCreate(CustomRangeSeekBar customRangeSeekBarNew, int index, float value) {
                    // Do nothing
                }

                @Override
                public void onSeek(CustomRangeSeekBar customRangeSeekBarNew, int index, float value) {
                    onSeekThumbs(index, value);
                }

                @Override
                public void onSeekStart(CustomRangeSeekBar customRangeSeekBarNew, int index, float value) {
                    if (exoPlayer != null) {
                        mHandler.removeCallbacks(mUpdateTimeTask);
                        seekBarVideo.setProgress(0);
                        exoPlayer.seekTo(mStartPosition * 1000);
                        exoPlayer.setPlayWhenReady(false);
                        imgPlay.setBackgroundResource(R.drawable.ic_white_play);
                    }
                }

                @Override
                public void onSeekStop(CustomRangeSeekBar customRangeSeekBarNew, int index, float value) {

                }
            });

            imgPlay.setOnClickListener(this);


            seekBarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    if (exoPlayer != null) {
                        mHandler.removeCallbacks(mUpdateTimeTask);
                        seekBarVideo.setMax(mTimeVideo * 1000);
                        seekBarVideo.setProgress(0);
                        exoPlayer.seekTo(mStartPosition * 1000);
                        exoPlayer.setPlayWhenReady(false);
                        imgPlay.setBackgroundResource(R.drawable.ic_white_play);
                    }
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mHandler.removeCallbacks(mUpdateTimeTask);
                    exoPlayer.seekTo((mStartPosition * 1000) - seekBarVideo.getProgress());
                }
            });
        } catch (Exception e) {

            LogModel logModel = new LogModel.LogBuilder()
                    .apiVersion(Utility.getAndroidVersion())
                    .appName(Constants.APP_NAME)
                    .className(className)
                    .errorMessage(e.getMessage())
                    .methodName(e.getStackTrace()[0].getMethodName())
                    .stackTrace(e.getStackTrace().toString())
                    .build();
            LogHelper logHelper = new LogHelper();
            logHelper.Log(logModel);
        }

    }

    public void setExoPlayer() {
        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), "exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(
                    Uri.parse(srcFile),
                    dataSourceFactory,
                    new DefaultExtractorsFactory(),
                    null,
                    null);

            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            onVideoPrepared();
            //  exoPlayer.setPlayWhenReady(true);
        } catch (Exception e) {

            LogModel logModel = new LogModel.LogBuilder()
                    .apiVersion(Utility.getAndroidVersion())
                    .appName(Constants.APP_NAME)
                    .className(className)
                    .errorMessage(e.getMessage())
                    .methodName(e.getStackTrace()[0].getMethodName())
                    .stackTrace(e.getStackTrace().toString())
                    .build();
            LogHelper logHelper = new LogHelper();
            logHelper.Log(logModel);
        }

    }

    @Override
    public void onClick(View view) {
        try {
            if (view == txtVideoCancel) {
                finish();
            } else if (view == txtVideoUpload) {
                int diff = mEndPosition - mStartPosition;
                if (diff < 3) {
                    Toast.makeText(VideoTimmerActivity.this, getString(R.string.video_length_validation),
                            Toast.LENGTH_LONG).show();
                } else {
                    MediaMetadataRetriever
                            mediaMetadataRetriever = new MediaMetadataRetriever();
                    mediaMetadataRetriever.setDataSource(VideoTimmerActivity.this, Uri.parse(srcFile));
                    final File file = new File(srcFile);

                    //notify that video trimming started
                    if (mOnVideoTrimListener != null)
                        mOnVideoTrimListener.onTrimStarted();

                    BackgroundTask.execute(
                            new BackgroundTask.Task("", 0L, "") {
                                @Override
                                public void execute() {
                                    try {
                                        Utility.startTrim(file, dstFile, mStartPosition * 1000, mEndPosition * 1000, mOnVideoTrimListener);
                                    } catch (final Throwable e) {
                                        Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                                    }
                                }
                            }
                    );
                }

            } else if (view == imgPlay) {
                if (exoPlayer.getPlayWhenReady()) {
                    if (exoPlayer != null) {
                        exoPlayer.setPlayWhenReady(false);
                        imgPlay.setBackgroundResource(R.drawable.ic_white_play);
                    }
                } else {
                    if (exoPlayer != null) {
                        exoPlayer.setPlayWhenReady(true);
                        imgPlay.setBackgroundResource(R.drawable.ic_white_pause);
                        int prgrss = seekBarVideo.getProgress();
                        if (prgrss >= 0) {
                            txtVideoLength.setText("00:0" + (prgrss / 1000));
                            updateProgressBar();
                        }
                    }
                }
            }
        } catch (Exception e) {

            LogModel logModel = new LogModel.LogBuilder()
                    .apiVersion(Utility.getAndroidVersion())
                    .appName(Constants.APP_NAME)
                    .className(className)
                    .errorMessage(e.getMessage())
                    .methodName(e.getStackTrace()[0].getMethodName())
                    .stackTrace(e.getStackTrace().toString())
                    .build();
            LogHelper logHelper = new LogHelper();
            logHelper.Log(logModel);
        }
    }

    private void setBitmap(Uri mVideoUri) {
        try {
            tileView.setVideo(mVideoUri);
        } catch (Exception e) {

            LogModel logModel = new LogModel.LogBuilder()
                    .apiVersion(Utility.getAndroidVersion())
                    .appName(Constants.APP_NAME)
                    .className(className)
                    .errorMessage(e.getMessage())
                    .methodName(e.getStackTrace()[0].getMethodName())
                    .stackTrace(e.getStackTrace().toString())
                    .build();
            LogHelper logHelper = new LogHelper();
            logHelper.Log(logModel);
        }
    }

    private void onVideoPrepared() {
        try {
            int duration = getVideoDuration(this, srcFile);
            mDuration = (int) (duration / 1000);
            setSeekBarPosition();
        } catch (Exception e) {

            LogModel logModel = new LogModel.LogBuilder()
                    .apiVersion(Utility.getAndroidVersion())
                    .appName(Constants.APP_NAME)
                    .className(className)
                    .errorMessage(e.getMessage())
                    .methodName(e.getStackTrace()[0].getMethodName())
                    .stackTrace(e.getStackTrace().toString())
                    .build();
            LogHelper logHelper = new LogHelper();
            logHelper.Log(logModel);
        }
    }

    public void updateProgressBar() {
        try {
            mHandler.postDelayed(mUpdateTimeTask, 100);
        } catch (Exception e) {

            LogModel logModel = new LogModel.LogBuilder()
                    .apiVersion(Utility.getAndroidVersion())
                    .appName(Constants.APP_NAME)
                    .className(className)
                    .errorMessage(e.getMessage())
                    .methodName(e.getStackTrace()[0].getMethodName())
                    .stackTrace(e.getStackTrace().toString())
                    .build();
            LogHelper logHelper = new LogHelper();
            logHelper.Log(logModel);
        }
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            try {


                if (seekBarVideo.getProgress() >= seekBarVideo.getMax()) {
                    seekBarVideo.setProgress((int) (exoPlayer.getCurrentPosition() - mStartPosition * 1000));
                    txtVideoLength.setText(milliSecondsToTimer(seekBarVideo.getProgress()) + "");
                    exoPlayer.seekTo(mStartPosition * 1000);
                    exoPlayer.setPlayWhenReady(false);
                    seekBarVideo.setProgress(0);
                    txtVideoLength.setText("00:00");
                    imgPlay.setBackgroundResource(R.drawable.ic_white_play);
                } else {
                    seekBarVideo.setProgress((int) (exoPlayer.getCurrentPosition() - mStartPosition * 1000));
                    txtVideoLength.setText(milliSecondsToTimer(seekBarVideo.getProgress()) + "");
                    mHandler.postDelayed(this, 100);
                }
            } catch (Exception e) {

                LogModel logModel = new LogModel.LogBuilder()
                        .apiVersion(Utility.getAndroidVersion())
                        .appName(Constants.APP_NAME)
                        .className(className)
                        .errorMessage(e.getMessage())
                        .methodName(e.getStackTrace()[0].getMethodName())
                        .stackTrace(e.getStackTrace().toString())
                        .build();
                LogHelper logHelper = new LogHelper();
                logHelper.Log(logModel);
            }
        }
    };

    private void setSeekBarPosition() {
        try {
            if (mDuration >= mMaxDuration) {
                mStartPosition = 0;
                mEndPosition = mMaxDuration;

                mCustomRangeSeekBarNew.setThumbValue(0, (mStartPosition * 100) / mDuration);
                mCustomRangeSeekBarNew.setThumbValue(1, (mEndPosition * 100) / mDuration);

            } else {
                mStartPosition = 0;
                mEndPosition = mDuration;
            }


            mTimeVideo = mDuration;
            mCustomRangeSeekBarNew.initMaxWidth();
            seekBarVideo.setMax(mMaxDuration * 1000);
            exoPlayer.seekTo(mStartPosition * 1000);

            String mStart = mStartPosition + "";
            if (mStartPosition < 10)
                mStart = "0" + mStartPosition;

            int startMin = Integer.parseInt(mStart) / 60;
            int startSec = Integer.parseInt(mStart) % 60;

            String mEnd = mEndPosition + "";
            if (mEndPosition < 10)
                mEnd = "0" + mEndPosition;

            int endMin = Integer.parseInt(mEnd) / 60;
            int endSec = Integer.parseInt(mEnd) % 60;

            txtVideoTrimSeconds.setText(String.format(Locale.US, "%02d:%02d - %02d:%02d", startMin, startSec, endMin, endSec));
        } catch (Exception e) {

            LogModel logModel = new LogModel.LogBuilder()
                    .apiVersion(Utility.getAndroidVersion())
                    .appName(Constants.APP_NAME)
                    .className(className)
                    .errorMessage(e.getMessage())
                    .methodName(e.getStackTrace()[0].getMethodName())
                    .stackTrace(e.getStackTrace().toString())
                    .build();
            LogHelper logHelper = new LogHelper();
            logHelper.Log(logModel);
        }
    }

    /**
     * called when playing video completes
     */
    private void onVideoCompleted() {
        try {
            mHandler.removeCallbacks(mUpdateTimeTask);
            seekBarVideo.setProgress(0);
            exoPlayer.seekTo(mStartPosition * 1000);
            exoPlayer.setPlayWhenReady(false);
            imgPlay.setBackgroundResource(R.drawable.ic_white_play);
        } catch (Exception e) {

            LogModel logModel = new LogModel.LogBuilder()
                    .apiVersion(Utility.getAndroidVersion())
                    .appName(Constants.APP_NAME)
                    .className(className)
                    .errorMessage(e.getMessage())
                    .methodName(e.getStackTrace()[0].getMethodName())
                    .stackTrace(e.getStackTrace().toString())
                    .build();
            LogHelper logHelper = new LogHelper();
            logHelper.Log(logModel);
        }
    }

    /**
     * Handle changes of left and right thumb movements
     *
     * @param index index of thumb
     * @param value value
     */
    private void onSeekThumbs(int index, float value) {
        try {
            switch (index) {
                case BarThumb.LEFT: {
                    mStartPosition = (int) ((mDuration * value) / 100L);
                    exoPlayer.seekTo(mStartPosition * 1000);
                    break;
                }
                case BarThumb.RIGHT: {
                    mEndPosition = (int) ((mDuration * value) / 100L);
                    break;
                }
            }
            mTimeVideo = (mEndPosition - mStartPosition);
            seekBarVideo.setMax(mTimeVideo * 1000);
            seekBarVideo.setProgress(0);
            exoPlayer.seekTo(mStartPosition * 1000);

            String mStart = mStartPosition + "";
            if (mStartPosition < 10)
                mStart = "0" + mStartPosition;

            int startMin = Integer.parseInt(mStart) / 60;
            int startSec = Integer.parseInt(mStart) % 60;

            String mEnd = mEndPosition + "";
            if (mEndPosition < 10)
                mEnd = "0" + mEndPosition;
            int endMin = Integer.parseInt(mEnd) / 60;
            int endSec = Integer.parseInt(mEnd) % 60;

            txtVideoTrimSeconds.setText(String.format(Locale.US, "%02d:%02d - %02d:%02d", startMin, startSec, endMin, endSec));
        } catch (Exception e) {

            LogModel logModel = new LogModel.LogBuilder()
                    .apiVersion(Utility.getAndroidVersion())
                    .appName(Constants.APP_NAME)
                    .className(className)
                    .errorMessage(e.getMessage())
                    .methodName(e.getStackTrace()[0].getMethodName())
                    .stackTrace(e.getStackTrace().toString())
                    .build();
            LogHelper logHelper = new LogHelper();
            logHelper.Log(logModel);
        }
    }

    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString;
        String minutesString;

        try {
            int hours = (int) (milliseconds / (1000 * 60 * 60));
            int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
            int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
            // Add hours if there
            if (hours > 0) {
                finalTimerString = hours + ":";
            }

            // Prepending 0 to seconds if it is one digit
            if (seconds < 10) {
                secondsString = "0" + seconds;
            } else {
                secondsString = "" + seconds;
            }

            if (minutes < 10) {
                minutesString = "0" + minutes;
            } else {
                minutesString = "" + minutes;
            }

            finalTimerString = finalTimerString + minutesString + ":" + secondsString;
        } catch (Exception e) {

            LogModel logModel = new LogModel.LogBuilder()
                    .apiVersion(Utility.getAndroidVersion())
                    .appName(Constants.APP_NAME)
                    .className(className)
                    .errorMessage(e.getMessage())
                    .methodName(e.getStackTrace()[0].getMethodName())
                    .stackTrace(e.getStackTrace().toString())
                    .build();
            LogHelper logHelper = new LogHelper();
            logHelper.Log(logModel);
        } finally {
            return finalTimerString;
        }


    }
}
