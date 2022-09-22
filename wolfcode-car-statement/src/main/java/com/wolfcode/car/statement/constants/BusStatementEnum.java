package com.wolfcode.car.statement.constants;


import lombok.Getter;

public enum BusStatementEnum {
    CONSUME(0,"消费中"),
    PAID(1,"已支付");
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

    BusStatementEnum(Integer code, String desc){
        this.code=code;
        this.desc=desc;
    }
}
