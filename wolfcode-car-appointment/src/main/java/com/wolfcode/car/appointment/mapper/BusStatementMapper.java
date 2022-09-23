package com.wolfcode.car.appointment.mapper;


import com.wolfcode.car.appointment.domain.BusStatement;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;


/**
 * 结算单Mapper接口
 * 
 * @author wolfcode
 * @date 2022-09-22
 */
public interface BusStatementMapper 
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
     * 删除结算单
     * 
     * @param id 结算单主键
     * @return 结果
     */
    public int deleteBusStatementById(Long id);

    /**
     * 批量删除结算单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusStatementByIds(Long[] ids);

    /**
     * 更新用户账户
     * @param statementId
     * @param totalAmount
     * @param totalQuantity
     * @param discountAmount
     * @return
     */
    public int updateAmount(@Param("statementId")Long statementId,
                            @Param("totalAmount") BigDecimal totalAmount,
                            @Param("totalQuantity") BigDecimal totalQuantity,
                            @Param("discountAmount") BigDecimal discountAmount);

    public int pay(@Param("statementId") Long statementId,
                   @Param("userId") Long userId,
                   @Param("status") Integer status);

    public BusStatement selectBusStatementByAppointmentId(Long id);
}
