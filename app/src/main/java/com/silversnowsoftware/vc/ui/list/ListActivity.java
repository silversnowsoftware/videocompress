package com.silversnowsoftware.vc.ui.list;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.ui.base.BaseActivity;
import com.silversnowsoftware.vc.ui.base.BaseResponse;
import com.silversnowsoftware.vc.utils.SharedPref;
import com.silversnowsoftware.vc.utils.Types;
import com.silversnowsoftware.vc.utils.constants.Keys;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;


public class ListActivity extends BaseActivity implements IListView {


    @Inject
    IListPresenter<IListView> mPresenter;

    private Context getContext() {
        return getApplicationContext();
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


    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showToastMethod(String.valueOf(item.getItemId()));
        switch (item.getItemId()) {
            case R.id.action_delete:
                DeleteFilesOperation deleteFilesOperation = new DeleteFilesOperation();
                deleteFilesOperation.execute();
                mPresenter.fillListView();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DeleteFilesOperation extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            List<Integer> selectedFiles = (List<Integer>) SharedPref.getData(Keys.SELECTED_FILE_LIST, Types.getSelectedFileModelListType(), getContext());
            mPresenter.deleteSelectedFiles(selectedFiles);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            showProgressDialog(ListActivity.this, getString(R.string.deleting));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dismissProgressDialog();
        }
    }
}
