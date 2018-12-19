package com.silversnowsoftware.vc.model.logger;

import java.util.HashMap;

/**
 * Created by burak on 12/16/2018.
 */

public class LogModel {
    public String AppName;
    public String ClassName;
    public String MethodName;
    public String ErrorMessage;
    public String StackTrace;
    public String ApiVersion;
    public long Latitude;
    public long Longitude;
    public HashMap<String,String> Params;



    public LogModel(LogBuilder logBuilder) {
        this.AppName = logBuilder.AppName;
        this.ClassName = logBuilder.ClassName;
        this.MethodName = logBuilder.MethodName;
        this.ErrorMessage = logBuilder.ErrorMessage;
        this.StackTrace = logBuilder.StackTrace;
        this.ApiVersion = logBuilder.ApiVersion;
        this.Latitude = logBuilder.Latitude;
        this.Longitude = logBuilder.Longitude;
        this.Params = logBuilder.Params;
    }

    public static class LogBuilder {

        private String AppName;
        private String ClassName;
        private String MethodName;
        private String ErrorMessage;
        private String StackTrace;
        private String ApiVersion;
        private long Latitude;
        private long Longitude;
        private HashMap<String,String> Params;

        public LogBuilder appName(String appName) {
            this.AppName = appName;
            return this;
        }
        public LogBuilder className(String className) {
            this.ClassName = className;
            return this;
        }

        public LogBuilder methodName(String methodName) {
            this.MethodName = methodName;
            return this;
        }

        public LogBuilder errorMessage(String errorMessage) {
            this.ErrorMessage = errorMessage;
            return this;
        }

        public LogBuilder stackTrace(String stackTrace) {
            this.StackTrace = stackTrace;
            return this;
        }

        public LogBuilder apiVersion(String apiVersion) {
            this.ApiVersion = apiVersion;
            return this;
        }

        public LogBuilder latitude(long latitude) {
            this.Latitude = latitude;
            return this;
        }

        public LogBuilder longitude(long longitude) {
            this.Longitude = longitude;
            return this;
        }

        public LogBuilder params(HashMap<String,String> params) {
            this.Params = params;
            return this;
        }



        public LogModel build() {
            LogModel logModel = new LogModel(this);
            return logModel;
        }
    }


}
