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

//    private String getProgress(String source) {
//        //progress frame=   28 fps=0.0 q=24.0 size= 107kB time=00:00:00.91 bitrate= 956.4kbits/s
//        Pattern p = Pattern.compile("00:\\d{2}:\\d{2}");
//        Matcher m = p.matcher(source);
//        if (m.find()) {
//            //00:00:00
//            String result = m.group(0);
//            String temp[] = result.split(":");
//            Double seconds = Double.parseDouble(temp[1]) * 60 + Double.parseDouble(temp[2]);
//
//            if (0 != videoLength) {
//                return seconds / videoLength + "";
//            }
//            return "0";
//        }
//        return "";
//    }


}



