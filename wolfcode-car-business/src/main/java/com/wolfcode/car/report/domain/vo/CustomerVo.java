package com.wolfcode.car.report.domain.vo;

import com.wolfcode.car.common.core.domain.vo.QueryObject;
import lombok.Data;

@Data
public class CustomerVo extends QueryObject {
    /**
     * 用户名
     */
    private String customerName;

    /**
     * 电话号码
     */
    private String customerPhone;
}
