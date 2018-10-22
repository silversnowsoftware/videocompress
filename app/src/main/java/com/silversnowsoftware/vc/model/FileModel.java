package com.silversnowsoftware.vc.model;

import android.support.annotation.NonNull;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.silversnowsoftware.vc.utils.constants.Globals;
import com.silversnowsoftware.vc.utils.enums.FileStatusEnum;
import com.silversnowsoftware.vc.utils.enums.MediaTypeEnum;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by burak on 10/17/2018.
 */

public class FileModel implements Serializable {
    public FileModel() {
        Attributes = new FileAttributes();
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

    public String getThumbnail() {
        return Thumbnail;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
