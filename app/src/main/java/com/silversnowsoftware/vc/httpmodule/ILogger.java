package com.silversnowsoftware.vc.httpmodule;

import com.silversnowsoftware.vc.model.logger.LogModel;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by burak on 12/16/2018.
 */

public interface ILogger {

    @POST("/api/logger")
    void Logger(@Body LogModel logData, Callback<String> callback);
}
