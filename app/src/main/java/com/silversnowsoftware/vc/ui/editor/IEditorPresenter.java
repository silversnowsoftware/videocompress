package com.silversnowsoftware.vc.ui.editor;

import android.content.Intent;
import android.net.Uri;

import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.model.listener.OnVideoTrimListener;
import com.silversnowsoftware.vc.ui.base.BaseResponse;
import com.silversnowsoftware.vc.ui.base.IBasePresenter;

/**
 * Created by burak on 11/1/2018.
 */

public interface IEditorPresenter<V extends IEditorView> extends IBasePresenter<V> {
    void ActivityResult(int requestCode, int resultCode, Intent data);

    void videoCompress();

    void setViewHolder();

    void setVideoPrepared();

    void onVideoPrepared();

    void setSeekBarPosition();

    void setExoPlayer();

    void playPauseVideo();

    void updateProgressBar();

    void setBitmap(Uri mVideoUri);

    String milliSecondsToTimer(long milliseconds);

    void onSeekThumbs(int index, float value);

    String trimVideo(OnVideoTrimListener mOnVideoTrimListener);

    FileModel processFile();

    void setDefaultEditor();

    void updateFileModel(FileModel model);

    boolean addFileModel(FileModel model);

    void customRangeSeekBarNewInit();

    void seekBarVideoInit();

    void loadAd();
}
