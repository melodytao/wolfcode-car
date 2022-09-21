package com.wolfcode.car.appointment.service;

import java.util.List;
import com.wolfcode.car.appointment.domain.BusAppointment;
import com.wolfcode.car.appointment.domain.vo.ReservationVo;

/**
 * 养修信息预约Service接口
 * 
 * @author wolfcode
 * @date 2022-09-19
 */
public interface IBusAppointmentService 
{
    /**
     * 查询养修信息预约
     * 
     * @param id 养修信息预约主键
     * @return 养修信息预约
     */
    public BusAppointment selectBusAppointmentById(Long id);

    /**
     * 查询养修信息预约列表
     * 
     * @param busAppointment 养修信息预约
     * @return 养修信息预约集合
     */
    public List<BusAppointment> selectBusAppointmentList(BusAppointment busAppointment);

    /**
     * 新增养修信息预约
     * 
     * @param busAppointment 养修信息预约
     * @return 结果
     */
    public int insertBusAppointment(BusAppointment busAppointment);

    /**
     * 修改养修信息预约
     * 
     * @param busAppointment 养修信息预约
     * @return 结果
     */
    public int updateBusAppointment(BusAppointment busAppointment);

    /**
     * 批量删除养修信息预约
     * 
     * @param ids 需要删除的养修信息预约主键集合
     * @return 结果
     */
    public int deleteBusAppointmentByIds(Long[] ids);

    /**
     * 删除养修信息预约信息
     * 
     * @param id 养修信息预约主键
     * @return 结果
     */
    public int deleteBusAppointmentById(Long id);


    /**
     * 客户到店
     */
    void arrival(Long id);

    /**
     * 取消预约
     * @param id
     */
    void cancel(Long id);

    /**
     * 更改预约状态
     * @param appointmentId
     * @param status
     */
    void changeStatus(Long appointmentId,Integer status);

    /**
     * 预约服务
     * @param reservationVo
     * @return
     */
    int reservation(ReservationVo reservationVo);
    //TODO 生成结算单

}