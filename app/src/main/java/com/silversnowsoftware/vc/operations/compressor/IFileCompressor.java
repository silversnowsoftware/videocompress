package com.silversnowsoftware.vc.operations.compressor;

import android.app.Activity;

import com.silversnowsoftware.vc.ui.compression.videocompress.CompressListener;
import com.silversnowsoftware.vc.ui.compression.videocompress.Compressor;

import java.io.File;

/**
 * Created by burak on 10/15/2018.
 */

public interface IFileCompressor {
    Compressor mCompressor(Activity act);

    void Compress(String cmd, final CompressListener listener, File mFile);
}
