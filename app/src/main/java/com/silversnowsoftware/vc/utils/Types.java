package com.silversnowsoftware.vc.utils;

import com.google.gson.reflect.TypeToken;
import com.silversnowsoftware.vc.model.FileModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by burak on 11/16/2018.
 */

public class Types {
    public static Type getFileModelListType() {
        return new TypeToken<ArrayList<FileModel>>() {
        }.getType();
    }
}
