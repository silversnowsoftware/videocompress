package com.silversnowsoftware.vc.utils.helpers;

import com.silversnowsoftware.vc.httpmodule.ILogger;
import com.silversnowsoftware.vc.model.logger.LogModel;
import com.silversnowsoftware.vc.utils.Utility;
import com.silversnowsoftware.vc.utils.constants.Constants;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by burak on 12/16/2018.
 */

public class LogHelper {
    private static RestAdapter restAdapter;

    private static RestAdapter getRestAdapter() {
        if (restAdapter == null) {
            return restAdapter = new RestAdapter.Builder()
                    .setEndpoint(Constants.REST_BASE_SERVICE)
                    .build();
        }
        return restAdapter;
    }


    public static void Log(String className, Exception ex) {

        LogModel logModel = new LogModel.LogBuilder()
                .apiVersion(Utility.getAndroidVersion())
                .appName(Constants.APP_NAME)
                .className(className)
                .errorMessage(ex.getMessage())
                .methodName(ex.getStackTrace()[0].getMethodName())
                .stackTrace(ex.getStackTrace().toString())
                .build();

        ILogger logger = getRestAdapter().create(ILogger.class);
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
