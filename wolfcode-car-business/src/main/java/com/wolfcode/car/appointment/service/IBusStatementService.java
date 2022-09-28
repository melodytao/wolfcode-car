package com.wolfcode.car.appointment.service;



import com.wolfcode.car.appointment.domain.BusStatement;

import java.math.BigDecimal;
import java.util.List;

/**
 * 结算单Service接口
 * 
 * @author wolfcode
 * @date 2022-09-22
 */
public interface IBusStatementService 
{
    /**
     * 查询结算单
     * 
     * @param id 结算单主键
     * @return 结算单
     */
    public BusStatement selectBusStatementById(Long id);

    /**
     * 查询结算单列表
     * 
     * @param busStatement 结算单
     * @return 结算单集合
     */
    public List<BusStatement> selectBusStatementList(BusStatement busStatement);

    /**
     * 新增结算单
     * 
     * @param busStatement 结算单
     * @return 结果
     */
    public int insertBusStatement(BusStatement busStatement);

    /**
     * 修改结算单
     * 
     * @param busStatement 结算单
     * @return 结果
     */
    public int updateBusStatement(BusStatement busStatement);

    /**
     * 批量删除结算单
     * 
     * @param ids 需要删除的结算单主键集合
     * @return 结果
     */
    public int deleteBusStatementByIds(Long[] ids);

    /**
     * 删除结算单信息
     * 
     * @param id 结算单主键
     * @return 结果
     */
    public int deleteBusStatementById(Long id);

    /**
     * 更新结算单余额
     * @param statementId
     * @param totalAmount
     * @param totalQuantity
     * @param discountAmount
     * @return
     */
    public int updateAmount(Long statementId, BigDecimal totalAmount,
                            BigDecimal totalQuantity, BigDecimal discountAmount);

    /**
     * 支付，更改结算单状态
     * @param statementId
     * @return
     */
    public void pay(Long statementId);

    /**
     * 通过预约单id获取结算单
     * @param appointmentId
     * @return
     */
    BusStatement getBusStatementByAppointmentId(Long appointmentId);
}
