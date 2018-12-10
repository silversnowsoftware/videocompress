package com.silversnowsoftware.vc.ui.list;

import android.os.Bundle;
import android.view.View;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.ui.base.BaseActivity;
import com.silversnowsoftware.vc.utils.SharedPref;
import com.silversnowsoftware.vc.utils.constants.Keys;

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

        SharedPref.RemoveKey(Keys.SELECTED_FILE_LIST, this);

        mPresenter.onAttach(this);
        mPresenter.setViewHolder();
        mPresenter.fillListView();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_list;
    }


}
