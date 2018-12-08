package com.silversnowsoftware.vc.ui.editor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.listener.OnVideoTrimListener;
import com.silversnowsoftware.vc.ui.base.BaseActivity;
import com.silversnowsoftware.vc.ui.base.BaseResponse;
import com.silversnowsoftware.vc.ui.list.ListActivity;

import java.util.List;

import javax.inject.Inject;

public class EditorActivity extends BaseActivity implements IEditorView {

    @Inject
    IEditorPresenter<IEditorView> mPresenter;
    EditorViewHolder meditorViewHolder;
    private ProgressDialog mProgressDialog;

    OnVideoTrimListener mOnVideoTrimListener = new OnVideoTrimListener() {
        @Override
        public void onTrimStarted() {
            // Create an indeterminate progress dialog
            mProgressDialog = new ProgressDialog(EditorActivity.this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setTitle("Trimming...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        public void getResult(Uri uri) {
            mProgressDialog.dismiss();
            Bundle conData = new Bundle();
            conData.putString("INTENT_VIDEO_FILE", uri.getPath());
            Intent intent = new Intent();
            intent.putExtras(conData);
            setResult(RESULT_OK, intent);
            finish();

            RedirectToActivity(ListActivity.class);
        }

        @Override
        public void cancelAction() {
            mProgressDialog.dismiss();
        }

        @Override
        public void onError(String message) {
            mProgressDialog.dismiss();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        meditorViewHolder = new EditorViewHolder(this);
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        mPresenter.setViewHolder();
        mPresenter.setVideoToVideoView();

        meditorViewHolder.btnCompress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseResponse result = mPresenter.addSelectedFile();
                if (!result.getSuccess()) {
                    showToastMethod(result.getMessage());
                    return;
                }

                mPresenter.trimVideo(mOnVideoTrimListener);

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
