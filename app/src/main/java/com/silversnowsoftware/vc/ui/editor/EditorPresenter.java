package com.silversnowsoftware.vc.ui.editor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.model.customvideoviews.BackgroundTask;
import com.silversnowsoftware.vc.model.customvideoviews.BarThumb;
import com.silversnowsoftware.vc.model.customvideoviews.CustomRangeSeekBar;
import com.silversnowsoftware.vc.model.listener.OnRangeSeekBarChangeListener;
import com.silversnowsoftware.vc.model.listener.OnVideoTrimListener;
import com.silversnowsoftware.vc.model.logger.LogModel;
import com.silversnowsoftware.vc.operations.compressor.FileCompressor;
import com.silversnowsoftware.vc.ui.base.BasePresenter;
import com.silversnowsoftware.vc.ui.base.BaseResponse;
import com.silversnowsoftware.vc.ui.list.ListActivity;
import com.silversnowsoftware.vc.ui.trimmer.VideoTimmerActivity;
import com.silversnowsoftware.vc.utils.Types;
import com.silversnowsoftware.vc.utils.Utility;
import com.silversnowsoftware.vc.utils.constants.Constants;
import com.silversnowsoftware.vc.utils.constants.Keys;
import com.silversnowsoftware.vc.utils.enums.FileStatusEnum;
import com.silversnowsoftware.vc.utils.helpers.LogHelper;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;
import static com.silversnowsoftware.vc.utils.SharedPref.getData;
import static com.silversnowsoftware.vc.utils.SharedPref.putData;
import static com.silversnowsoftware.vc.utils.constants.Arrays.VideoResolutions;
import static com.silversnowsoftware.vc.utils.helpers.FileHelper.getVideoDuration;
import static com.silversnowsoftware.vc.utils.helpers.FileHelper.getVideoResoution;
import static com.silversnowsoftware.vc.utils.helpers.FileHelper.retriveVideoFrameFromVideo;

/**
 * Created by burak on 11/1/2018.
 */

