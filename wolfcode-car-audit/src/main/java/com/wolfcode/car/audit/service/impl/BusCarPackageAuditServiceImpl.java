package com.wolfcode.car.audit.service.impl;

import java.util.List;
import com.wolfcode.car.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wolfcode.car.audit.mapper.BusCarPackageAuditMapper;
import com.wolfcode.car.audit.domain.BusCarPackageAudit;
import com.wolfcode.car.audit.service.IBusCarPackageAuditService;

/**
 * 套餐审核Service业务层处理
 * 
 * @author wolfcode
 * @date 2022-09-26
 */
@Service
public class BusCarPackageAuditServiceImpl implements IBusCarPackageAuditService 
{
    @Autowired
    private BusCarPackageAuditMapper busCarPackageAuditMapper;

    /**
     * 查询套餐审核
     * 
     * @param id 套餐审核主键
     * @return 套餐审核
     */
    @Override
    public BusCarPackageAudit selectBusCarPackageAuditById(Long id)
    {
        return busCarPackageAuditMapper.selectBusCarPackageAuditById(id);
    }

    /**
     * 查询套餐审核列表
     * 
     * @param busCarPackageAudit 套餐审核
     * @return 套餐审核
     */
    @Override
    public List<BusCarPackageAudit> selectBusCarPackageAuditList(BusCarPackageAudit busCarPackageAudit)
    {
        return busCarPackageAuditMapper.selectBusCarPackageAuditList(busCarPackageAudit);
    }

    /**
     * 新增套餐审核
     * 
     * @param busCarPackageAudit 套餐审核
     * @return 结果
     */
    @Override
    public int insertBusCarPackageAudit(BusCarPackageAudit busCarPackageAudit)
    {
        busCarPackageAudit.setCreateTime(DateUtils.getNowDate());
        return busCarPackageAuditMapper.insertBusCarPackageAudit(busCarPackageAudit);
    }

    /**
     * 修改套餐审核
     * 
     * @param busCarPackageAudit 套餐审核
     * @return 结果
     */
    @Override
    public int updateBusCarPackageAudit(BusCarPackageAudit busCarPackageAudit)
    {
        return busCarPackageAuditMapper.updateBusCarPackageAudit(busCarPackageAudit);
    }

    /**
     * 批量删除套餐审核
     * 
     * @param ids 需要删除的套餐审核主键
     * @return 结果
     */
    @Override
    public int deleteBusCarPackageAuditByIds(Long[] ids)
    {
        return busCarPackageAuditMapper.deleteBusCarPackageAuditByIds(ids);
    }

    /**
     * 删除套餐审核信息
     * 
     * @param id 套餐审核主键
     * @return 结果
     */
    @Override
    public int deleteBusCarPackageAuditById(Long id)
    {
        return busCarPackageAuditMapper.deleteBusCarPackageAuditById(id);
    }
}
