package com.wolfcode.car.appointment.service;

import com.wolfcode.car.appointment.domain.BusServiceItem;
import com.wolfcode.car.appointment.domain.info.ServiceItemAuditInfo;
import com.wolfcode.car.appointment.domain.vo.ServiceItemAuditVo;

import java.util.List;

/**
 * 服务项Service接口
 * 
 * @author wolfcode
 * @date 2022-09-21
 */
public interface IBusServiceItemService 
{
    /**
     * 查询服务项
     * 
     * @param id 服务项主键
     * @return 服务项
     */
    public BusServiceItem selectBusServiceItemById(Long id);

    /**
     * 查询服务项列表
     * 
     * @param busServiceItem 服务项
     * @return 服务项集合
     */
    public List<BusServiceItem> selectBusServiceItemList(BusServiceItem busServiceItem);

    /**
     * 新增服务项
     * 
     * @param busServiceItem 服务项
     * @return 结果
     */
    public int insertBusServiceItem(BusServiceItem busServiceItem);

    /**
     * 修改服务项
     * 
     * @param busServiceItem 服务项
     * @return 结果
     */
    public int updateBusServiceItem(BusServiceItem busServiceItem);

    /**
     * 批量删除服务项
     * 
     * @param ids 需要删除的服务项主键集合
     * @return 结果
     */
    public int deleteBusServiceItemByIds(Long[] ids);

    /**
     * 删除服务项信息
     * 
     * @param id 服务项主键
     * @return 结果
     */
    public int deleteBusServiceItemById(Long id);

    /**
     * 上架功能
     * @param id
     */
    void saleOn(Long id);

    /**
     * 下架功能
     * @param id
     */
    void saleOff(Long id);

    /**
     * 编辑审核信息
     * @param id
     * @return
     */
    ServiceItemAuditInfo auditInfo(Long id);

    /**
     * 开启审核
     * @param serviceItemAuditVo
     */
    void startAudit(ServiceItemAuditVo serviceItemAuditVo);
}
