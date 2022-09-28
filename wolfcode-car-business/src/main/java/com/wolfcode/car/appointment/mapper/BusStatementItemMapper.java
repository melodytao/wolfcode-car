package com.wolfcode.car.appointment.mapper;

import com.wolfcode.car.appointment.domain.BusStatementItem;

import java.util.List;

public interface BusStatementItemMapper {
    /**
     * 查询结算单项
     * @param id 结算单项id
     * @return 结算单项
     */
    BusStatementItem selectBusStatementItemById(Long id);

    /**
     * 结算单项列表
     * @param busStatementItem 结算单项
     * @return 结算单项集合
     */
    List<BusStatementItem> selectBusStatementItemList(BusStatementItem busStatementItem);

    /**
     * 新增结算单项
     * @param busStatementItem 结算单项
     * @return 结果
     */
    int insertBusStatementItem(BusStatementItem busStatementItem);

    /**
     * 更改服务单项
     * @param busStatementItem 服务单项
     * @return 结果
     */
    int updateBustatementItem(BusStatementItem busStatementItem);

    /**
     * 根据id删除结算单项
     * @param id 主键id
     * @return 结果
     */
    int deleteStatementItemById(Long id);

    /**
     * 批量删除
     * @param ids 主键数组
     * @return 结果
     */
    int deleteStatementItemByIds(Long[] ids);

    /**
     * 通过结算单id删除结算单项
     * @param id 结算单id
     * @return 结果
     */
    int deleteByStatementId(Long id);
}
