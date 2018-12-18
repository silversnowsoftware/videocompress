package com.silversnowsoftware.vc.ui.editor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.googlecode.mp4parser.authoring.Edit;
import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.model.listener.OnVideoTrimListener;
import com.silversnowsoftware.vc.model.logger.LogModel;
import com.silversnowsoftware.vc.ui.base.BaseActivity;
import com.silversnowsoftware.vc.ui.base.BaseResponse;
import com.silversnowsoftware.vc.ui.list.ListActivity;
import com.silversnowsoftware.vc.utils.Utility;
import com.silversnowsoftware.vc.utils.constants.Constants;
import com.silversnowsoftware.vc.utils.constants.Globals;
import com.silversnowsoftware.vc.utils.helpers.LogManager;

import java.util.List;

import javax.inject.Inject;

public class EditorActivity extends BaseActivity implements IEditorView {

    private static final String className = EditorActivity.class.getSimpleName();
    @Inject
    IEditorPresenter<IEditorView> mPresenter;
    EditorViewHolder meditorViewHolder;

    OnVideoTrimListener mOnVideoTrimListener = new OnVideoTrimListener() {
        @Override
        public void onTrimStarted() {
            showProgressDialog(EditorActivity.this, getString(R.string.video_trim));
        }

        @Override
        public void getResult(Uri uri) {

            dismissProgressDialog();

            finish();


            redirectToActivity(ListActivity.class);
        }

        @Override
        public void cancelAction() {
            dismissProgressDialog();
        }

        @Override
        public void onError(String message) {
            dismissProgressDialog();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        meditorViewHolder = new EditorViewHolder(this);
        getActivityComponent().inject(this);
        try {
            mPresenter.onAttach(this);
            mPresenter.setViewHolder();
            mPresenter.setVideoToVideoView();
        }
        catch (Exception ex)
        {

            LogManager.Log(className, ex);
        }

        meditorViewHolder.btnCompress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FileModel result = mPresenter.addSelectedFile();
                    if (!result.getSuccess()) {
                        return;
                    }

                    String dest = mPresenter.trimVideo(mOnVideoTrimListener);
                    result.setPath(dest);
                    mPresenter.updateModel(result);
                }
                catch (Exception ex){

                    LogManager.Log(className, ex);
                }

            }
        });
        meditorViewHolder.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.setDefaultEditor();
            }
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_editor;
    }
}
