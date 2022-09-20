package com.wolfcode.car.appointment.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wolfcode.car.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 养修预约实体
 */
@Data
public class BusAppointment implements Serializable {
    /**  */
    private Long id;

    /** 客户姓名 */
    @Excel(name = "客户姓名")
    private String customerName;

    /** 客户联系方式 */
    @Excel(name = "客户联系方式")
    private String customerPhone;

    /** 预约时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "预约时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date appointmentTime;

    /** 实际到店时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "实际到店时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date actualArrivalTime;

    /** 车牌号码 */
    @Excel(name = "车牌号码")
    private String licensePlate;

    /** 汽车类型 */
    @Excel(name = "汽车类型")
    private String carSeries;

    /** 服务类型【维修0/保养1】 */
    @Excel(name = "服务类型【维修0/保养1】")
    private Integer serviceType;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createTime;

    /** 备注信息 */
    @Excel(name = "备注信息")
    private String info;

    /** 状态【预约中0/已到店1/用户取消2/超时取消3】 */
    @Excel(name = "状态【预约中0/已到店1/用户取消2/超时取消3】")
    private Integer status;

}
