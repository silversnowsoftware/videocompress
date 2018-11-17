package com.silversnowsoftware.vc.ui.editor;

import android.content.Intent;

import com.silversnowsoftware.vc.ui.base.IBasePresenter;

/**
 * Created by burak on 11/1/2018.
 */

public interface IEditorPresenter<V extends IEditorView> extends IBasePresenter<V> {
    void ActivityResult(int requestCode, int resultCode, Intent data);

    void videoCompress();
    void setViewHolder();
    void setVideoToVideoView();
}
