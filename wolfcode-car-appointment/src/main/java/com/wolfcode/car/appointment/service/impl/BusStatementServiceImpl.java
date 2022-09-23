package com.wolfcode.car.appointment.service.impl;

import com.wolfcode.car.appointment.constants.BusAppointmentEnum;
import com.wolfcode.car.appointment.constants.BusStatementEnum;
import com.wolfcode.car.appointment.domain.BusStatement;
import com.wolfcode.car.appointment.mapper.BusStatementMapper;
import com.wolfcode.car.appointment.service.IBusAppointmentService;
import com.wolfcode.car.appointment.service.IBusStatementService;
import com.wolfcode.car.common.enums.DeleteStatus;
import com.wolfcode.car.common.utils.AssertUtils;
import com.wolfcode.car.common.utils.DateUtils;
import com.wolfcode.car.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;

/**
 * 结算单Service业务层处理
 *
 * @author wolfcode
 * @date 2022-09-22
 */
@Service
public class BusStatementServiceImpl implements IBusStatementService {
    @Autowired
    private BusStatementMapper busStatementMapper;
    @Autowired
    private IBusAppointmentService busAppointmentService;
    /**
     * 查询结算单
     *
     * @param id 结算单主键
     * @return 结算单
     */
    @Override
    public BusStatement selectBusStatementById(Long id) {
        return busStatementMapper.selectBusStatementById(id);
    }

    /**
     * 查询结算单列表
     *
     * @param busStatement 结算单
     * @return 结算单
     */
    @Override
    public List<BusStatement> selectBusStatementList(BusStatement busStatement) {
        return busStatementMapper.selectBusStatementList(busStatement);
    }

    /**
     * 新增结算单
     *
     * @param busStatement 结算单
     * @return 结果
     */
    @Override
    public int insertBusStatement(BusStatement busStatement) {
        busStatement.setCreateTime(DateUtils.getNowDate());
        return busStatementMapper.insertBusStatement(busStatement);
    }

    /**
     * 修改结算单
     *
     * @param busStatement 结算单
     * @return 结果
     */
    @Override
    public int updateBusStatement(BusStatement busStatement) {
        BusStatement statement = selectBusStatementById(busStatement.getId());
        // 校验非空
        Assert.notNull(statement, "非法参数");
        // 已支付状态，不能更新
        AssertUtils.checkNotEqualsStatus(BusStatementEnum.PAID.getCode(),
                statement.getStatus(), "非法操作");
        return busStatementMapper.updateBusStatement(busStatement);
    }

    /**
     * 批量删除结算单
     *
     * @param ids 需要删除的结算单主键
     * @return 结果
     */
    @Override
    public int deleteBusStatementByIds(Long[] ids) {
        return busStatementMapper.deleteBusStatementByIds(ids);
    }

    /**
     * 删除结算单信息
     *
     * @param id 结算单主键
     * @return 结果
     */
    @Override
    public int deleteBusStatementById(Long id) {
        BusStatement statement = selectBusStatementById(id);
        Assert.notNull(statement, "非法参数");
        statement.setIsDelete(DeleteStatus.DELETED.ordinal());// 所有删除都是伪删除
        return updateBusStatement(statement);
    }

    @Override
    public int updateAmount(Long statementId, BigDecimal totalAmount, BigDecimal totalQuantity, BigDecimal discountAmount) {
        return busStatementMapper.updateAmount(statementId, totalAmount, totalQuantity, discountAmount);
    }

    /**
     * 支付
     * 修改结算单状态
     * 有预约单修改预约单的状态
     * @param statementId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pay(Long statementId) {
        // 获取用户id
        Long userId = SecurityUtils.getUserId();
        // 修改结算单状态
        busStatementMapper.pay(statementId, userId, BusStatementEnum.PAID.getCode());
        // 判断当前结算单是否有对应预约单，如果有修改成已支付状态
        BusStatement busStatement = selectBusStatementById(statementId);
        if (busStatement.getAppointmentId() != null) {
            busAppointmentService.changeStatus(busStatement.getAppointmentId(), BusAppointmentEnum.PAID.getCode());
        }
    }

    /**
     * 通过预约单id获取结算单
     * @param appointmentId
     * @return
     */
    @Override
    public BusStatement getBusStatementByAppointmentId(Long appointmentId) {
        return busStatementMapper.selectBusStatementByAppointmentId(appointmentId);
    }
}
