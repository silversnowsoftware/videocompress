package com.silversnowsoftware.vc.ui.base.component;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.ui.editor.EditorActivity;
import com.silversnowsoftware.vc.utils.helpers.FileHelper;

import java.util.List;

import static com.silversnowsoftware.vc.utils.SharedPref.putData;

/**
 * Created by burak on 10/29/2018.
 */

public class VideoCompressAdapter extends ArrayAdapter {
    public VideoCompressAdapter(@NonNull Context context, int resource, @NonNull List<FileModel> files) {
        super(context, resource, files);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.file_model_list, null);

        }
        final FileModel model = (FileModel) getItem(position); //LinkedTreeMap cannot be cast to com.silversnowsoftware.vc.model.FileMod
        if (model != null) {
            TextView videoName, videoProgress;
            ImageView videoTumbnail;
            ProgressBar pbProgress;

            videoName = v.findViewById(R.id.tvVideoName);

            videoTumbnail = v.findViewById(R.id.videoTumbnail);
            pbProgress = v.findViewById(R.id.pbProgress);

            videoName.setText(model.getName());

            pbProgress.setProgress(55);
            //videoTumbnail.setImageBitmap(FileHelper.getBitmapFromBase64(model.getThumbnail()));

         /*   v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent editorActivity = new Intent(getContext(), EditorActivity.class);
                    putData("SelectedFile", model, getContext());
                    getContext().startActivity(editorActivity);
                }
            });*/

            /*Intent intent = new Intent(context,EditorActivity.class);
            intent.pu*/
        }
        return v;

    }
}
