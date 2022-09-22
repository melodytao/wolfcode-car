package com.wolfcode.car.serviceitem.service.impl;

import java.util.List;

import com.wolfcode.car.common.exception.BusinessException;
import com.wolfcode.car.common.utils.AssertUtils;
import com.wolfcode.car.common.utils.DateUtils;
import com.wolfcode.car.serviceitem.constants.AuditStatusEnum;
import com.wolfcode.car.serviceitem.constants.CarPackageEnum;
import com.wolfcode.car.serviceitem.constants.SaleStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wolfcode.car.serviceitem.mapper.BusServiceItemMapper;
import com.wolfcode.car.serviceitem.domain.BusServiceItem;
import com.wolfcode.car.serviceitem.service.IBusServiceItemService;
import org.springframework.util.Assert;

/**
 * 服务项Service业务层处理
 *
 * @author wolfcode
 * @date 2022-09-21
 */
@Service
public class BusServiceItemServiceImpl implements IBusServiceItemService {
    @Autowired
    private BusServiceItemMapper busServiceItemMapper;

    /**
     * 查询服务项
     *
     * @param id 服务项主键
     * @return 服务项
     */
    @Override
    public BusServiceItem selectBusServiceItemById(Long id) {
        return busServiceItemMapper.selectBusServiceItemById(id);
    }

    /**
     * 查询服务项列表
     *
     * @param busServiceItem 服务项
     * @return 服务项
     */
    @Override
    public List<BusServiceItem> selectBusServiceItemList(BusServiceItem busServiceItem) {
        return busServiceItemMapper.selectBusServiceItemList(busServiceItem);
    }

    /**
     * 新增服务项
     *
     * @param busServiceItem 服务项
     * @return 结果
     */
    @Override
    public int insertBusServiceItem(BusServiceItem busServiceItem) {
        busServiceItem.setCreateTime(DateUtils.getNowDate());
        //判断是否套餐
        if (CarPackageEnum.YES.getCode() == busServiceItem.getCarPackage()) {
            //审核套餐，设置状态初始化
            busServiceItem.setAuditStatus(AuditStatusEnum.INIT.getCode());
        } else {
            //非套餐，设置状态无需审核
            busServiceItem.setAuditStatus(AuditStatusEnum.NOREQUIRED.getCode());
        }
        return busServiceItemMapper.insertBusServiceItem(busServiceItem);
    }

    /**
     * 修改服务项
     *
     * @param busServiceItem 服务项
     * @return 结果
     */
    @Override
    public int updateBusServiceItem(BusServiceItem busServiceItem) {
        BusServiceItem serviceItem = this.selectBusServiceItemById(busServiceItem.getId());
        Assert.notNull(serviceItem, "非法参数");
        //处于上架状态，不能进行修改
        AssertUtils.checkNotEqualsStatus(SaleStatusEnum.ON.getCode(), serviceItem.getSaleStatus());
        //处于审核中的状态，不能进行修改
        AssertUtils.checkNotEqualsStatus(AuditStatusEnum.AUDITING.getCode(), serviceItem.getAuditStatus());
        //如果是套餐并审核通过，进行修改，状态改成初始化
        if(AuditStatusEnum.APPROVED.getCode()==serviceItem.getAuditStatus()){
            busServiceItem.setAuditStatus(AuditStatusEnum.INIT.getCode());
        }
        return busServiceItemMapper.updateBusServiceItem(busServiceItem);
    }

    /**
     * 批量删除服务项
     *
     * @param ids 需要删除的服务项主键
     * @return 结果
     */
    @Override
    public int deleteBusServiceItemByIds(Long[] ids) {
        return busServiceItemMapper.deleteBusServiceItemByIds(ids);
    }

    /**
     * 删除服务项信息
     *
     * @param id 服务项主键
     * @return 结果
     */
    @Override
    public int deleteBusServiceItemById(Long id) {
        return busServiceItemMapper.deleteBusServiceItemById(id);
    }

    @Override
    public void saleOn(Long id) {
        BusServiceItem serviceItem = selectBusServiceItemById(id);
        Assert.notNull(serviceItem,"非法参数");
        // 处于上架状态，直接返回
        if(SaleStatusEnum.ON.getCode()==serviceItem.getSaleStatus()) return;
        // 套餐处于非审核，不允许上架操作
        if(CarPackageEnum.YES.getCode()==serviceItem.getCarPackage()
           && AuditStatusEnum.APPROVED.getCode()!=serviceItem.getAuditStatus()){
            throw new BusinessException("未审核通过套餐不允许上架");
        }
        busServiceItemMapper.updateSaleStatus(id,SaleStatusEnum.ON.getCode());
    }

    @Override
    public void saleOff(Long id) {
        busServiceItemMapper.updateSaleStatus(id,SaleStatusEnum.OFF.getCode());
    }
}
