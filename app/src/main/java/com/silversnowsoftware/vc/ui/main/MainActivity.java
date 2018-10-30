package com.silversnowsoftware.vc.ui.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.databinding.ActivityMainBinding;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.model.listener.ICustomListener;
import com.silversnowsoftware.vc.ui.base.BaseActivity;
import com.silversnowsoftware.vc.ui.compression.permission.PermissionsActivity;
import com.silversnowsoftware.vc.ui.compression.permission.PermissionsChecker;
import com.silversnowsoftware.vc.ui.compression.videorecord.CameraActivity;
import com.silversnowsoftware.vc.ui.list.ListActivity;
import com.silversnowsoftware.vc.utils.ManifestUtil;
import com.silversnowsoftware.vc.utils.constants.Constants;
import com.silversnowsoftware.vc.utils.constants.Globals;
import com.silversnowsoftware.vc.utils.helpers.FileHelper;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.silversnowsoftware.vc.utils.constants.Globals.handler;


public class MainActivity extends BaseActivity implements IMainView {


    ActivityMainBinding mBinding;
    static ArrayList<String> MediasPaths = new ArrayList<>();

    @Inject
    IMainPresenter<IMainView> mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        handler = new Handler();
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, getLayoutResourceId());
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        mBinding.progressBar.setMax(100);

        mBinding.btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraActivity.startActivityForResult(MainActivity.this, Constants.REQUEST_CODE_FOR_RECORD_VIDEO);
            }
        });

        mBinding.btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Globals.FileModelList.get(0).getCustomListener(new ICustomListener() {
                    @Override
                    public void onSuccess(Double rate) {

                    }

                    @Override
                    public void onProgress(Double rate) {
                        Log.i("Progressss--->", String.valueOf(rate));
                        mBinding.progressBar.setProgress(rate.intValue());
                    }

                    @Override
                    public void onFailure(String error) {

                    }
                });
                mPresenter.VideoCompress();
            }
        });

        mBinding.btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mediaIntent = new Intent(
                        Intent.ACTION_GET_CONTENT
                        //,Uri.parse(Environment.DIRECTORY_DCIM)
                );
                // mediaIntent.setType("*/*");
                mediaIntent.setType("video/*");
                mediaIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"video/*"});
                mediaIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(mediaIntent, 1);

            }
        });
        mBinding.btnListFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent listActivity = new Intent(getApplicationContext(),ListActivity.class);
                startActivity(listActivity);
            }
        });
        PermissionsChecker mChecker = new PermissionsChecker(getApplicationContext());
        if (mChecker.lacksPermissions(ManifestUtil.PERMISSIONS)) {
            PermissionsActivity.startActivityForResult(this, Constants.REQUEST_CODE_FOR_PERMISSIONS, ManifestUtil.PERMISSIONS);
        }

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //mPresenter.ActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //MediaStore.Video.Thumbnails.getThumbnail()
            MediasPaths = null;
            ArrayList<String> tempfilepath = FileHelper.GetAllPath(getApplicationContext(), data);
            FileModel mdl = null;
            for (String item : tempfilepath) {
                mdl = new FileModel();
                mdl.setName(item);




                Bitmap bitmap = null;
                try {
                    bitmap = FileHelper.retriveVideoFrameFromVideo(item);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                if (bitmap != null) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, 240, 240, false);
                    //mBinding.imageView.setImageBitmap(bitmap);
                    mdl.setThumbnailBmp(bitmap);
                }
                Globals.FileModelList.add(mdl);
            }
        }
    }


}
