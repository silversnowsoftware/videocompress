package com.silversnowsoftware.vc.ui.editor;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.ui.base.BaseActivity;

import javax.inject.Inject;

public class EditorActivity extends BaseActivity implements IEditorView {

    @Inject
    IEditorPresenter<IEditorView> mPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        mPresenter.setViewHolder();
        mPresenter.setVideoToVideoView();


    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_editor;
    }
}
