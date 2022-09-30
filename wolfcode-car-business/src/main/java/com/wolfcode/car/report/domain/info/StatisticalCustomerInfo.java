package com.wolfcode.car.report.domain.info;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StatisticalCustomerInfo {
    /**
     * 电话号码
     */
    private String customerPhone;

    /**
     * 消费金额
     */
    private BigDecimal statisticalAmount;
}
