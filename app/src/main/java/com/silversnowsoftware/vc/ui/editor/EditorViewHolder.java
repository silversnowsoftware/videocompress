package com.silversnowsoftware.vc.ui.editor;

import android.app.Activity;
import android.widget.Button;
import android.widget.VideoView;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.ui.base.IBaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by burak on 11/17/2018.
 */

public class EditorViewHolder<V extends IEditorView> implements IBaseViewHolder {
    Activity mView;

    @BindView(R.id.btnCompress)
    public Button btnCompress;
    @BindView(R.id.vvVideoPlayer)
    public VideoView vvVideoPlayer;


    public EditorViewHolder(V activity) {
        mView = (Activity) activity;
        ButterKnife.bind(this, mView);
    }

}
