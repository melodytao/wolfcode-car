package com.wolfcode.car.appointment.constants;


import lombok.Getter;

public enum BusAppointmentEnum {
    APPOINTMENT(0,"预约中"),
    ARRIVAL(1,"已到店"),
    CANCEL(2,"用户取消"),
    OVERTIME(3,"超时取消"),
    SETTLE(4,"已结算"),
    PAID(5,"已支付");
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

    BusAppointmentEnum(Integer code,String desc){
        this.code=code;
        this.desc=desc;
    }
}
