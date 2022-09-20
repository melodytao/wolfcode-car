package com.wolfcode.car.appointment.mapper;

import java.util.List;
import com.wolfcode.car.appointment.domain.BusAppointment;

/**
 * 养修信息预约Mapper接口
 * 
 * @author wolfcode
 * @date 2022-09-19
 */
public interface BusAppointmentMapper 
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
     * 删除养修信息预约
     * 
     * @param id 养修信息预约主键
     * @return 结果
     */
    public int deleteBusAppointmentById(Long id);

    /**
     * 批量删除养修信息预约
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusAppointmentByIds(Long[] ids);
}