package com.silversnowsoftware.vc.ui.editor;

import android.app.Activity;
import android.content.Intent;
import android.util.Pair;
import android.widget.ArrayAdapter;

import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.operations.compressor.FileCompressor;
import com.silversnowsoftware.vc.ui.base.BasePresenter;
import com.silversnowsoftware.vc.utils.Types;
import com.silversnowsoftware.vc.utils.constants.Keys;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.internal.Utils;

import static com.silversnowsoftware.vc.utils.SharedPref.getData;
import static com.silversnowsoftware.vc.utils.constants.Arrays.VideoResolutions;

/**
 * Created by burak on 11/1/2018.
 */

public class EditorPresenter<V extends IEditorView> extends BasePresenter<V>
        implements IEditorPresenter<V> {

    EditorViewHolder mViewHolder;

    @Inject
    public EditorPresenter() {
        super();

    }

    public void setViewHolder() {
        mViewHolder = new EditorViewHolder(getView());

    }

    @Override
    public void ActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void videoCompress() {
        FileCompressor fileCompressor = new FileCompressor(((Activity) getView()));
        // fileCompressor.Compress();
    }

    public void setVideoToVideoView() {
        List<FileModel> fileModelList = (List<FileModel>) getData(Keys.FILE_LIST_KEY, Types.getFileModelListType(), getContext());
        mViewHolder.vvVideoPlayer.setVideoPath(fileModelList.get(0).getPath());
        mViewHolder.vvVideoPlayer.start();
    }

    public void fillResolutionsSpinner() {
        int index = 0;
        String[] spinnerArray = new String[VideoResolutions.size()];
        for (Map.Entry<String, String> entry : VideoResolutions.entrySet()) {
            spinnerArray[index] = entry.getKey();
            index++;
        }
        Arrays.sort(spinnerArray);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mViewHolder.spResolution.setAdapter(adapter);
    }
}
