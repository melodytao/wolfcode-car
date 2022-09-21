package com.wolfcode.car.serviceitem.constants;

import lombok.Getter;

public enum SaleStatusEnum {
    OFF(0,"下架"),
    ON(1,"上架");
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

    SaleStatusEnum(Integer code, String desc){
        this.code=code;
        this.desc=desc;
    }
}
