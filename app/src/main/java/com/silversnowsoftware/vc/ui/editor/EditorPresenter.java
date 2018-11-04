package com.silversnowsoftware.vc.ui.editor;

import android.app.Activity;
import android.content.Intent;

import com.silversnowsoftware.vc.operations.compressor.FileCompressor;
import com.silversnowsoftware.vc.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by burak on 11/1/2018.
 */

public class EditorPresenter<V extends IEditorView> extends BasePresenter<V>
        implements IEditorPresenter<V> {

    @Inject
    public EditorPresenter() {
        super();

    }
    @Override
    public void ActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void VideoCompress() {
        FileCompressor fileCompressor = new FileCompressor(((Activity) getView()));
        fileCompressor.Compress();
    }
}
