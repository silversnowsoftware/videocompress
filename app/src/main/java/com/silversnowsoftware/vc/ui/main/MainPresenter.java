package com.silversnowsoftware.vc.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.util.Log;

import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.operations.compressor.FileCompressor;
import com.silversnowsoftware.vc.ui.base.BasePresenter;
import com.silversnowsoftware.vc.ui.compression.permission.PermissionsActivity;
import com.silversnowsoftware.vc.utils.constants.Constants;
import com.silversnowsoftware.vc.utils.constants.Globals;

import javax.inject.Inject;

/**
 * Created by burak on 10/14/2018.
 */

public class MainPresenter<V extends IMainView> extends BasePresenter<V>
        implements IMainPresenter<V> {

    private Double videoLength = 0.00;

    @Inject
    public MainPresenter() {
        super();

    }





}
