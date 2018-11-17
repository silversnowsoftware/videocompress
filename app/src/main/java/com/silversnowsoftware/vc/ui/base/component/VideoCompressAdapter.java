package com.silversnowsoftware.vc.ui.base.component;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.model.listener.ICustomListener;
import com.silversnowsoftware.vc.operations.compressor.FileCompressor;
import com.silversnowsoftware.vc.utils.constants.Keys;
import com.silversnowsoftware.vc.utils.enums.FileStatusEnum;
import com.silversnowsoftware.vc.utils.helpers.FileHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by burak on 10/29/2018.
 */

public class VideoCompressAdapter extends ArrayAdapter {
    public VideoCompressAdapter(@NonNull Context context, int resource, @NonNull List<FileModel> files) {
        super(context, resource, files);
    }

    Activity mActivity;

    public void setActivity(Activity activity) {
        mActivity = activity;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.file_model_list, null);

        }
        viewHolder = new ViewHolder(view);
        final FileModel model = (FileModel) getItem(position);
        if (model != null) {
            viewHolder.tvVideoName.setText(model.getName());
            viewHolder.ivVideoTumbnail.setImageBitmap(FileHelper.getBitmapFromBase64(model.getThumbnail()));
        }

        if (model.getFileStatus() == FileStatusEnum.PREPEARING) {
            model.setFileStatus(FileStatusEnum.PROGRESSING);
            final ViewHolder finalViewHolder = viewHolder;
            FileCompressor fc = new FileCompressor(mActivity);

            model.getCustomListener(new ICustomListener() {
                @Override
                public void onSuccess(Double rate) {
                    Log.i("Success Rate: ", String.valueOf(rate.intValue()));
                    finalViewHolder.pbProgress.setProgress(100);
                    model.setFileStatus(FileStatusEnum.SUCCESS);
                    notifyDataSetChanged();
                }

                @Override
                public void onProgress(Double rate) {

                    if (rate.intValue() > 0) {
                        Log.i("Rate: ", String.valueOf(rate.intValue()));
                        finalViewHolder.pbProgress.setProgress(rate.intValue());
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onFailure(String error) {
                    model.setFileStatus(FileStatusEnum.ERROR);
                }
            });
            fc.Compress(model);


        }

        return view;

    }

    static class ViewHolder {
        @BindView(R.id.tvVideoName)
        TextView tvVideoName;
        @BindView(R.id.pbProgress)
        ProgressBar pbProgress;
        @BindView(R.id.ivVideoTumbnail)
        ImageView ivVideoTumbnail;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
