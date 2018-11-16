package com.silversnowsoftware.vc.ui.list;
import android.os.Bundle;
import android.view.View;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.ui.base.BaseActivity;
import javax.inject.Inject;
import butterknife.ButterKnife;


public class ListActivity extends BaseActivity implements IListView {


    @Inject
    IListPresenter<IListView> mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        getActivityComponent().inject(this);
        ButterKnife.bind(this);

        mPresenter.onAttach(this);
        mPresenter.setViewHolder(this);
        mPresenter.fillListView();

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_list;
    }


}
