package com.silversnowsoftware.vc.ui.editor;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.listener.ICustomListener;
import com.silversnowsoftware.vc.utils.constants.Globals;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class EditorActivity extends Activity {

    @BindView(R.id.btnRun)
    Button btnRun;

    @Inject
    IEditorPresenter<IEditorView> mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editor);

    }

    @OnClick(R.id.btnRun)
    void btnRun_onClick()
    {
        Globals.FileModelList.get(0).getCustomListener(new ICustomListener() {
            @Override
            public void onSuccess(Double rate) {

            }

            @Override
            public void onProgress(Double rate) {
                Log.i("Progressss--->", String.valueOf(rate));

            }

            @Override
            public void onFailure(String error) {

            }
        });
        mPresenter.VideoCompress();
        Intent listActivity = new Intent(getApplicationContext(),ListActivity.class);
        startActivity(listActivity);
    }

}
