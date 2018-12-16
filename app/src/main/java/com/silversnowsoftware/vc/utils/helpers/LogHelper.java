package com.silversnowsoftware.vc.utils.helpers;

import com.silversnowsoftware.vc.httpmodule.ILogger;
import com.silversnowsoftware.vc.model.logger.LogModel;
import com.silversnowsoftware.vc.utils.constants.Constants;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by burak on 12/16/2018.
 */

public class LogHelper {
    private RestAdapter restAdapter;

    public LogHelper() {
        restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.REST_BASE_SERVICE)
                .build();
    }

    public void Log(LogModel logModel) {

        ILogger logger = restAdapter.create(ILogger.class);
        logger.Logger(logModel, new Callback<String>() {


            @Override
            public void success(String s, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
