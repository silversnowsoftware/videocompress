package com.silversnowsoftware.vc.ui.list;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.model.listener.OnEventListener;
import com.silversnowsoftware.vc.ui.base.BaseActivity;
import com.silversnowsoftware.vc.ui.base.BaseResponse;
import com.silversnowsoftware.vc.utils.SharedPref;
import com.silversnowsoftware.vc.utils.Types;
import com.silversnowsoftware.vc.utils.constants.Keys;
import com.silversnowsoftware.vc.utils.helpers.FileHelper;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

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
        SharedPref.RemoveKey(Keys.SELECTION_MODE, this);

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
                deleteFilesOperation();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DeleteFilesOperationAsync extends AsyncTask<Void, Void, Boolean> {

        private OnEventListener<String> mCallBack;
        private Context mContext;
        public Exception mException;

        DeleteFilesOperationAsync(Context context, OnEventListener callback) {
            mCallBack = callback;
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean result = true;
            try {
                for (FileModel fileModel : getSelectedFiles()) {
                    File file = new File(fileModel.getPath());
                    if (file.exists()) {
                        //file.delete();

                        mPresenter.deleteSelectedFile(fileModel);
                    }
                }

            } catch (Exception ex) {
                mException = ex;
                result = false;
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(ListActivity.this, getString(R.string.deleting));
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            dismissProgressDialog();
            if (mCallBack != null) {
                if (mException == null) {
                    mCallBack.onSuccess(result);
                }
            } else {
                mCallBack.onFailure(mException);
            }
        }
    }

    private void deleteFilesOperation() {

        if (getSelectedFiles() != null) {

            DeleteFilesOperationAsync deleteFilesOperationAsync = new DeleteFilesOperationAsync(getContext(), new OnEventListener() {
                @Override
                public void onSuccess(Boolean object) {
                    mPresenter.fillListView();
                }

                @Override
                public void onFailure(Exception e) {
                    showToastMethod(getString(R.string.error));
                }
            });
            deleteFilesOperationAsync.execute();
        }
    }
}
