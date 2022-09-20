package com.wolfcode.car.appointment.service.impl;

import java.util.List;

import com.wolfcode.car.appointment.constants.BusAppointmentEnum;
import com.wolfcode.car.common.exception.BusinessException;
import com.wolfcode.car.common.utils.AssertUtils;
import com.wolfcode.car.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wolfcode.car.appointment.mapper.BusAppointmentMapper;
import com.wolfcode.car.appointment.domain.BusAppointment;
import com.wolfcode.car.appointment.service.IBusAppointmentService;
import org.springframework.util.Assert;

/**
 * 养修信息预约Service业务层处理
 *
 * @author wolfcode
 * @date 2022-09-19
 */
@Service
public class BusAppointmentServiceImpl implements IBusAppointmentService {
    @Autowired
    private BusAppointmentMapper busAppointmentMapper;

    /**
     * 查询养修信息预约
     *
     * @param id 养修信息预约主键
     * @return 养修信息预约
     */
    @Override
    public BusAppointment selectBusAppointmentById(Long id) {
        return busAppointmentMapper.selectBusAppointmentById(id);
    }

    /**
     * 查询养修信息预约列表
     *
     * @param busAppointment 养修信息预约
     * @return 养修信息预约
     */
    @Override
    public List<BusAppointment> selectBusAppointmentList(BusAppointment busAppointment) {
        return busAppointmentMapper.selectBusAppointmentList(busAppointment);
    }

    /**
     * 新增养修信息预约
     *
     * @param busAppointment 养修信息预约
     * @return 结果
     */
    @Override
    public int insertBusAppointment(BusAppointment busAppointment) {
        // 校验预约日期
        AssertUtils.checkAppointmentDate(busAppointment.getAppointmentTime(),
                DateUtils.getNowDate());
        busAppointment.setCreateTime(DateUtils.getNowDate());
        return busAppointmentMapper.insertBusAppointment(busAppointment);
    }

    /**
     * 修改养修信息预约
     *
     * @param busAppointment 养修信息预约
     * @return 结果
     */
    @Override
    public int updateBusAppointment(BusAppointment busAppointment) {
        // 业务规则：非预约中状态，不能更新字段
        BusAppointment newObj = selectBusAppointmentById(busAppointment.getId());
        AssertUtils.checkStatus(newObj.getStatus(),
                BusAppointmentEnum.APPOINTMENT.getCode(),
                "非预约中的记录不允许编辑");
        // 校验预约日期
        AssertUtils.checkAppointmentDate(busAppointment.getAppointmentTime(),
                DateUtils.getNowDate());
        return busAppointmentMapper.updateBusAppointment(busAppointment);
    }

    /**
     * 批量删除养修信息预约
     *
     * @param ids 需要删除的养修信息预约主键
     * @return 结果
     */
    @Override
    public int deleteBusAppointmentByIds(Long[] ids) {
        return busAppointmentMapper.deleteBusAppointmentByIds(ids);
    }

    /**
     * 删除养修信息预约信息
     *
     * @param id 养修信息预约主键
     * @return 结果
     */
    @Override
    public int deleteBusAppointmentById(Long id) {
        return busAppointmentMapper.deleteBusAppointmentById(id);
    }

    /**
     * 客户到店
     *
     * @param id
     */
    @Override
    public void arrival(Long id) {
        BusAppointment busAppointment = selectBusAppointmentById(id);
        AssertUtils.checkStatus(busAppointment.getStatus(),
                BusAppointmentEnum.APPOINTMENT.getCode());
        BusAppointment newObj = new BusAppointment();
        newObj.setId(id);
        newObj.setStatus(BusAppointmentEnum.ARRIVAL.getCode());
        //更新实际到店时间
        newObj.setActualArrivalTime(DateUtils.getNowDate());
        busAppointmentMapper.updateBusAppointment(newObj);
    }

    /**
     * 取消预约
     *
     * @param id
     */
    @Override
    public void cancel(Long id) {
        BusAppointment busAppointment = selectBusAppointmentById(id);
        AssertUtils.checkStatus(busAppointment.getStatus(),
                BusAppointmentEnum.APPOINTMENT.getCode());
        //更新状态
        BusAppointment newObj=new BusAppointment();
        newObj.setId(id);
        newObj.setStatus(BusAppointmentEnum.CANCEL.getCode());
        busAppointmentMapper.updateBusAppointment(newObj);
    }

    /**
     * 更改预约状态
     *
     * @param id
     * @param status
     */
    @Override
    public void changeStatus(Long id, Integer status) {
        BusAppointment newObj=new BusAppointment();
        newObj.setId(id);
        newObj.setStatus(status);
        busAppointmentMapper.updateBusAppointment(newObj);
    }
}