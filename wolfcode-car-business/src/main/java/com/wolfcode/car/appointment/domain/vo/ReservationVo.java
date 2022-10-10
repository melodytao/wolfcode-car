package com.wolfcode.car.appointment.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wolfcode.car.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class ReservationVo {
    /**  */
    private Long id;
    /** 客户姓名 */
    private String customerName;
    /** 客户联系方式 */
    private String customerPhone;
    /** 预约时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date appointmentTime;
    /** 车牌号码 */
    private String licensePlate;
    /** 汽车类型 */
    private String carSeries;
    /** 服务类型【维修0/保养1】 */
    private Long serviceType;
    /** 创建时间 */
    private Date createTime;
    /** 备注信息 */
    @Excel(name = "备注信息")
    private String info;
    /** 状态【预约中0/已到店1/用户取消2/超时取消3】 */
    private Integer status;
    /** 验证码 */
    private String code;
}
