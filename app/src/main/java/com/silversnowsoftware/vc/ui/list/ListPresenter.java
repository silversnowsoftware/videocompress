package com.silversnowsoftware.vc.ui.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.model.logger.LogModel;
import com.silversnowsoftware.vc.ui.base.BasePresenter;
import com.silversnowsoftware.vc.ui.base.BaseResponse;
import com.silversnowsoftware.vc.ui.base.component.VideoCompressAdapter;
import com.silversnowsoftware.vc.ui.editor.EditorActivity;
import com.silversnowsoftware.vc.ui.main.IMainPresenter;
import com.silversnowsoftware.vc.ui.main.IMainView;
import com.silversnowsoftware.vc.utils.Utility;
import com.silversnowsoftware.vc.utils.constants.Constants;
import com.silversnowsoftware.vc.utils.constants.Globals;
import com.silversnowsoftware.vc.utils.constants.Keys;
import com.silversnowsoftware.vc.utils.helpers.LogManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.silversnowsoftware.vc.utils.SharedPref.getData;
import static com.silversnowsoftware.vc.utils.Types.getFileModelListType;

/**
 * Created by burak on 11/16/2018.
 */

public class ListPresenter<V extends IListView> extends BasePresenter<V> implements IListPresenter<V> {
    private static final String className = ListPresenter.class.getSimpleName();
    ListViewHolder viewHolder;
    @Inject
    IMainPresenter<IMainView> mPresenter;

    @Inject
    public ListPresenter() {
        super();

    }

    public void setViewHolder() {
        try {
            viewHolder = new ListViewHolder(getView());
        } catch (Exception ex) {
            LogManager.Log(className, ex);
        }
    }

    @Override
    public BaseResponse deleteSelectedFile(FileModel fileModel) {
        BaseResponse response = new BaseResponse();
        try {
            getRepositoryFileModel().remove(fileModel);
            response.setSuccess(true);
        } catch (Exception ex) {
            response.setSuccess(false);
            LogManager.Log(className, ex);
        }
        return response;
    }

    @Override
    public void shareVideoFiles(List<FileModel> fileModelList) {
        try {

            if (fileModelList != null && fileModelList.size() > 0) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.setType("video/*");
                ArrayList<Uri> files = new ArrayList<Uri>();

                for (FileModel fileModel : fileModelList) {
                    File file = new File(fileModel.getPath());
                    Uri uri = Uri.fromFile(file);
                    files.add(uri);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                getContext().startActivity(intent);
            } else {
                alertDialog(((Activity) getView()), getContext().getString(R.string.Alert), getContext().getString(R.string.ChooseAnyVideoForShare));
            }
        } catch (Exception ex) {

            LogManager.Log(className, ex);
        }
    }


    public void fillListView() {
        try {

            List<FileModel> fileModelList = getFileModelList();
            Collections.reverse(fileModelList);
            VideoCompressAdapter videoCompressAdapter = getVideoCompressAdapter(fileModelList);
            videoCompressAdapter.setActivity((Activity) getView());
            Globals.selectionMode = false;
            Globals.selectedFiles.clear();
            if (fileModelList != null) {
                videoCompressAdapter.notifyDataSetChanged();
                viewHolder.lvFileModel.setAdapter(videoCompressAdapter);
            }
            viewHolder.lvFileModel.setEmptyView(viewHolder.tvNoDataFound);
        } catch (Exception ex) {

            LogManager.Log(className, ex);
        }
    }

    private List<FileModel> getFileModelList() {
        List<FileModel> list = null;
        try {
            list = getRepositoryFileModel().getAll();

        } catch (Exception ex) {
            LogManager.Log(className, ex);
        }
        return list;
    }

    private VideoCompressAdapter getVideoCompressAdapter(List<FileModel> fileModelList) {
        VideoCompressAdapter adapter = null;
        try {
            adapter = new VideoCompressAdapter(getContext(), R.layout.file_model_list, fileModelList);
        } catch (Exception ex) {

            LogManager.Log(className, ex);
        }
        return adapter;
    }

}
