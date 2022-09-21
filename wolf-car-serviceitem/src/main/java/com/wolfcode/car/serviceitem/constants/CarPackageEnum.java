package com.wolfcode.car.serviceitem.constants;

import lombok.Getter;

public enum CarPackageEnum {
    NO(0,"非套餐"),YES(1,"套餐");
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

    CarPackageEnum(Integer code,String desc){
        this.code=code;
        this.desc=desc;
    }
}
