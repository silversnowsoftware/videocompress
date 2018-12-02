package com.silversnowsoftware.vc.ui.editor;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


import static com.silversnowsoftware.vc.utils.SharedPref.getData;
import static com.silversnowsoftware.vc.utils.SharedPref.putData;
import static com.silversnowsoftware.vc.utils.constants.Arrays.VideoResolutions;
import static com.silversnowsoftware.vc.utils.helpers.FileHelper.getVideoResoution;

/**
 * Created by burak on 11/17/2018.
 */

public class EditorViewHolder<V extends IEditorView> implements IBaseViewHolder {
    Activity mView;
    @BindView(R.id.btnCompress)
    public Button btnCompress;
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
    @BindView(R.id.seekBarLayout)
    public LinearLayout seekBarLayout;
    @BindView(R.id.rb144p)
    public RadioButton rb144p;
    @BindView(R.id.rb240p)
    public RadioButton rb240p;
    @BindView(R.id.rb360p)
    public RadioButton rb360p;
    @BindView(R.id.rb480p)
    public RadioButton rb480p;
    @BindView(R.id.rb720p)
    public RadioButton rb720p;
    @BindView(R.id.rgResolution)
    public RadioGroup rgResolution;

    public SimpleExoPlayer exoPlayer;


    public EditorViewHolder(V activity) {
        mView = (Activity) activity;
        ButterKnife.bind(this, mView);
    }


    @OnClick(R.id.btnCompress)
    void btnCompress_onClick() {
        List<FileModel> fileModelList = (List<FileModel>) getData(Keys.FILE_LIST_KEY, Types.getFileModelListType(), mView);
        FileModel fileModel = fileModelList.get(fileModelList.size() - 1);

        Map<String, Integer> videoResolutions = getVideoResoution(fileModel.getPath());
        int width = videoResolutions.get("width");
        int height = videoResolutions.get("height");

        String resolution = getFitResolution(width, height);
        fileModel.setResolution(resolution);
        fileModel.setFileStatus(FileStatusEnum.PREPEARING);
        fileModelList.clear();
        fileModelList.add( fileModel);
        putData(Keys.FILE_LIST_KEY, fileModelList, mView);

        Intent listActivity = new Intent(mView, ListActivity.class);
        mView.startActivity(listActivity);

    }



    String getSelectedResolution() {
        int id = rgResolution.getCheckedRadioButtonId();
        switch (id) {
            case R.id.rb144p:
                return (String) rb144p.getText();
            case R.id.rb240p:
                return (String) rb240p.getText();
            case R.id.rb360p:
                return (String) rb360p.getText();
            case R.id.rb480p:
                return (String) rb480p.getText();
            case R.id.rb720p:
                return (String) rb720p.getText();
            default:
                return "";
        }

    }

    String getFitResolution(int width, int height) {
        int[] resolutions = VideoResolutions.get(getSelectedResolution());

        int orientation = 0;
        int minValue = height;
        int maxValue = width;
        if (height > width) {
            orientation = 1;
            maxValue = height;
            minValue = width;
        }

        double minRate = (double) resolutions[1] / (double) minValue;
        double maxRate = (double) resolutions[0] / (double) maxValue ;

        int minResult = (int) (minValue * minRate);
        int maxResult = (int) (maxValue * maxRate);

        String resolution = "";

        if (orientation == 0) {
            resolution = minResult + "x" + maxResult;
        } else {
            resolution = maxResult + "x" + minResult;
        }
        return resolution;
    }

/*
    @OnClick(R.id.btnCompress)
    void btnCompress_onClick() {
    }

    @OnClick(R.id.btnCompress)
    void btnCompress_onClick() {
    }*/


}
