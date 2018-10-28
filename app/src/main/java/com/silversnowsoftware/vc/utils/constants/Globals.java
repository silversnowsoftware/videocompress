package com.silversnowsoftware.vc.utils.constants;

import android.os.Environment;
import android.os.Handler;

import com.silversnowsoftware.vc.model.FileModel;

import java.util.ArrayList;

/**
 * Created by burak on 10/16/2018.
 */

public class Globals {

    public static String currentInputVideoPath = Environment.getExternalStorageDirectory().getPath() + "/videoKit/in.mp4";
    public static String currentOutputVideoPath = Environment.getExternalStorageDirectory().getPath() + "/videoKit/";
    public static ArrayList<FileModel> FileModelList;
    public static Handler handler;
}
