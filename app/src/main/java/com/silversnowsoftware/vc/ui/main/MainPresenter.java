package com.silversnowsoftware.vc.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.operations.compressor.FileCompressor;
import com.silversnowsoftware.vc.operations.compressor.IFileCompressor;
import com.silversnowsoftware.vc.ui.base.BasePresenter;
import com.silversnowsoftware.vc.ui.compression.permission.PermissionsActivity;
import com.silversnowsoftware.vc.utils.constants.Constants;
import com.silversnowsoftware.vc.utils.constants.Globals;
import com.silversnowsoftware.vc.utils.helpers.FileHelper;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

/**
 * Created by burak on 10/14/2018.
 */

public class MainPresenter<V extends IMainView> extends BasePresenter<V>
        implements IMainPresenter<V> {

    private Double videoLength = 0.00;
    private String cmd = "-y -i " + Globals.currentInputVideoPath + " -strict -2 -vcodec libx264 -preset ultrafast " +
            "-crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s 640x480 -aspect 16:9 " + Globals.currentOutputVideoPath;

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

                    FileModel mFileModel2 = new FileModel();
                    mFileModel2.setPath(videoPath);
                    mFileModel2.setName("out2.mp4");



                    MediaMetadataRetriever retr = new MediaMetadataRetriever();
                    retr.setDataSource(Globals.currentInputVideoPath);
                    String time = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    //7680
                    try {
                        videoLength = Double.parseDouble(time) / 1000.00;
                    } catch (Exception e) {
                        e.printStackTrace();
                        videoLength = 0.00;
                    }

                    mFileModel.setVideoLength(videoLength);
                    mFileModel2.setVideoLength(videoLength);
                    Globals.FileModelList.add(mFileModel);
                    Globals.FileModelList.add(mFileModel2);
                   // refreshCurrentPath();
                }
            } else if (resultCode == Constants.RESULT_CODE_FOR_RECORD_VIDEO_FAILED) {

                Globals.currentInputVideoPath = "";
            }
        }
    }

    private void refreshCurrentPath() {

        setCmd("-y -i " + Globals.currentInputVideoPath + " -strict -2 -vcodec libx264 -preset ultrafast " +
                "-crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s 858x480 -aspect 16:9 " + Globals.currentOutputVideoPath);

    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public void VideoCompress() {
        FileCompressor fileCompressor = new FileCompressor(((Activity) getView()));
        fileCompressor.Compress();

    }


}
