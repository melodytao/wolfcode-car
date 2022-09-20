package com.wolfcode.car.appointment.constants;

import lombok.Getter;

public enum  ServiceTypeEnum {
    REPAIR(0,"维修"),
    MAINTAIN(1,"保养");
    /**
     * 状态码
     */
    @Getter
    private Integer code;
    /**
     * 描述
     */
    @Getter
    private String desc;

    ServiceTypeEnum(Integer code,String desc){
        this.code=code;
        this.desc=desc;
    }
}
