package com.silversnowsoftware.vc.model;

import android.graphics.Bitmap;

import com.j256.ormlite.field.DatabaseField;
import com.silversnowsoftware.vc.model.listener.ICustomListener;
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

public class FileModel implements Serializable {
    public ICustomListener listener;

    public FileModel() {

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
    @DatabaseField(columnName = "ConvertTime")
    private Date ConvertTime;
    @DatabaseField(columnName = "Thumbnail")
    private String Thumbnail;
    @DatabaseField(columnName = "Extension")
    private String Extension;
    @DatabaseField(columnName = "FileStatus")
    private FileStatusEnum FileStatus;
    @DatabaseField(columnName = "MediaType")
    private MediaTypeEnum MediaType;
    private String ResolutionX;
    private String ResolutionY;
    private Double VideoLength;
    private Double Progress;
    private Bitmap ThumbnailBmp;


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

    public Date getConvertTime() {
        return ConvertTime;
    }

    public void setConvertTime(Date convertTime) {
        ConvertTime = convertTime;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public Bitmap getThumbnailBmp() {
        return ThumbnailBmp;
    }

    public void setThumbnailBmp(Bitmap bitmap) {
        ThumbnailBmp = bitmap;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
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
                "-crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s " + getResolutionX() + "x" + getResolutionY() + " -aspect 16:9 " + Globals.currentOutputVideoPath + this.Name;
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

    public String getResolutionX() {
        return ResolutionX;
    }

    public void setResolutionX(String resolutionX) {
        ResolutionX = resolutionX;
    }

    public String getResolutionY() {
        return ResolutionY;
    }

    public void setResolutionY(String resolutionY) {
        ResolutionY = resolutionY;
    }

    public Double getProgress() {
        return Progress;
    }

    public void setProgress(Double progress) {
        Progress = progress;
    }
}
