package com.silversnowsoftware.vc.ui.base.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.FileModel;

import java.util.List;

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
        FileModel model = (FileModel) getItem(position);
        if (model != null) {
            TextView videoName, videoProgress;
            ImageView videoTumbnail;

            videoName = (TextView) v.findViewById(R.id.videoName);
            videoProgress = (TextView) v.findViewById(R.id.videoProgress);
            videoTumbnail = (ImageView) v.findViewById(R.id.videoTumbnail);

            videoName.setText(model.getName());
            videoProgress.setText(String.valueOf(model.getProgress()));
            videoTumbnail.setImageBitmap(model.getThumbnailBmp());

        }
        return v;

    }
}
