package com.wolfcode.car.report.domain.info;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StatisticalConsumptionInfo {
    /**
     * 统计日期
     */
    private String statisticalDate;

    /**
     * 统计订单数量
     */
    private Long orderCount;
}
