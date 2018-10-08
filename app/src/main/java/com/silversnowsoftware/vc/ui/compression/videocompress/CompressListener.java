package com.silversnowsoftware.vc.ui.compression.videocompress;


public interface CompressListener {
    public void onExecSuccess(String message);
    public void onExecFail(String reason);
    public void onExecProgress(String message);
}
