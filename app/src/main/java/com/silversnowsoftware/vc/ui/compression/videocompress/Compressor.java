package com.silversnowsoftware.vc.ui.compression.videocompress;

import android.app.Activity;
import android.content.Context;

import com.github.hiteshsondhi88.libffmpeg.CommandResult;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FileUtils;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.ShellCommand;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.silversnowsoftware.vc.data.db.DbFileModel;
import com.silversnowsoftware.vc.utils.helpers.LogManager;

import java.lang.reflect.Array;


public class Compressor {
    private ShellCommand shellCommand;
    private Process process;
    private Context context;
    public Activity a;
    public FFmpeg ffmpeg;
    private static final String className = Compressor.class.getSimpleName();
    public Compressor(Activity activity) {
        a = activity;
        ffmpeg = FFmpeg.getInstance(a);
    }

    public Compressor(Context context) {
        this.context = context;
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

            LogManager.Log(className, ex);
        }
    }

    public void execCommand(String cmd, final CompressListener mListener) {
        try {
            String[] cmds = cmd.split(" ");
            ffmpeg.execute(cmds, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {
                }

                @Override
                public void onProgress(String message) {
                    mListener.onExecProgress(message);
                }

                @Override
                public void onFailure(String message) {
                    mListener.onExecFail(message);
                }

                @Override
                public void onSuccess(String message) {
                    mListener.onExecSuccess(message);
                }

                @Override
                public void onFinish() {
                }
            });
        } catch (FFmpegCommandAlreadyRunningException ex) {

            LogManager.Log(className, ex);
        }
    }
}
