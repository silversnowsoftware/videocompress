package com.silversnowsoftware.vc.utils.helpers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.ui.main.MainActivity;

import java.io.File;

import static com.silversnowsoftware.vc.utils.constants.Arrays.MIME_MapTable;

/**
 * Created by burak on 10/15/2018.
 */

public class FileHelper {

    public static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();

        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }

        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;

        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    public static String getFileSize(String path) {
        File f = new File(path);
        if (!f.exists()) {
            return "0 MB";
        } else {
            long size = f.length();
            return (size / 1024f) / 1024f + "MB";
        }
    }

    public static Intent openFile( File file) throws Exception {
        Intent intent = null;
        try {
            intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            intent.setAction(Intent.ACTION_VIEW);

            String type = FileHelper.getMIMEType(file);

            intent.setDataAndType(/*uri*/Uri.fromFile(file), type);

        } catch (Exception e) {
            intent = null;
            throw new Exception("Dosya Açılamadı.");
        }
        return intent;
    }

}