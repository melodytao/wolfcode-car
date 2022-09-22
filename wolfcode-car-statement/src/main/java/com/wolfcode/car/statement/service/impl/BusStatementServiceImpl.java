package com.wolfcode.car.statement.service.impl;

import java.util.List;

import com.wolfcode.car.common.enums.DeleteStatus;
import com.wolfcode.car.common.utils.AssertUtils;
import com.wolfcode.car.common.utils.DateUtils;
import com.wolfcode.car.statement.constants.BusStatementEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wolfcode.car.statement.mapper.BusStatementMapper;
import com.wolfcode.car.statement.domain.BusStatement;
import com.wolfcode.car.statement.service.IBusStatementService;
import org.springframework.util.Assert;

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
                statement.getStatus(),"非法操作");
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
        Assert.notNull(statement,"非法参数");
        statement.setIsDelete(DeleteStatus.DELETED.ordinal());// 所有删除都是伪删除
        return updateBusStatement(statement);
    }
}
