package com.silversnowsoftware.vc.ui.list;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.model.listener.OnEventListener;
import com.silversnowsoftware.vc.model.logger.LogModel;
import com.silversnowsoftware.vc.ui.base.BaseActivity;
import com.silversnowsoftware.vc.ui.main.MainActivity;
import com.silversnowsoftware.vc.utils.SharedPref;
import com.silversnowsoftware.vc.utils.Utility;
import com.silversnowsoftware.vc.utils.constants.Constants;
import com.silversnowsoftware.vc.utils.constants.Keys;
import com.silversnowsoftware.vc.utils.helpers.LogManager;

import java.io.File;

import javax.inject.Inject;

import butterknife.ButterKnife;


public class ListActivity extends BaseActivity implements IListView {
    private static final String className = ListActivity.class.getSimpleName();

    @Inject
    IListPresenter<IListView> mPresenter;
    ListViewHolder mListViewHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        getActivityComponent().inject(this);
        ButterKnife.bind(this);

        try {

            mListViewHolder = new ListViewHolder(this);

            MobileAds.initialize(this, "ca-app-pub-9069451453527664~1459246129");
            mListViewHolder.mInterstitialAd = new InterstitialAd(this);
            mListViewHolder.mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                }

                @Override
                public void onAdLeftApplication() {
                    super.onAdLeftApplication();
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();

                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    mListViewHolder.mInterstitialAd.show();
                }

                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                }
            });
            mListViewHolder.mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
            mListViewHolder.mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("B3E228E2A3DF6402D6DCF40712D066F6").build());


            AdRequest adRequest = new AdRequest.Builder().addTestDevice("B3E228E2A3DF6402D6DCF40712D066F6").build();
            mListViewHolder.adViewList.loadAd(adRequest);
            mListViewHolder.adViewList.setAdListener(new AdListener());


            mPresenter.onAttach(this);
            mPresenter.setViewHolder();
            mPresenter.deleteErrorFileModel();
            mPresenter.fillListView();
        } catch (Exception ex) {

            LogManager.Log(className, ex);
        }


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

        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteFilesOperation();
                break;
            case R.id.action_share:
                mPresenter.shareVideoFiles(getSelectedFiles());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
                        file.delete();
                    }
                    mPresenter.deleteSelectedFile(fileModel);
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

        if (getSelectedFiles() != null && getSelectedFiles().size() > 0) {

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
        } else {
            alertDialog(ListActivity.this, getString(R.string.Alert), getString(R.string.ChooseAnyVideoForDelete));
        }
    }

}
