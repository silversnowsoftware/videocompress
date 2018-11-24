package com.silversnowsoftware.vc.ui.editor;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.MediaController;

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
import com.silversnowsoftware.vc.operations.compressor.FileCompressor;
import com.silversnowsoftware.vc.ui.base.BasePresenter;
import com.silversnowsoftware.vc.utils.Types;
import com.silversnowsoftware.vc.utils.constants.Keys;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.silversnowsoftware.vc.utils.SharedPref.getData;
import static com.silversnowsoftware.vc.utils.constants.Arrays.VideoResolutions;

/**
 * Created by burak on 11/1/2018.
 */

public class EditorPresenter<V extends IEditorView> extends BasePresenter<V>
        implements IEditorPresenter<V> {

    EditorViewHolder mViewHolder;

    @Inject
    public EditorPresenter() {
        super();

    }

    public void setViewHolder() {
        mViewHolder = new EditorViewHolder(getView());

    }

    @Override
    public void ActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void videoCompress() {
        FileCompressor fileCompressor = new FileCompressor(((Activity) getView()));
        // fileCompressor.Compress();
    }

    public void setVideoToVideoView() {
        List<FileModel> fileModelList = (List<FileModel>) getData(Keys.FILE_LIST_KEY, Types.getFileModelListType(), getContext());
    /*    MediaController mediaController= new MediaController((Activity) getView());
        mediaController.setAnchorView(mViewHolder.vvVideoPlayer);*/
/*
        mViewHolder.vvVideoPlayer.setMediaController(mediaController);
        mViewHolder.vvVideoPlayer.setVideoPath(fileModelList.get(0).getPath());
        mViewHolder.vvVideoPlayer.requestFocus();
        mViewHolder.vvVideoPlayer.start();*/


        SimpleExoPlayerView exoPlayerView;
        SimpleExoPlayer exoPlayer;
        String videoURL = "http://blueappsoftware.in/layout_design_android_blog.mp4";




        exoPlayerView =  mViewHolder.vvVideoPlayer;
        try {


            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance((Activity)getView(), trackSelector);

            Uri videoURI = Uri.parse(videoURL);

            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getContext(),"exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource =

                    new ExtractorMediaSource(
                            Uri.parse(fileModelList.get(0).getPath()),
                            dataSourceFactory,
                            new DefaultExtractorsFactory(),
                            null,
                            null);




            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.seekTo(5000);
            exoPlayer.setPlayWhenReady(true);


        } catch (Exception e) {
            Log.e("MainAcvtivity", " exoplayer error " + e.toString());
        }


    }

    public void fillResolutionsSpinner() {
        int index = 0;
        String[] spinnerArray = new String[VideoResolutions.size()];
        for (Map.Entry<String, String> entry : VideoResolutions.entrySet()) {
            spinnerArray[index] = entry.getKey();
            index++;
        }
        Arrays.sort(spinnerArray);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mViewHolder.spResolution.setAdapter(adapter);
    }
}
