package com.silversnowsoftware.vc.ui.main;

import android.content.Intent;
import android.graphics.Bitmap;

import com.google.gson.reflect.TypeToken;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.ui.base.BasePresenter;
import com.silversnowsoftware.vc.ui.base.IBaseViewHolder;
import com.silversnowsoftware.vc.ui.list.ListViewHolder;
import com.silversnowsoftware.vc.utils.Types;
import com.silversnowsoftware.vc.utils.constants.Keys;
import com.silversnowsoftware.vc.utils.helpers.FileHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.silversnowsoftware.vc.utils.SharedPref.getData;
import static com.silversnowsoftware.vc.utils.SharedPref.putData;
import static com.silversnowsoftware.vc.utils.helpers.FileHelper.getBase64FromBitmap;
import static com.silversnowsoftware.vc.utils.helpers.FileHelper.getFileNameFromPath;


/**
 * Created by burak on 10/14/2018.
 */

public class MainPresenter<V extends IMainView> extends BasePresenter<V>
        implements IMainPresenter<V> {
    MainViewHolder mViewHolder;
    private Double videoLength = 0.00;

    @Inject
    public MainPresenter() {
        super();
    }

    public void setViewHolder() {
        mViewHolder = new MainViewHolder(getView());
    }
    public void collectFiles(Intent data) {
        ArrayList<String> tempfilepath = FileHelper.GetAllPath(getContext(), data);
        for (String path : tempfilepath) {

            FileModel fileModel = createFileModel(path);

            List<FileModel> list = (List<FileModel>) getData(Keys.FILE_LIST_KEY, Types.getFileModelListType(), getContext());
            if(list == null) list = new  ArrayList<FileModel>();
            if (!list.contains(fileModel))
                list.add(fileModel);

            putData(Keys.FILE_LIST_KEY, list, getContext());
        }
    }

    public FileModel createFileModel(String path) {

        FileModel fileModel = new FileModel();

        fileModel.setPath(path);
        fileModel.setName(getFileNameFromPath(path));

        Bitmap bitmap = null;
        try {
            bitmap = FileHelper.retriveVideoFrameFromVideo(path);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        if (bitmap != null) {
            bitmap = Bitmap.createScaledBitmap(bitmap, 240, 240, false);
            String byteThumb = getBase64FromBitmap(bitmap);
            fileModel.setThumbnail(byteThumb);
        }
        return fileModel;
    }




}
