package com.wolfcode.car.appointment.service.impl;

import com.wolfcode.car.appointment.constants.BusAppointmentEnum;
import com.wolfcode.car.appointment.domain.BusAppointment;
import com.wolfcode.car.appointment.domain.BusStatement;
import com.wolfcode.car.appointment.domain.vo.ReservationVo;
import com.wolfcode.car.appointment.mapper.BusAppointmentMapper;
import com.wolfcode.car.appointment.service.IBusAppointmentService;
import com.wolfcode.car.appointment.service.IBusStatementService;
import com.wolfcode.car.common.core.service.SmsService;
import com.wolfcode.car.common.utils.AssertUtils;
import com.wolfcode.car.common.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

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
    @Autowired
    private SmsService smsService;
    @Autowired
    private IBusStatementService busStatementService;

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
        BusAppointment newObj = new BusAppointment();
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
        BusAppointment newObj = new BusAppointment();
        newObj.setId(id);
        newObj.setStatus(status);
        busAppointmentMapper.updateBusAppointment(newObj);
    }

    @Override
    public int reservation(ReservationVo reservationVo) {
        //验证手机的验证码是否正确
        smsService.checkCaptcha(reservationVo.getCustomerPhone(), "appointment", reservationVo.getCode());
        BusAppointment busAppointment = new BusAppointment();
        BeanUtils.copyProperties(reservationVo, busAppointment);
        return insertBusAppointment(busAppointment);
    }

    /**
     * 生成结算单
     *
     * @param appointmentId 预约id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BusStatement generateStatement(Long appointmentId) {
        BusAppointment busAppointment = selectBusAppointmentById(appointmentId);
        Assert.notNull(busAppointment, "非法参数");
        //到店，已结算，支付状态才可以获取或生成结算单
        boolean pass = BusAppointmentEnum.ARRIVAL.getCode() == busAppointment.getStatus() ||
                BusAppointmentEnum.SETTLE.getCode() == busAppointment.getStatus() ||
                BusAppointmentEnum.PAID.getCode() == busAppointment.getStatus();
        Assert.state(pass,"非法操作");
        //通过预约单id查询结算单对象
        BusStatement busStatement=busStatementService.getBusStatementByAppointmentId(appointmentId);
        //预约单id能查询出预约单，直接返回即可，不用生成
        if(busStatement!=null) return busStatement;
        // 生成结算单
        busStatement=new BusStatement();
        busStatement.setCustomerName(busAppointment.getCustomerName());
        busStatement.setCustomerPhone(busAppointment.getCustomerPhone());
        busStatement.setServiceType(busAppointment.getServiceType());
        busStatement.setCarSeries(busAppointment.getCarSeries());
        busStatement.setAppointmentId(appointmentId);
        busStatement.setLicensePlate(busAppointment.getLicensePlate());
        busStatement.setInfo(busAppointment.getInfo());
        busStatement.setActualArrivalTime(busAppointment.getActualArrivalTime());
        busStatement.setCreateTime(DateUtils.getNowDate());
        // 生成预约单
        busStatementService.insertBusStatement(busStatement);
        // 修改预约单状态(结算单生成---已结算)
        changeStatus(appointmentId,BusAppointmentEnum.SETTLE.getCode());
        return busStatement;
    }
}