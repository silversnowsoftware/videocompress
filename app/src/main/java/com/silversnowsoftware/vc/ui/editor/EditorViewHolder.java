package com.silversnowsoftware.vc.ui.editor;

import android.app.Activity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.gms.ads.AdView;
import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.customvideoviews.CustomRangeSeekBar;
import com.silversnowsoftware.vc.model.customvideoviews.TileView;

import butterknife.BindView;
import butterknife.ButterKnife;


import static com.silversnowsoftware.vc.utils.SharedPref.getData;

/**
 * Created by burak on 11/17/2018.
 */

public final class EditorViewHolder {
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
    @BindView(R.id.rb1080p)
    public RadioButton rb1080p;
    @BindView(R.id.rgResolution)
    public RadioGroup rgResolution;
    @BindView(R.id.btnClear)
    public Button btnClear;
    @BindView(R.id.tvPreviewThumb)
    public TextView tvPreviewThumb;
    @BindView(R.id.adViewEditor)
    public AdView adViewEditor;

    public SimpleExoPlayer exoPlayer;


    public EditorViewHolder(Activity activity) {
        mView = (Activity) activity;
        ButterKnife.bind(this, mView);
    }

}
