package com.wolfcode.car.audit.constants;

import lombok.Getter;

public enum CarPackageAuditEnum {
    AUDITING(0,"审核中"),
    APPROVED(1,"审核通过"),
    REJECT(2,"审批拒绝"),
    CANCEL(3,"审批撤销");
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

    CarPackageAuditEnum(Integer code, String desc){
        this.code=code;
        this.desc=desc;
    }
}
