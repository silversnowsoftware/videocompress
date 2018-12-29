package com.silversnowsoftware.vc.ui.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.httpmodule.ILogger;
import com.silversnowsoftware.vc.model.logger.LogModel;
import com.silversnowsoftware.vc.ui.base.BaseActivity;
import com.silversnowsoftware.vc.ui.compression.permission.PermissionsActivity;
import com.silversnowsoftware.vc.ui.compression.permission.PermissionsChecker;
import com.silversnowsoftware.vc.ui.compression.videorecord.CameraActivity;
import com.silversnowsoftware.vc.ui.editor.EditorActivity;
import com.silversnowsoftware.vc.ui.list.ListActivity;
import com.silversnowsoftware.vc.utils.ManifestUtil;
import com.silversnowsoftware.vc.utils.Utility;
import com.silversnowsoftware.vc.utils.constants.Constants;
import com.silversnowsoftware.vc.utils.helpers.LogManager;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.silversnowsoftware.vc.utils.constants.Globals.handler;
import static java.lang.System.exit;


public class MainActivity extends BaseActivity implements IMainView {


    @Inject
    IMainPresenter<IMainView> mPresenter;
    MainViewHolder mainViewHolder;
    private static final String className = MainActivity.class.getSimpleName();
    AdListener mAdListener = new AdListener() {};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        handler = new Handler();
        super.onCreate(savedInstanceState);
        mainViewHolder = new MainViewHolder(this);

        MobileAds.initialize(this, "ca-app-pub-9069451453527664~1459246129");
        AdRequest adRequest = new AdRequest.Builder().build();
        mainViewHolder.adViewMain.loadAd(adRequest);
        mainViewHolder.adViewMain.setAdListener(mAdListener);
        progressBarDialog(MainActivity.this);
        try {

            getActivityComponent().inject(this);
            mPresenter.onAttach(this);
            mPresenter.deleteErrorFileModel();

            PermissionsChecker mChecker = new PermissionsChecker(getApplicationContext());
            if (mChecker.lacksPermissions(ManifestUtil.PERMISSIONS)) {
                PermissionsActivity.startActivityForResult(this, Constants.REQUEST_CODE_FOR_PERMISSIONS, ManifestUtil.PERMISSIONS);
            }
            ButterKnife.bind(this);


            mainViewHolder.btnChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mPresenter.chooseFile();

                }
            });

            mainViewHolder.btnListFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    redirectToActivity(ListActivity.class);
                }
            });

            mainViewHolder.btnRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirectToActivity(CameraActivity.class);
                }
            });
        } catch (Exception ex) {

            LogManager.Log(className, ex);
        }


    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //exit(0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mPresenter.collectFiles(data);
            finish();
            startActivity(new Intent(getApplicationContext(), EditorActivity.class));
        }
    }


}
