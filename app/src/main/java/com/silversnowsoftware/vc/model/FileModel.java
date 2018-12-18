package com.silversnowsoftware.vc.model;

import android.graphics.Bitmap;

import com.j256.ormlite.field.DatabaseField;
import com.silversnowsoftware.vc.model.listener.ICustomListener;
import com.silversnowsoftware.vc.ui.base.BaseResponse;
import com.silversnowsoftware.vc.utils.constants.Globals;
import com.silversnowsoftware.vc.utils.enums.FileStatusEnum;
import com.silversnowsoftware.vc.utils.enums.MediaTypeEnum;
import com.silversnowsoftware.vc.utils.helpers.FileHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by burak on 10/17/2018.
 */

public class FileModel extends BaseResponse implements Serializable  {
    public ICustomListener listener;

    public FileModel() {
        FileStatus = FileStatusEnum.NONE;
    }

    public void getCustomListener(ICustomListener listener) {
        this.listener = listener;
    }

    @DatabaseField(columnName = "Id", generatedId = true)
    private int id;
    @DatabaseField(columnName = "Name")
    private String Name;
    @DatabaseField(columnName = "Path")
    private String Path;
    @DatabaseField(columnName = "FileSize")
    private Long FileSize;
    @DatabaseField(columnName = "CreateDate")
    private Date CreateDate;
    @DatabaseField(columnName = "Thumbnail")
    private String Thumbnail;
    @DatabaseField(columnName = "Extension")
    private String Extension;
    @DatabaseField(columnName = "FileStatus")
    private FileStatusEnum FileStatus;
    @DatabaseField(columnName = "MediaType")
    private MediaTypeEnum MediaType;
    @DatabaseField(columnName = "Resolution")
    private String Resolution;
    private Double VideoLength;
    private Double Progress;


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public Long getFileSize() {
        return FileSize;
    }

    public void setFileSize(Long fileSize) {
        FileSize = fileSize;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(String bytes) {
        Thumbnail = bytes;
    }


    public String getExtension() {
        return Extension;
    }

    public void setExtension(String extension) {
        Extension = extension;
    }

    public FileStatusEnum getFileStatus() {
        return FileStatus;
    }

    public void setFileStatus(FileStatusEnum fileStatus) {
        FileStatus = fileStatus;
    }

    public MediaTypeEnum getMediaType() {
        return MediaType;
    }

    public void setMediaType(MediaTypeEnum mediaType) {
        MediaType = mediaType;
    }


    public String getCompressCmd() {
        return "-y -i " + this.Path + " -strict -2 -vcodec libx264 -preset ultrafast " +
                "-crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s " + getResolution() + " -aspect 16:9 " + Globals.currentOutputVideoPath + this.Name;
    }

    public Double getVideoLength() {
        return VideoLength;
    }

    public void setVideoLength(Double videoLength) {
        VideoLength = videoLength;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResolution() {
        return Resolution;
    }

    public void setResolution(String resolution) {
        Resolution = resolution;
    }


    public Double getProgress() {
        return Progress;
    }

    public void setProgress(Double progress) {
        Progress = progress;
    }



    @Override
    public boolean equals(Object obj) {
        return this.Name.equals(((FileModel) obj).Name);
    }

    public Date getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(Date createDate) {
        CreateDate = createDate;
    }
}
