package com.wolfcode.car.statement.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wolfcode.car.common.annotation.Excel;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 结算单对象 bus_statement
 * 
 * @author wolfcode
 * @date 2022-09-22
 */
@Data
public class BusStatement implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 客户姓名 */
    @Excel(name = "客户姓名")
    private String customerName;

    /** 客户联系方式 */
    @Excel(name = "客户联系方式")
    private String customerPhone;

    /** 实际到店时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Excel(name = "实际到店时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actualArrivalTime;

    /** 车牌号码 */
    @Excel(name = "车牌号码")
    private String licensePlate;

    /** 汽车类型 */
    @Excel(name = "汽车类型")
    private String carSeries;

    /** 服务类型【维修/保养】 */
    @Excel(name = "服务类型")
    private Long serviceType;

    /** 预约单ID【通过这个来判断是否预约用户,唯一标识】 */
    @Excel(name = "预约单ID")
    private Long appointmentId;

    /** 结算状态【消费中0/已支付1】 */
    @Excel(name = "结算状态】")
    private Integer status;

    /** 收款时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "收款时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date payTime;

    /** 收款人id */
    @Excel(name = "收款人id")
    private Long payeeId;

    /** 总消费金额 */
    @Excel(name = "总消费金额")
    private BigDecimal totalAmount;

    /** 服务项数量 */
    @Excel(name = "服务项数量")
    private BigDecimal totalQuantity;

    /** 折扣金额 */
    @Excel(name = "折扣金额")
    private BigDecimal discountAmount;

    /** 备注信息 */
    @Excel(name = "备注信息")
    private String info;

    /** 逻辑删除 */
    @Excel(name = "逻辑删除")
    private Integer isDelete;

    @JsonFormat(pattern = "yyyy-MM-dd")
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
