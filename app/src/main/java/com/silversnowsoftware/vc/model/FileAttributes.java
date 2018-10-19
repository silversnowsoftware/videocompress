package com.silversnowsoftware.vc.model;

/**
 * Created by burak on 10/17/2018.
 */

public class FileAttributes {
    public CompressAttributes compressAttributes;
    public CropAttributes cropAttributes;

    public FileAttributes() {
        compressAttributes = new CompressAttributes();
        cropAttributes = new CropAttributes();
    }

    public class CompressAttributes {

    }

    public class CropAttributes {

    }
}

