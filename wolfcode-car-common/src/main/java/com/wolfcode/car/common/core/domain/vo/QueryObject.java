package com.wolfcode.car.common.core.domain.vo;

import lombok.Data;

@Data
public class QueryObject {
    private Integer pageNum=1;
    private Integer pageSize=5;
}
