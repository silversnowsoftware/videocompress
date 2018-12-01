package com.silversnowsoftware.vc.ui.list;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.ui.base.BasePresenter;
import com.silversnowsoftware.vc.ui.base.component.VideoCompressAdapter;
import com.silversnowsoftware.vc.ui.main.IMainPresenter;
import com.silversnowsoftware.vc.utils.constants.Keys;

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
    ListViewHolder viewHolder;
    @Inject
    public ListPresenter() {
        super();

    }
    public void setViewHolder() {
        viewHolder = new ListViewHolder(getView());
    }

    public void fillListView() {

        List<FileModel> fileModelList = getFileModelList();
        VideoCompressAdapter videoCompressAdapter = getVideoCompressAdapter(fileModelList);
        videoCompressAdapter.setActivity((Activity) getView());

        if (fileModelList != null)
        viewHolder.lvFileModel.setAdapter(videoCompressAdapter);

        viewHolder.lvFileModel.setEmptyView(viewHolder.tvNoDataFound);
    }

    private List<FileModel> getFileModelList() {
        return (List<FileModel>) getData(Keys.FILE_LIST_KEY, getFileModelListType(), getContext());
    }

    private VideoCompressAdapter getVideoCompressAdapter(List<FileModel> fileModelList) {
        return new VideoCompressAdapter(getContext(), R.layout.file_model_list, fileModelList);
    }

}
