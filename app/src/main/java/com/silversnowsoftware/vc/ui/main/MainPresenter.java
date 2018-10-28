package com.silversnowsoftware.vc.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.util.Log;

import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.operations.compressor.FileCompressor;
import com.silversnowsoftware.vc.ui.base.BasePresenter;
import com.silversnowsoftware.vc.ui.compression.permission.PermissionsActivity;
import com.silversnowsoftware.vc.utils.constants.Constants;
import com.silversnowsoftware.vc.utils.constants.Globals;

import javax.inject.Inject;

/**
 * Created by burak on 10/14/2018.
 */

public class MainPresenter<V extends IMainView> extends BasePresenter<V>
        implements IMainPresenter<V> {

    private Double videoLength = 0.00;

    @Inject
    public MainPresenter() {
        super();

    }

    @Override
    public void ActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.REQUEST_CODE_FOR_PERMISSIONS) {

            if (PermissionsActivity.PERMISSIONS_DENIED == resultCode) {
                ((Activity) getView()).finish();
            } else if (PermissionsActivity.PERMISSIONS_GRANTED == resultCode) {
                //do nothing
            }
        } else if (requestCode == Constants.REQUEST_CODE_FOR_RECORD_VIDEO) {

            if (resultCode == Constants.RESULT_CODE_FOR_RECORD_VIDEO_SUCCEED) {

                String videoPath = data.getStringExtra(Constants.INTENT_EXTRA_VIDEO_PATH);
                if (!TextUtils.isEmpty(videoPath)) {
                    Globals.currentInputVideoPath = videoPath;

                    FileModel mFileModel = new FileModel();
                    mFileModel.setPath(videoPath);
                    mFileModel.setName("out1.mp4");
                    mFileModel.setResolutionX("480");
                    mFileModel.setResolutionY("320");

                    FileModel mFileModel2 = new FileModel();
                    mFileModel2.setPath(videoPath);
                    mFileModel2.setName("out2.mp4");
                    mFileModel2.setResolutionX("720");
                    mFileModel2.setResolutionY("480");


                    MediaMetadataRetriever retr = new MediaMetadataRetriever();
                    retr.setDataSource(Globals.currentInputVideoPath);
                    String time = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

                    try {
                        videoLength = Double.parseDouble(time) / 1000.00;
                    } catch (Exception e) {
                        e.printStackTrace();
                        videoLength = 0.00;
                    }

                    mFileModel.setVideoLength(videoLength);
                    mFileModel2.setVideoLength(videoLength);
                    Globals.FileModelList.add(mFileModel);
                   // Globals.FileModelList.add(mFileModel2);

                    DbFileModel().add(Globals.FileModelList);

                    for (FileModel item: DbFileModel().getAll()) {
                        Log.v("FileModel::  ",item.getName() + "-" + item.getId());
                    }

                }
            } else if (resultCode == Constants.RESULT_CODE_FOR_RECORD_VIDEO_FAILED) {

                Globals.currentInputVideoPath = "";
            }
        }
    }

    public void VideoCompress() {
        FileCompressor fileCompressor = new FileCompressor(((Activity) getView()));
        fileCompressor.Compress();

    }


}
