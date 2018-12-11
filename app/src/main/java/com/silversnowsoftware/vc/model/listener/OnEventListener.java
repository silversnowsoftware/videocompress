package com.silversnowsoftware.vc.model.listener;

/**
 * Created by burak on 12/11/2018.
 */

public interface OnEventListener<T> {
    public void onSuccess(Boolean object);
    public void onFailure(Exception e);
}