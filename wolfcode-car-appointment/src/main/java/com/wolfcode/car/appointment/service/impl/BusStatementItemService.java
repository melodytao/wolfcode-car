package com.wolfcode.car.appointment.service.impl;

import com.wolfcode.car.appointment.constants.BusStatementEnum;
import com.wolfcode.car.appointment.domain.BusStatement;
import com.wolfcode.car.appointment.domain.BusStatementItem;
import com.wolfcode.car.appointment.domain.vo.BusStatementItemsVo;
import com.wolfcode.car.appointment.mapper.BusStatementItemMapper;
import com.wolfcode.car.appointment.service.IBusStatementItemService;
import com.wolfcode.car.appointment.service.IBusStatementService;
import com.wolfcode.car.common.utils.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;


@Service
public class BusStatementItemService implements IBusStatementItemService {
    @Autowired
    private BusStatementItemMapper busStatementItemMapper;
    @Autowired
    private IBusStatementService busStatementService;

    @Override
    public List<BusStatementItem> selectBusStatementItemList(BusStatementItem busStatementItem) {
        return busStatementItemMapper.selectBusStatementItemList(busStatementItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBusStatementItems(BusStatementItemsVo vo) {
        List<BusStatementItem> busStatementItems = vo.getBusStatementItems();
        BigDecimal discountPrice = vo.getDiscountPrice();
        //非空校验
        Assert.notEmpty(busStatementItems, "非法参数");
        //判断集合中是否空元素
        Assert.noNullElements(busStatementItems, "不允许某个结算单项为空");
        //检测集合中是否来自同一个结算单
        checkUnqinueStatementId(busStatementItems, "检测集合中结算单项不是来自同一个结算单");
        // 获取结算单
        Long statementId = busStatementItems.get(0).getStatementId();
        BusStatement busStatement = busStatementService.selectBusStatementById(statementId);
        Assert.notNull(busStatement, "结算单不存在");
        // 判断结算单状态
        // 已支付的结算不能进行保存操作
        AssertUtils.checkNotEqualsStatus(BusStatementEnum.PAID.getCode(),
                busStatement.getStatus(), "已支付的结算不能进行保存操作");
        // 删除之前得明细得数据
        busStatementItemMapper.deleteByStatementId(statementId);

        // 处理总金额
        BigDecimal totalAmount=new BigDecimal(0);
        BigDecimal totalQuantity=new BigDecimal(0);
        for (BusStatementItem busStatementItem : busStatementItems) {
            busStatementItemMapper.insertBusStatementItem(busStatementItem);
            totalAmount=totalAmount.add(busStatementItem.getItemPrice().
                    multiply(busStatementItem.getItemQuantity()));
            totalQuantity=totalQuantity.add(busStatementItem.getItemQuantity());
        }
        Assert.state(totalQuantity.intValue()>0,"购买数量不能零");
        Assert.state(discountPrice.compareTo(totalAmount)<=0,"非法操作");
        //更新结算单的总消费金额,总数量,总折扣金额
        busStatementService.updateAmount(statementId,totalAmount,totalQuantity,discountPrice);
    }

    /**
     * 检测集合中是否来自同一个结算单
     *
     * @param busStatementItems
     */
    private void checkUnqinueStatementId(List<BusStatementItem> busStatementItems, String msg) {
        HashSet<Long> set = new HashSet<>();
        busStatementItems.stream().forEach((item) -> {
            set.add(item.getStatementId());
        });
        Assert.state(set.size() == 1, msg);
    }
}
