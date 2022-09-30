package com.wolfcode.car.report.domain.vo;

import com.wolfcode.car.common.core.domain.vo.QueryObject;
import lombok.Data;

/**
 * 消费单
 */
@Data
public class ConsumptionVo extends QueryObject {
    /**
     * 判断是年月日周的选择标识
     * 0按日 1按周 2按月 3按年
     */
    private Integer dateStatus=0;

    //预约单还是服务单
    private Integer serviceStatus=0; //0结算单，1预约单
}
