package com.silversnowsoftware.vc.ui.editor;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.model.customvideoviews.CustomRangeSeekBar;
import com.silversnowsoftware.vc.model.customvideoviews.TileView;
import com.silversnowsoftware.vc.ui.base.IBaseViewHolder;
import com.silversnowsoftware.vc.ui.list.ListActivity;
import com.silversnowsoftware.vc.ui.list.ListViewHolder;
import com.silversnowsoftware.vc.utils.Types;
import com.silversnowsoftware.vc.utils.constants.Keys;
import com.silversnowsoftware.vc.utils.enums.FileStatusEnum;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


import static com.silversnowsoftware.vc.utils.SharedPref.getData;
import static com.silversnowsoftware.vc.utils.SharedPref.putData;
import static com.silversnowsoftware.vc.utils.constants.Arrays.VideoResolutions;

/**
 * Created by burak on 11/17/2018.
 */

public class EditorViewHolder<V extends IEditorView> implements IBaseViewHolder {
    Activity mView;
    @BindView(R.id.btnCompress)
    public Button btnCompress;
    @BindView(R.id.spResolution)
    public Spinner spResolution;
    @BindView(R.id.txtVideoCancel)
    public TextView txtVideoCancel;
    @BindView(R.id.txtVideoUpload)
    public TextView txtVideoUpload;
    @BindView(R.id.txtVideoTrimSeconds)
    public TextView txtVideoTrimSeconds;
    @BindView(R.id.timeLineView)
    public TileView tileView;
    @BindView(R.id.timeLineBar)
    public CustomRangeSeekBar mCustomRangeSeekBarNew;
    @BindView(R.id.imgPlay)
    public ImageView imgPlay;
    @BindView(R.id.seekBarVideo)
    public SeekBar seekBarVideo;
    @BindView(R.id.txtVideoLength)
    public TextView txtVideoLength;
    @BindView(R.id.exoVideoView)
    public SimpleExoPlayerView exoPlayerView;

    public SimpleExoPlayer exoPlayer;


    public EditorViewHolder(V activity) {
        mView = (Activity) activity;
        ButterKnife.bind(this, mView);
    }


    @OnClick(R.id.btnCompress)
    void btnCompress_onClick() {
        List<FileModel> fileModelList = (List<FileModel>) getData(Keys.FILE_LIST_KEY, Types.getFileModelListType(), mView);
        FileModel fileModel = fileModelList.get(fileModelList.size() - 1);
        String resolution = VideoResolutions.get(spResolution.getSelectedItem());
        fileModel.setResolution(resolution);
        fileModel.setFileStatus(FileStatusEnum.PREPEARING);
        fileModelList.set(fileModelList.indexOf(fileModel), fileModel);
        putData(Keys.FILE_LIST_KEY, fileModelList, mView);

        Intent listActivity = new Intent(mView, ListActivity.class);
        mView.startActivity(listActivity);

    }



/*
    @OnClick(R.id.btnCompress)
    void btnCompress_onClick() {
    }

    @OnClick(R.id.btnCompress)
    void btnCompress_onClick() {
    }*/


}
