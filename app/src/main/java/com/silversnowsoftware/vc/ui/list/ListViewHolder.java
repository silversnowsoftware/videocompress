package com.silversnowsoftware.vc.ui.list;

import android.app.Activity;
import android.widget.ListView;
import android.widget.TextView;

import com.silversnowsoftware.vc.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by burak on 11/16/2018.
 */

public class ListViewHolder<V extends IListView> {
    Activity activity;

    @BindView(R.id.tvNoDataFound)
    public TextView tvNoDataFound;

    @BindView(R.id.lvFileModel)
    public ListView lvFileModel;

    public ListViewHolder(V activity) {
        this.activity = (Activity) activity;
        ButterKnife.bind(this, this.activity);
    }
}
