package com.silversnowsoftware.vc.operations.compressor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.CommandResult;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegInterface;
import com.github.hiteshsondhi88.libffmpeg.FileUtils;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.ShellCommand;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.ui.compression.permission.PermissionsActivity;
import com.silversnowsoftware.vc.ui.compression.videocompress.CompressListener;
import com.silversnowsoftware.vc.ui.compression.videocompress.Compressor;
import com.silversnowsoftware.vc.ui.compression.videocompress.InitListener;
import com.silversnowsoftware.vc.ui.main.MainActivity;
import com.silversnowsoftware.vc.utils.constants.Constants;
import com.silversnowsoftware.vc.utils.constants.Globals;
import com.silversnowsoftware.vc.utils.enums.FileStatusEnum;
import com.silversnowsoftware.vc.utils.helpers.FileHelper;

import java.io.File;
import java.lang.reflect.Array;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.drakeet.materialdialog.MaterialDialog;

import static com.silversnowsoftware.vc.utils.helpers.FileHelper.getFileSize;

/**
 * Created by burak on 10/15/2018.
 */

public class FileCompressor implements IFileCompressor {


    private Compressor mCompressor;
    private Context context;
    public Activity a;
    FFmpegInterface ffmpeg;
    public String TAG = getClass().getName();
    static FileCompressor mFileCompressor;

    private FileCompressor(Activity activity) {
        this.context = activity.getApplicationContext();
        this.a = activity;
        ffmpeg = FFmpeg.getInstance(activity);
        mCompressor = new Compressor(activity);
    }

    public static FileCompressor getInstance(Activity activity){
        if(mFileCompressor == null)
            return new FileCompressor(activity);
        else
            return mFileCompressor;
    }

    public void Compress(String cmd, final Double videoLength) {

      File mFile = new File(Globals.currentOutputVideoPath);
        if (mFile.exists()) {
            mFile.delete();
        }

        mCompressor.execCommand(cmd, new CompressListener() {
            @Override
            public void onExecSuccess(String message) {
                Toast.makeText(context,"Video Compressed", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onExecFail(String reason) {
                Toast.makeText(context,"Video Error", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onExecProgress(String message) {

               Log.i("Progress::",message);
                getProgress(message, videoLength);
            }
        });

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

    @Override
    public String getProgress(String source,Double videoLength) {
        //progress frame=   28 fps=0.0 q=24.0 size= 107kB time=00:00:00.91 bitrate= 956.4kbits/s
        Pattern p = Pattern.compile("00:\\d{2}:\\d{2}");
        Matcher m = p.matcher(source);
        if (m.find()) {
            //00:00:00
            String result = m.group(0);
            String temp[] = result.split(":");
            Double seconds = Double.parseDouble(temp[1]) * 60 + Double.parseDouble(temp[2]);

            if (0 != videoLength) {
                return seconds / videoLength + "";
            }
            return "0";
        }
        return "";
    }

}