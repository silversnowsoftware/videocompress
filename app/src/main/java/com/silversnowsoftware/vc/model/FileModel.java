package com.silversnowsoftware.vc.model;

import android.support.annotation.NonNull;

import com.silversnowsoftware.vc.utils.constants.Globals;
import com.silversnowsoftware.vc.utils.enums.FileStatusEnum;
import com.silversnowsoftware.vc.utils.enums.MediaTypeEnum;

import java.io.File;
import java.util.Date;

/**
 * Created by burak on 10/17/2018.
 */

public class FileModel {
    public FileModel() {
        Attributes = new FileAttributes();
    }

    private String Name;
    private String Path;
    private Long FileSize;
    private Date ConvertTime;
    private String ThumbNail;
    private String Extension;
    private FileStatusEnum FileStatus;
    private MediaTypeEnum MediaType;
    private Double VideoLength;
    private  FileAttributes Attributes;



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

    public String getThumbNail() {
        return ThumbNail;
    }

    public void setThumbNail(String thumbNail) {
        ThumbNail = thumbNail;
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


    public  String getCompressCmd()
    {
        return "-y -i " + Path + " -strict -2 -vcodec libx264 -preset ultrafast " +
                "-crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s 858x480 -aspect 16:9 " + Globals.currentOutputVideoPath + Name;
    }

    public Double getVideoLength() {
        return VideoLength;
    }

    public void setVideoLength(Double videoLength) {
        VideoLength = videoLength;
    }
}
