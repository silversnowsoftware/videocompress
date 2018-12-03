package com.silversnowsoftware.vc.ui.editor;

import android.content.Intent;
import android.net.Uri;

import com.silversnowsoftware.vc.ui.base.IBasePresenter;

/**
 * Created by burak on 11/1/2018.
 */

public interface IEditorPresenter<V extends IEditorView> extends IBasePresenter<V> {
    void ActivityResult(int requestCode, int resultCode, Intent data);

    void videoCompress();
    void setViewHolder();
    void setVideoToVideoView();
    void onVideoPrepared();
    void setSeekBarPosition();
    void setExoPlayer();
    void updateProgressBar();
    void setBitmap(Uri mVideoUri);
    String milliSecondsToTimer(long milliseconds);
    void onSeekThumbs(int index, float value);
    void TrimVideo();
}
