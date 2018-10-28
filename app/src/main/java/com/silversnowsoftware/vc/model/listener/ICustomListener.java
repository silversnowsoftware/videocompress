package com.silversnowsoftware.vc.model.listener;

/**
 * Created by burak on 10/25/2018.
 */

public interface ICustomListener {
    void onSuccess(Double rate);
    void onProgress(Double rate);
    void onFailure(String error);
}
