package com.silversnowsoftware.vc.ui.list;

import android.content.Context;
import android.os.AsyncTask;
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

    private Context getContext() {
        return this.getApplicationContext();
    }

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

        DeleteFilesOperation deleteFilesOperation = new DeleteFilesOperation();
        deleteFilesOperation.execute();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_list;
    }

    private class DeleteFilesOperation extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            mPresenter.deleteSelectedFiles(new ArrayList<Integer>());
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(getContext(), getString(R.string.deleting));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dismissProgressDialog();
        }
    }
}
