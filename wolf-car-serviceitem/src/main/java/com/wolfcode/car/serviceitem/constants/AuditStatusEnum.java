package com.wolfcode.car.serviceitem.constants;

import lombok.Getter;

public enum AuditStatusEnum {
    INIT(0,"初始化"),
    AUDITING(1,"审核中"),
    APPROVED(2,"审核通过"),
    REPLY(3,"审批拒绝"),
    NOREQUIRED(4,"无需审核");
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

    AuditStatusEnum(Integer code, String desc){
        this.code=code;
        this.desc=desc;
    }
}
