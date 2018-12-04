package com.silversnowsoftware.vc.ui.editor;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.ui.base.BaseActivity;

import javax.inject.Inject;

public class EditorActivity extends BaseActivity implements IEditorView {

    @Inject
    IEditorPresenter<IEditorView> mPresenter;

    EditorViewHolder meditorViewHolder;

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
                String result = mPresenter.AddFileData();
                if (!result.isEmpty()) {
                    showToastMethod(result);
                }
            }
        });

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_editor;
    }
}