public class EditorPresenter<V extends IEditorView> extends BasePresenter<V>
        implements IEditorPresenter<V> {
    private static final String className = EditorPresenter.class.getSimpleName();
    EditorViewHolder mViewHolder;
    String srcFile;
    String mDefaultResolutionId;
    private int mDuration = 0;
    private int mTimeVideo = 0;
    private int mStartPosition = 0;
    private int mEndPosition = 0;
    // set your max video trim seconds
    private int mMaxDuration = 60;
    private Handler mHandler = new Handler();


    String dstFile = null;
    FileModel responseModel;

    @Inject
    public EditorPresenter() {
        super();
        responseModel = new FileModel();
    }

    public void setViewHolder() {
        mViewHolder = new EditorViewHolder((Activity) getView());

    }

    @Override
    public void ActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void videoCompress() {
        FileCompressor fileCompressor = new FileCompressor(((Activity) getView()));
        // fileCompressor.Compress();
    }


    @Override
    public void setVideoToVideoView() {
        try {
            List<FileModel> fileModelList = getRepositoryFileModel().getAll();

            srcFile = fileModelList.get(fileModelList.size() - 1).getPath();
            mMaxDuration = getVideoDuration((Activity) getView(), srcFile);
            mDefaultResolutionId = findVideoResolution(srcFile);
            setSelectedResolution(mDefaultResolutionId);

            mViewHolder.tileView.post(new Runnable() {
                @Override
                public void run() {

                    setBitmap(Uri.parse(srcFile));
                    setExoPlayer();
                }
            });

            mViewHolder.mCustomRangeSeekBarNew.addOnRangeSeekBarListener(new OnRangeSeekBarChangeListener() {
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
                    if (mViewHolder.exoPlayer != null) {
                        mHandler.removeCallbacks(mUpdateTimeTask);
                        mViewHolder.seekBarVideo.setProgress(0);
                        mViewHolder.exoPlayer.seekTo(mStartPosition * 1000);
                        mViewHolder.exoPlayer.setPlayWhenReady(false);
                        mViewHolder.imgPlay.setBackgroundResource(R.drawable.ic_white_play);
                    }
                }

                @Override
                public void onSeekStop(CustomRangeSeekBar customRangeSeekBarNew, int index, float value) {

                }
            });

            mViewHolder.imgPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playPauseVideo();
                }
            });

            mViewHolder.exoPlayerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    playPauseVideo();
                    return false;
                }
            });

            mViewHolder.mCustomRangeSeekBarNew.addOnRangeSeekBarListener(new OnRangeSeekBarChangeListener() {
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
                    if (mViewHolder.exoPlayer != null) {
                        mHandler.removeCallbacks(mUpdateTimeTask);
                        mViewHolder.seekBarVideo.setProgress(0);
                        mViewHolder.exoPlayer.seekTo(mStartPosition * 1000);
                        mViewHolder.exoPlayer.setPlayWhenReady(false);
                        mViewHolder.imgPlay.setBackgroundResource(R.drawable.ic_white_play);
                    }
                }

                @Override
                public void onSeekStop(CustomRangeSeekBar customRangeSeekBarNew, int index, float value) {

                }
            });


            mViewHolder.seekBarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()

            {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    if (mViewHolder.exoPlayer != null) {
                        mHandler.removeCallbacks(mUpdateTimeTask);
                        mViewHolder.seekBarVideo.setMax(mTimeVideo * 1000);
                        mViewHolder.seekBarVideo.setProgress(0);
                        mViewHolder.exoPlayer.seekTo(mStartPosition * 1000);
                        mViewHolder.exoPlayer.setPlayWhenReady(false);
                        mViewHolder.imgPlay.setBackgroundResource(R.drawable.ic_white_play);
                    }
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mHandler.removeCallbacks(mUpdateTimeTask);
                    mViewHolder.exoPlayer.seekTo((mStartPosition * 1000) - mViewHolder.seekBarVideo.getProgress());
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

    @Override
    public void onVideoPrepared() {
        try {
            int duration = getVideoDuration((Activity) getView(), srcFile);
            mDuration = (duration);
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

    @Override
    public void setSeekBarPosition() {

        try {
            if (mDuration >= mMaxDuration) {
                mStartPosition = 0;
                mEndPosition = mMaxDuration;

                mViewHolder.mCustomRangeSeekBarNew.setThumbValue(0, (mStartPosition * 100) / mDuration);
                mViewHolder.mCustomRangeSeekBarNew.setThumbValue(1, (mEndPosition * 100) / mDuration);

            } else {
                mStartPosition = 0;
                mEndPosition = mDuration;
            }


            mTimeVideo = mDuration;
            mViewHolder.mCustomRangeSeekBarNew.initMaxWidth();
            mViewHolder.seekBarVideo.setMax(mMaxDuration * 1000);
            mViewHolder.exoPlayer.seekTo(mStartPosition * 1000);

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

            mViewHolder.txtVideoTrimSeconds.setText(String.format(Locale.US, "%02d:%02d - %02d:%02d", startMin, startSec, endMin, endSec));
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
    public void setExoPlayer() {

        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            mViewHolder.exoPlayer = ExoPlayerFactory.newSimpleInstance((Activity) getView(), trackSelector);

            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getContext(), "exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(
                    Uri.parse(srcFile),
                    dataSourceFactory,
                    new DefaultExtractorsFactory(),
                    null,
                    null);

            mViewHolder.exoPlayerView.setPlayer(mViewHolder.exoPlayer);
            mViewHolder.exoPlayer.prepare(mediaSource);
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

    public Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            try {


                if (mViewHolder.seekBarVideo.getProgress() >= mViewHolder.seekBarVideo.getMax()) {
                    mViewHolder.seekBarVideo.setProgress((int) (mViewHolder.exoPlayer.getCurrentPosition() - mStartPosition * 1000));
                    mViewHolder.txtVideoLength.setText(milliSecondsToTimer(mViewHolder.seekBarVideo.getProgress()) + "");
                    mViewHolder.exoPlayer.seekTo(mStartPosition * 1000);
                    mViewHolder.exoPlayer.setPlayWhenReady(false);
                    mViewHolder.seekBarVideo.setProgress(0);
                    mViewHolder.txtVideoLength.setText("00:00");
                    mViewHolder.imgPlay.setBackgroundResource(R.drawable.ic_white_play);
                } else {
                    mViewHolder.seekBarVideo.setProgress((int) (mViewHolder.exoPlayer.getCurrentPosition() - mStartPosition * 1000));
                    mViewHolder.txtVideoLength.setText(milliSecondsToTimer(mViewHolder.seekBarVideo.getProgress()) + "");
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

    @Override
    public void setBitmap(Uri mVideoUri) {
        mViewHolder.tileView.setVideo(mVideoUri);
    }

    @Override
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
        }
        // return timer string
        return finalTimerString;
    }

    @Override
    public void onSeekThumbs(int index, float value) {
        try {
            switch (index) {
                case BarThumb.LEFT: {
                    mStartPosition = (int) ((mDuration * value) / 100L);
                    mViewHolder.exoPlayer.seekTo(mStartPosition * 1000);
                    break;
                }
                case BarThumb.RIGHT: {
                    mEndPosition = (int) ((mDuration * value) / 100L);
                    break;
                }
            }
            mTimeVideo = (mEndPosition - mStartPosition);
            mViewHolder.seekBarVideo.setMax(mTimeVideo * 1000);
            mViewHolder.seekBarVideo.setProgress(0);
            mViewHolder.exoPlayer.seekTo(mStartPosition * 1000);

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

            mViewHolder.txtVideoTrimSeconds.setText(String.format(Locale.US, "%02d:%02d - %02d:%02d", startMin, startSec, endMin, endSec));
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
    public String trimVideo(final OnVideoTrimListener mOnVideoTrimListener) {
        dstFile = Environment.getExternalStorageDirectory() + "/" + getContext().getString(R.string.app_name) + new Date().getTime()
                + Utility.VIDEO_FORMAT;
        try {

            MediaMetadataRetriever
                    mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource((Activity) getView(), Uri.parse(srcFile));
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
        return dstFile;
    }

    @Override
    public FileModel addSelectedFile() {


        try {
            List<FileModel> fileModelList = getRepositoryFileModel().getAll();
            responseModel = fileModelList.get(fileModelList.size() - 1);
            responseModel.setSuccess(true);

            Map<String, Integer> videoResolutions = getVideoResoution(responseModel.getPath());
            int width = videoResolutions.get("width");
            int height = videoResolutions.get("height");

            String videoResolution = getSelectedResolution();
            if (!videoResolution.isEmpty()) {
                String resolution = getFitResolution(width, height, videoResolution);
                responseModel.setResolution(resolution);
                responseModel.setFileStatus(FileStatusEnum.PREPEARING);
                getRepositoryFileModel().add(responseModel);

            } else {
                responseModel.setSuccess(false);
                responseModel.setMessage(getContext().getString(R.string.choose_format));
            }
        } catch (Exception e) {
            responseModel.setSuccess(false);
            responseModel.setMessage(getContext().getString(R.string.error));
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

        return responseModel;
    }

    @Override
    public void setDefaultEditor() {
        try {


            mStartPosition = 0;
            mEndPosition = mMaxDuration;
            mViewHolder.mCustomRangeSeekBarNew.setThumbValue(0, (mStartPosition * 100) / mDuration);
            mViewHolder.mCustomRangeSeekBarNew.setThumbValue(1, (mEndPosition * 100) / mDuration);
            mViewHolder.seekBarVideo.setProgress((int) (mViewHolder.exoPlayer.getCurrentPosition() - mStartPosition * 1000));
            mViewHolder.seekBarVideo.setMax(mMaxDuration * 1000);
            mViewHolder.txtVideoLength.setText(milliSecondsToTimer(mViewHolder.seekBarVideo.getProgress()) + "");
            mViewHolder.exoPlayer.seekTo(mStartPosition * 1000);
            mViewHolder.exoPlayer.setPlayWhenReady(false);
            mViewHolder.seekBarVideo.setProgress(0);
            mViewHolder.txtVideoLength.setText("00:00");
            mViewHolder.imgPlay.setBackgroundResource(R.drawable.ic_white_play);

            setSelectedResolution(mDefaultResolutionId);
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
    public void updateModel(FileModel model) {
        try {
            getRepositoryFileModel().update(model);
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


    String getSelectedResolution() {
        try {


            int id = mViewHolder.rgResolution.getCheckedRadioButtonId();
            switch (id) {
                case R.id.rb144p:
                    return (String) mViewHolder.rb144p.getText();
                case R.id.rb240p:
                    return (String) mViewHolder.rb240p.getText();
                case R.id.rb360p:
                    return (String) mViewHolder.rb360p.getText();
                case R.id.rb480p:
                    return (String) mViewHolder.rb480p.getText();
                case R.id.rb720p:
                    return (String) mViewHolder.rb720p.getText();
                case R.id.rb1080p:
                    return (String) mViewHolder.rb1080p.getText();
                default:
                    return "";

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
        } finally {
            return "";
        }

    }

    void setSelectedResolution(String resolution) {
        try {
            int id = -1;
            switch (resolution) {
                case "144p":
                    id = mViewHolder.rb144p.getId();
                    break;
                case "240p":
                    id = mViewHolder.rb240p.getId();
                    break;
                case "360p":
                    id = mViewHolder.rb360p.getId();
                    break;
                case "480p":
                    id = mViewHolder.rb480p.getId();
                    break;
                case "720p":
                    id = mViewHolder.rb720p.getId();
                    break;
                case "1080p":
                    id = mViewHolder.rb1080p.getId();
                    break;
                default:
                    id = -1;
                    break;
            }
            mViewHolder.rgResolution.clearCheck();
            mViewHolder.rgResolution.check(id);
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

    String getFitResolution(int width, int height, String videoResolution) {
        String resolution = "";
        try {
            int[] resolutions = VideoResolutions.get(videoResolution);

            int orientation = 0;
            int minValue = height;
            int maxValue = width;
            if (height > width) {
                orientation = 1;
                maxValue = height;
                minValue = width;
            }

            double minRate = (double) resolutions[1] / (double) minValue;
            double maxRate = (double) resolutions[0] / (double) maxValue;

            int minResult = (int) (minValue * minRate);
            int maxResult = (int) (maxValue * maxRate);


            if (orientation == 0) {
                resolution = minResult + "x" + maxResult;
            } else {
                resolution = maxResult + "x" + minResult;
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
        return resolution;
    }

    String findVideoResolution(String path) {
        String resolution = "";
        try {
            Map<String, Integer> videoResolutions = getVideoResoution(path);
            int width = videoResolutions.get("width");
            int height = videoResolutions.get("height");
            int[] value = new int[]{width, height};

            for (Map.Entry<String, int[]> item : VideoResolutions.entrySet()) {
                int w = item.getValue()[1];
                int h = item.getValue()[0];
                if (width == w)
                    resolution = item.getKey();
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
        return resolution;
    }

    void playPauseVideo() {
        try {


            if (mViewHolder.exoPlayer.getPlayWhenReady()) {
                if (mViewHolder.exoPlayer != null) {
                    mViewHolder.exoPlayer.setPlayWhenReady(false);
                    mViewHolder.imgPlay.setBackgroundResource(R.drawable.ic_white_play);
                    mViewHolder.seekBarLayout.animate().cancel();
                    mViewHolder.seekBarLayout.setTranslationY(0);
                }
            } else {
                if (mViewHolder.exoPlayer != null) {
                    mViewHolder.exoPlayer.setPlayWhenReady(true);
                    mViewHolder.imgPlay.setBackgroundResource(R.drawable.ic_white_pause);
                    int progress = mViewHolder.seekBarVideo.getProgress();
                    if (progress >= 0) {
                        mViewHolder.txtVideoLength.setText("00:0" + (progress / 1000));
                        mHandler.postDelayed(mUpdateTimeTask, 100);
                    }

                    mViewHolder.seekBarLayout.animate().setStartDelay(3000).translationY(mViewHolder.seekBarLayout.getHeight()).setDuration(1000);

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

}
