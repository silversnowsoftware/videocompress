package com.silversnowsoftware.vc.ui.base.component;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.model.listener.ICustomListener;
import com.silversnowsoftware.vc.operations.compressor.FileCompressor;
import com.silversnowsoftware.vc.utils.SharedPref;
import com.silversnowsoftware.vc.utils.Types;
import com.silversnowsoftware.vc.utils.constants.Globals;
import com.silversnowsoftware.vc.utils.constants.Keys;
import com.silversnowsoftware.vc.utils.enums.FileStatusEnum;
import com.silversnowsoftware.vc.utils.helpers.FileHelper;

import java.lang.reflect.Type;
import java.security.Key;
import java.util.ArrayList;
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

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.file_model_list, null);

        }
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder._view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                viewHolder.ivSelectRow.setVisibility(View.VISIBLE);
                viewHolder.selectRow.setBackgroundResource(R.color.selectedListItemColor);
                Globals.selectedFiles.remove((Object)viewHolder.getId());
                SharedPref.putData(Keys.SELECTION_MODE, true, getContext());
                notifyDataSetChanged();

                return true;
            }
        });
        viewHolder._view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean selectionMode = (Boolean) SharedPref.getData(Keys.SELECTION_MODE, Types.getHasLongClickType(), getContext());
                if(selectionMode != null && selectionMode) {
                    Globals.selectedFiles = (ArrayList<Integer>) SharedPref.getData(Keys.SELECTED_FILE_LIST, Types.getSelectedFileModelListType(), getContext());

                    if (Globals.selectedFiles == null)
                        Globals.selectedFiles = new ArrayList<Integer>();

                    if (viewHolder.getSelected()) {
                        viewHolder.setSelected(false);
                        viewHolder.ivSelectRow.setVisibility(View.GONE);
                        viewHolder.selectRow.setBackgroundColor(Color.WHITE);
                        Globals.selectedFiles.remove((Object) viewHolder.getId());
                    } else {
                        viewHolder.setSelected(true);
                        viewHolder.ivSelectRow.setVisibility(View.VISIBLE);
                        viewHolder.selectRow.setBackgroundResource(R.color.selectedListItemColor);
                        Globals.selectedFiles.add(viewHolder.getId());
                    }

                    SharedPref.putData(Keys.SELECTED_FILE_LIST, Globals.selectedFiles, getContext());
                }
            }
        });

        final FileModel model = (FileModel) getItem(position);
        if (model != null) {
            viewHolder.setId(model.getId());
            viewHolder.tvVideoName.setText(model.getName());
            viewHolder.ivVideoTumbnail.setImageBitmap(FileHelper.getBitmapFromBase64(model.getThumbnail()));
            viewHolder.tvResolution.setText(model.getResolution());

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
        View _view;

        private int _id;

        @BindView(R.id.tvVideoName)
        TextView tvVideoName;
        @BindView(R.id.pbProgress)
        ProgressBar pbProgress;
        @BindView(R.id.ivVideoTumbnail)
        ImageView ivVideoTumbnail;
        @BindView(R.id.tvResolution)
        TextView tvResolution;
        @BindView(R.id.ivSelectRow)
        ImageView ivSelectRow;
        @BindView(R.id.selectRow)
        LinearLayout selectRow;

        private boolean isSelected;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            _view = view;
        }

        public boolean getSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public int getId() {
            return _id;
        }

        public void setId(int id) {
            this._id = id;
        }
    }
}
