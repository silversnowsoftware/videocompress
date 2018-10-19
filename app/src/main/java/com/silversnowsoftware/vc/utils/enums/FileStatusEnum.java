package com.silversnowsoftware.vc.utils.enums;

/**
 * Created by burak on 10/17/2018.
 */

public enum FileStatusEnum {


    PREPEARING(1),
    PROGRESSING(2),
    SUCCESS(3),
    ERROR(4),
    CANCELED(5);

    private final int value;

    private FileStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


}
