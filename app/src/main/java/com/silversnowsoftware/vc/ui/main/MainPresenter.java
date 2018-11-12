package com.silversnowsoftware.vc.ui.main;

import android.content.Intent;
import android.graphics.Bitmap;

import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.ui.base.BasePresenter;
import com.silversnowsoftware.vc.utils.helpers.FileHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.silversnowsoftware.vc.utils.SharedPref.getData;
import static com.silversnowsoftware.vc.utils.SharedPref.putData;
import static com.silversnowsoftware.vc.utils.helpers.FileHelper.getBase64FromBitmap;


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

    public void collectFiles(Intent data) {
        ArrayList<String> tempfilepath = FileHelper.GetAllPath(getContext(), data);
        for (String path : tempfilepath) {

            FileModel fileModel = createFileModel(path);

            List<FileModel> list = getData("MyKey", ArrayList.class, getContext());

            if (!list.contains(fileModel)) list.add(fileModel);

            putData("MyKey", list, getContext());
        }
    }

    public FileModel createFileModel(String path) {

        FileModel fileModel = new FileModel();
        fileModel.setName(path);

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
