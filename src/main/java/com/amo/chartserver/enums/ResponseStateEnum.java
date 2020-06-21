package com.amo.chartserver.enums;

import com.amo.chartserver.util.exception.BusinessException;

/**
 * 业务上的成功/失败
 */
public enum ResponseStateEnum {
    SUCCESS(1000, "成功"), FAILED(1001, "失败");

    private int code;
    private String name;

    ResponseStateEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ResponseStateEnum getByCode(int code) throws Exception {
        for (ResponseStateEnum stateEnum : values()) {
            if (stateEnum.code == code) return stateEnum;
        }
        throw new BusinessException("没有改类型");
    }
}
