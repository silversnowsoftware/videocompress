package com.silversnowsoftware.vc.operations.compressor;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegInterface;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.model.logger.LogModel;
import com.silversnowsoftware.vc.ui.compression.videocompress.CompressListener;
import com.silversnowsoftware.vc.ui.compression.videocompress.Compressor;
import com.silversnowsoftware.vc.ui.compression.videocompress.InitListener;
import com.silversnowsoftware.vc.utils.Utility;
import com.silversnowsoftware.vc.utils.constants.Constants;
import com.silversnowsoftware.vc.utils.constants.Globals;
import com.silversnowsoftware.vc.utils.helpers.FileHelper;
import com.silversnowsoftware.vc.utils.helpers.LogHelper;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by burak on 10/15/2018.
 */

public class FileCompressor implements IFileCompressor {

    private static final String className = FileCompressor.class.getSimpleName();
    private Compressor mCompressor;
    private Context context;
    private FFmpegInterface ffmpeg;
    public String TAG = getClass().getName();


    public FileCompressor(Activity activity) {
        this.context = activity.getApplicationContext();
        ffmpeg = FFmpeg.getInstance(activity);
        mCompressor = new Compressor(activity);

    }


    public void Compress(final FileModel fileModel) {
        try {
            if (fileModel == null)
                return;


            File mFile = new File(Globals.currentOutputVideoPath + fileModel.getName());
            if (mFile.exists()) {
                mFile.delete();
            }
            Log.i("Progress ", fileModel.getName() + "--" + fileModel.getVideoLength());
            mCompressor.execCommand(fileModel.getCompressCmd(), new CompressListener() {
                int counter = 0;

                @Override
                public void onExecSuccess(String message) {
                    File trimFile = new File(fileModel.getPath());
                    if (trimFile.exists()) {
                        trimFile.delete();
                    }
                    fileModel.listener.onSuccess(100.0);
                }

                @Override
                public void onExecFail(String reason) {
                    Toast.makeText(context, reason, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onExecProgress(String message) {

                    Double progress = getProgress(message, fileModel.getVideoLength()) * 100;
                    fileModel.setProgress(progress);
                    fileModel.listener.onProgress(progress);


                    counter++;

                }
            });
        } catch (Exception ex) {

            LogHelper.Log(className, ex);
        }

    }


    public void loadBinary(final InitListener mListener) {
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onStart() {
                }

                @Override
                public void onFailure() {
                    mListener.onLoadFail("incompatible with this device");
                }

                @Override
                public void onSuccess() {
                    mListener.onLoadSuccess();
                }

                @Override
                public void onFinish() {

                }
            });
        } catch (FFmpegNotSupportedException ex) {

            LogHelper.Log(className, ex);
        }
    }

    @Override
    public Double getProgress(String source, Double videoLength) {
        //progress frame=   28 fps=0.0 q=24.0 size= 107kB time=00:00:00.91 bitrate= 956.4kbits/s
        Pattern p = Pattern.compile("00:\\d{2}:\\d{2}");
        Matcher m = p.matcher(source);
        if (m.find()) {
            //00:00:00
            String result = m.group(0);
            String temp[] = result.split(":");
            Double seconds = Double.parseDouble(temp[1]) * 60 + Double.parseDouble(temp[2]);

           /*  if (seconds.intValue() == videoLength.intValue()){
                 return 0.0;
             }*/
            if (0.0 != videoLength) {
                Log.i("VideoLen", String.valueOf(seconds) + "/" + String.valueOf(videoLength));
                return seconds / videoLength;
            }
            return 0.0;
        }
        return 0.0;
    }


}