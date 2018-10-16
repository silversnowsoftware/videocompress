package com.silversnowsoftware.vc.operations.compressor;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.ui.compression.permission.PermissionsActivity;
import com.silversnowsoftware.vc.ui.compression.videocompress.CompressListener;
import com.silversnowsoftware.vc.ui.compression.videocompress.Compressor;
import com.silversnowsoftware.vc.ui.main.MainActivity;
import com.silversnowsoftware.vc.utils.constants.Constants;
import com.silversnowsoftware.vc.utils.helpers.FileHelper;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.drakeet.materialdialog.MaterialDialog;

import static com.silversnowsoftware.vc.utils.helpers.FileHelper.getFileSize;

/**
 * Created by burak on 10/15/2018.
 */

public class FileCompressor implements IFileCompressor {


    private Compressor compressor;

    @Override
    public Compressor mCompressor(Activity act) {
        compressor = new Compressor(act);
        return compressor;
    }

    @Override
    public void Compress(String cmd, CompressListener listener, File mFile) {

//        File mFile = new File(currentOutputVideoPath);
//        if (mFile.exists()) {
//            mFile.delete();
//        }

        compressor.execCommand(cmd, new CompressListener() {
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



}



