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
import com.github.hiteshsondhi88.libffmpeg.FileUtils;
import com.github.hiteshsondhi88.libffmpeg.ShellCommand;
import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.ui.compression.permission.PermissionsActivity;
import com.silversnowsoftware.vc.ui.compression.videocompress.CompressListener;
import com.silversnowsoftware.vc.ui.compression.videocompress.Compressor;
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
    private ShellCommand shellCommand;
    private Process process;
    private Context context;
    public Activity a;
    public FFmpeg ffmpeg;

    public FileCompressor(Context context){
            this.context = context;
    }

    @Override
    public Compressor mCompressor(Activity act) {
        mCompressor = new Compressor(act);
        return mCompressor;
    }

    @Override
    public void Compress(String cmd, final FileModel  mFile) {

//        File mFile = new File(currentOutputVideoPath);
//        if (mFile.exists()) {
//            mFile.delete();
//        }

        mCompressor.execCommand(cmd, new CompressListener() {
            @Override
            public void onExecSuccess(String message) {



            }

            @Override
            public void onExecFail(String reason) {


            }

            @Override
            public void onExecProgress(String message) {



            }
        });

    }


    public void myCompress(String cmd) {
        String[] cmds = cmd.split(" ");

        String[] ffmpegBinary = new String[]{FileUtils.getFFmpeg(context, null)};
        String[] command = concatenate(ffmpegBinary, cmds);
        shellCommand = new ShellCommand();
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