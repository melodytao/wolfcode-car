package com.wolfcode.car.serviceitem.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wolfcode.car.common.annotation.Excel;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 服务项对象 bus_service_item
 * 
 * @author wolfcode
 * @date 2022-09-21
 */
@Data
public class BusServiceItem implements Serializable
{
    private static final long serialVersionUID = 1L;
    /** 主键id */
    private Long id;
    /** 服务项名称 */
    @Excel(name = "服务项名称")
    private String name;

    /** 服务项原价 */
    @Excel(name = "服务项原价")
    private BigDecimal originalPrice;

    /** 服务项折扣价 */
    @Excel(name = "服务项折扣价")
    private BigDecimal discountPrice;

    /** 是否套餐【是/否】 */
    @Excel(name = "是否套餐【是/否】")
    private Integer carPackage;

    /** 备注信息 */
    @Excel(name = "备注信息")
    private String info;

    /** 服务分类【维修/保养/其他】 */
    @Excel(name = "服务分类【维修/保养/其他】")
    private Integer serviceCatalog;

    /** 审核状态【初始化/审核中/审核通过/审核拒绝/无需审核】 */
    @Excel(name = "审核状态【初始化/审核中/审核通过/审核拒绝/无需审核】")
    private Integer auditStatus;

    /** 上架状态【已上架/未上架】 */
    @Excel(name = "上架状态【已上架/未上架】")
    private Integer saleStatus;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createTime;

}
