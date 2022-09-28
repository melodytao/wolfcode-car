package com.wolfcode.car.audit.mapper;

import com.wolfcode.car.audit.domain.BusCarPackageAudit;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 套餐审核Mapper接口
 * 
 * @author wolfcode
 * @date 2022-09-26
 */
public interface BusCarPackageAuditMapper 
{
    /**
     * 查询套餐审核
     * 
     * @param id 套餐审核主键
     * @return 套餐审核
     */
    public BusCarPackageAudit selectBusCarPackageAuditById(Long id);

    /**
     * 查询套餐审核列表
     * 
     * @param busCarPackageAudit 套餐审核
     * @return 套餐审核集合
     */
    public List<BusCarPackageAudit> selectBusCarPackageAuditList(BusCarPackageAudit busCarPackageAudit);

    /**
     * 新增套餐审核
     * 
     * @param busCarPackageAudit 套餐审核
     * @return 结果
     */
    public int insertBusCarPackageAudit(BusCarPackageAudit busCarPackageAudit);

    /**
     * 修改套餐审核
     * 
     * @param busCarPackageAudit 套餐审核
     * @return 结果
     */
    public int updateBusCarPackageAudit(BusCarPackageAudit busCarPackageAudit);

    /**
     * 删除套餐审核
     * 
     * @param id 套餐审核主键
     * @return 结果
     */
    public int deleteBusCarPackageAuditById(Long id);

    /**
     * 批量删除套餐审核
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusCarPackageAuditByIds(Long[] ids);

    /**
     * 通过业务key查询套餐审核列表
     * @param businessKeyList 业务项的key集合，在这里就是服务单项id集合
     * @return
     */
    public List<BusCarPackageAudit> selectBusCarPackageAuditListByBusinessKeys(
            @Param("businessKeyList") List<String> businessKeyList);

    public List<BusCarPackageAudit> selectDoneAuditListByBusinessKeys(
            @Param("businessKeyList") List<String> businessKeyList);
}
