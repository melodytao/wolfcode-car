package com.wolfcode.car.appointment.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *  结算单项
 */
@Data
public class BusStatementItem implements Serializable {
    /**
     * 主键id
     */
    @JsonInclude
    private Long id;

    /**
     * 结算单ID
     */
    private Long statementId;

    /**
     * 服务项明细id
     */
    private Long itemId;

    /**
     * 服务项明细名称
     */
    private String itemName;

    /**
     * 服务项价格
     */
    private BigDecimal itemPrice;

    /**
     * 购买数量
     */
    private BigDecimal itemQuantity;
}
