package com.wolfcode.car.appointment.service;
import com.wolfcode.car.appointment.domain.BusStatementItem;
import com.wolfcode.car.appointment.domain.vo.BusStatementItemsVo;
import java.util.List;

/**
 * 结算单项服务
 */
public interface IBusStatementItemService {
    /**
     * 查询结算单项列表
     * @param busStatementItem
     * @return
     */
    List<BusStatementItem> selectBusStatementItemList(BusStatementItem busStatementItem);

    void saveBusStatementItems(BusStatementItemsVo busStatementItemsVo);
}
