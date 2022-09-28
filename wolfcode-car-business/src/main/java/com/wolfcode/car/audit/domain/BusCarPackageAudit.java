package com.wolfcode.car.audit.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wolfcode.car.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 套餐审核对象 bus_car_package_audit
 * 
 * @author wolfcode
 * @date 2022-09-26
 */
@Data
public class BusCarPackageAudit implements Serializable
{
    private static final long serialVersionUID = 1L;
    public static final Integer FLOW_AUDIT_TYPE = 0;//服务套餐审核类型
    /** 主键 */
    private Long id;

    /** 服务单项id */
    @Excel(name = "服务单项id")
    private Long serviceItemId;

    /** $column.columnComment */
    @Excel(name = "服务单项名称")
    private String serviceItemName;

    /** 服务单项备注 */
    @Excel(name = "服务单项备注")
    private String serviceItemInfo;

    /** 服务单项审核价格 */
    @Excel(name = "服务单项审核价格")
    private BigDecimal serviceItemPrice;

    /** 流程实例id */
    @Excel(name = "流程实例id")
    private String instanceId;

    /** 创建者 */
    @Excel(name = "创建者")
    private String creatorId;

    /** 备注 */
    @Excel(name = "备注")
    private String info;

    /** 状态【进行中0/审核拒绝1/审核通过2/审核撤销3】 */
    @Excel(name = "状态【进行中0/审核拒绝1/审核通过2/审核撤销3】")
    private Integer status;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createTime;
    /** 请求参数 */
    private Map<String, Object> params;
    public Map<String, Object> getParams()
    {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }
}
