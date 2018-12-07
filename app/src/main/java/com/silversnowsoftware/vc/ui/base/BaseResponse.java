package com.silversnowsoftware.vc.ui.base;

/**
 * Created by burak on 12/7/2018.
 */

public class BaseResponse {

        private boolean isSuccess;
        private String message;

    public boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
