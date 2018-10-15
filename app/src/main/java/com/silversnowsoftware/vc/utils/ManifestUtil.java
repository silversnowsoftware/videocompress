package com.silversnowsoftware.vc.utils;

import android.Manifest;

/**
 * Created by burak on 10/15/2018.
 */

public class ManifestUtil {

    public static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
}
