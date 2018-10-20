package com.silversnowsoftware.vc.utils.constants;

import android.os.Environment;

/**
 * Created by burak on 10/16/2018.
 */

public class Globals {

    public static String currentInputVideoPath = Environment.getExternalStorageDirectory().getPath() + "/videoKit/in.mp4";
    public static String currentOutputVideoPath = Environment.getExternalStorageDirectory().getPath() + "/videoKit/out.mp4";
}
