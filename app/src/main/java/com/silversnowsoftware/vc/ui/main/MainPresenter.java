package com.silversnowsoftware.vc.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.model.logger.LogModel;
import com.silversnowsoftware.vc.ui.base.BasePresenter;
import com.silversnowsoftware.vc.utils.Types;
import com.silversnowsoftware.vc.utils.Utility;
import com.silversnowsoftware.vc.utils.constants.Constants;
import com.silversnowsoftware.vc.utils.constants.Keys;
import com.silversnowsoftware.vc.utils.helpers.FileHelper;
import com.silversnowsoftware.vc.utils.helpers.LogManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.silversnowsoftware.vc.utils.SharedPref.getData;
import static com.silversnowsoftware.vc.utils.SharedPref.putData;
import static com.silversnowsoftware.vc.utils.helpers.FileHelper.getBase64FromBitmap;
import static com.silversnowsoftware.vc.utils.helpers.FileHelper.getFileNameFromPath;
import static com.silversnowsoftware.vc.utils.helpers.FileHelper.getVideoResoution;


/**
 * Created by burak on 10/14/2018.
 */

public class MainPresenter<V extends IMainView> extends BasePresenter<V>
        implements IMainPresenter<V> {
    private static final String className = MainPresenter.class.getSimpleName();
    private Double videoLength = 0.00;

    @Inject
    public MainPresenter() {
        super();
    }

    @Override
    public void chooseFile() {
        try {


            Activity activity = (Activity) getView();
            Intent mediaIntent = new Intent(
                    Intent.ACTION_GET_CONTENT
                    //,Uri.parse(Environment.DIRECTORY_DCIM)
            );
            // mediaIntent.setType("*/*");
            mediaIntent.setType("video/*");
            mediaIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"video/*"});
           /* mediaIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);*/
            activity.startActivityForResult(mediaIntent, 1);
        } catch (Exception ex) {

            LogManager.Log(className, ex);

        }
    }

    public void collectFiles(Intent data) {
        try {
            ArrayList<String> tempfilepath = FileHelper.GetAllPath(getContext(), data);
            for (String path : tempfilepath) {
                if (path == null)
                    continue;

                FileModel file = createFileModel(path);
                if (file != null) {
                    getRepositoryFileModel().add(file);
                }
            }
        } catch (Exception ex) {

            LogManager.Log(className, ex);

        }

    }

    public FileModel createFileModel(String path) {
        FileModel fileModel = new FileModel();

        try {
            fileModel.setPath(path);
            fileModel.setName(getFileNameFromPath(path));
            Map<String, Integer> videoResolutions = getVideoResoution(path);
            int width = videoResolutions.get("width");
            int height = videoResolutions.get("height");
            fileModel.setResolution(width + "x" + height);

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

            MediaMetadataRetriever retr = new MediaMetadataRetriever();
            retr.setDataSource(fileModel.getPath());
            String time = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

            try {
                videoLength = Double.parseDouble(time) / 1000.00;
            } catch (Exception ex) {
                videoLength = 0.00;

                LogManager.Log(className, ex);
            }
            fileModel.setVideoLength(videoLength.intValue());

        } catch (Exception ex) {
            LogManager.Log(className, ex);
        }
        return fileModel;
    }

    @Override
    public void deleteAllFiles() {
        try {
            getRepositoryFileModel().removeAll();
        } catch (Exception ex) {

            LogManager.Log(className, ex);
        }
    }


}
