package com.silversnowsoftware.vc.ui.list;

import android.os.Bundle;
import android.view.View;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.ui.base.BaseActivity;
import com.silversnowsoftware.vc.ui.base.BaseResponse;
import com.silversnowsoftware.vc.utils.SharedPref;
import com.silversnowsoftware.vc.utils.constants.Keys;

import java.util.ArrayList;

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
        SharedPref.RemoveKey(Keys.HAS_LONG_CLICK, this);

        mPresenter.onAttach(this);
        mPresenter.setViewHolder();
        mPresenter.fillListView();
        BaseResponse response = mPresenter.deleteSelectedFiles(new ArrayList<Integer>());
        if (response.getSuccess())
        {
            showProgressDialog(this,getString(R.string.deleting));
            dismissProgressDialog();
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_list;
    }


}
