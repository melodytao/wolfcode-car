package com.wolfcode.car.report.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wolfcode.car.common.core.domain.vo.QueryObject;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class ShopIncomeVo extends QueryObject {


    /**
     * 判断是年月日周的选择标识
     * 0按日 1按周 2按月 3按年
     */
    private Integer dateStatus=0;

}
