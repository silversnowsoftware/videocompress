package com.silversnowsoftware.vc.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.ui.base.BaseActivity;
import com.silversnowsoftware.vc.ui.compression.permission.PermissionsActivity;
import com.silversnowsoftware.vc.ui.compression.permission.PermissionsChecker;
import com.silversnowsoftware.vc.ui.editor.EditorActivity;
import com.silversnowsoftware.vc.utils.ManifestUtil;
import com.silversnowsoftware.vc.utils.constants.Constants;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.silversnowsoftware.vc.utils.constants.Globals.handler;


public class MainActivity extends BaseActivity implements IMainView {




    static ArrayList<String> MediasPaths = new ArrayList<>();

    @Inject
    IMainPresenter<IMainView> mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        handler = new Handler();
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        mPresenter.setViewHolder();
        //progressBar.setMax(100);

        PermissionsChecker mChecker = new PermissionsChecker(getApplicationContext());
        if (mChecker.lacksPermissions(ManifestUtil.PERMISSIONS)) {
            PermissionsActivity.startActivityForResult(this, Constants.REQUEST_CODE_FOR_PERMISSIONS, ManifestUtil.PERMISSIONS);
        }
        ButterKnife.bind(this);

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mPresenter.collectFiles(data);
            startActivity(new Intent(getApplicationContext(), EditorActivity.class));
        }
    }





}
