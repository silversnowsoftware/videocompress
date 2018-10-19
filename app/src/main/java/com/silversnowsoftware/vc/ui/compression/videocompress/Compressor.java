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

import java.lang.reflect.Array;


public class Compressor {
    private ShellCommand shellCommand;
    private Process process;
    private Context context;
    public Activity a;
    public FFmpeg ffmpeg;

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
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
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
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }


    public void myCompress(String cmd) {
        String[] cmds = cmd.split(" ");

        String[] ffmpegBinary = new String[]{FileUtils.getFFmpeg(context, null)};
        String[] command = concatenate(ffmpegBinary, cmds);
        process = shellCommand.run(command);
        if (process == null) {
            CommandResult.getDummyFailureResponse();
        }


    }

    public <T> T[] concatenate(T[] a, T[] b) {
        int aLen = a.length;
        int bLen = b.length;

        @SuppressWarnings("unchecked")
        T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }

}
