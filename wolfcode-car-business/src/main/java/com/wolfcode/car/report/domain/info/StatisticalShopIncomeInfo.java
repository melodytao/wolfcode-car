package com.wolfcode.car.report.domain.info;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StatisticalShopIncomeInfo {
    /**
     * 统计日期
     */
    private String statisticalDate;

    /**
     * 统计店铺收入
     */
    private BigDecimal statisticalAmount;
}
