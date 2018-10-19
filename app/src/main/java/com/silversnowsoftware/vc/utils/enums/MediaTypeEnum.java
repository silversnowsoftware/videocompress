package com.silversnowsoftware.vc.utils.enums;

/**
 * Created by burak on 10/17/2018.
 */

public enum MediaTypeEnum {

    VIDEO(0),
    PICTURE(1),
    GIF(2);

    private final int value;

    private MediaTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
