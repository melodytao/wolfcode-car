package com.wolfcode.car.audit.domain.vo;

import com.wolfcode.car.common.core.domain.vo.QueryObject;
import lombok.Data;

@Data
public class BusCarPackageAuditVo extends QueryObject {
    /**
     * 套餐审核id
     */
    private Long id;
    /**
     * 审核状态：同意，拒绝
     */
    private boolean auditStatus;
    /**
     * 备注
     */
    private String info;
}
