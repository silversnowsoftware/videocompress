package com.silversnowsoftware.vc.ui.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.data.db.DbFileModel;
import com.silversnowsoftware.vc.data.db.IRepository;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.utils.enums.FileStatusEnum;
import com.silversnowsoftware.vc.utils.helpers.LogManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by burak on 10/14/2018.
 */

public class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {

    private V mView;

    @Override
    public void onAttach(V view) {
        mView = view;
    }

    @Override
    public void onDetach() {

    }

    @Override
    public V getView() {
        return mView;
    }



    protected Context getContext() {
        return ((Activity) getView()).getApplicationContext();
    }


    protected IRepository<FileModel> getRepositoryFileModel() {
        return new DbFileModel(getContext());
    }
    public void alertDialog(final Activity context, final String title, final String message) {

        if (!context.isFinishing()) {
            new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(context.getString(R.string.Ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            dialog.dismiss();
                        }
                    }).show();
        }
    }
    public void deleteErrorFileModel() {
        try {
            List<FileModel> fileModelList = getRepositoryFileModel().getFileModelListWithFileStatus();

            getRepositoryFileModel().removeItems(fileModelList);
        } catch (Exception ex) {
            LogManager.Log("BasePresenter", ex);
        }
    }

}
