package com.wolfcode.car.audit.service;

import com.wolfcode.car.audit.domain.BusCarPackageAudit;
import com.wolfcode.car.audit.domain.info.HistoricCommentInfo;
import com.wolfcode.car.audit.domain.vo.BusCarPackageAuditVo;

import java.io.InputStream;
import java.util.List;

/**
 * 套餐审核Service接口
 * 
 * @author wolfcode
 * @date 2022-09-26
 */
public interface IBusCarPackageAuditService 
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
     * 批量删除套餐审核
     * 
     * @param ids 需要删除的套餐审核主键集合
     * @return 结果
     */
    public int deleteBusCarPackageAuditByIds(Long[] ids);

    /**
     * 删除套餐审核信息
     * 
     * @param id 套餐审核主键
     * @return 结果
     */
    public int deleteBusCarPackageAuditById(Long id);

    /**
     * 查询代办套餐审核审核列表
     * @return 办套餐审核审核列表
     */
    public List<BusCarPackageAudit> todoQuery(BusCarPackageAuditVo busCarPackageAuditVo);

    /**
     * 审核
     * @param auditVo
     */
    public void audit(BusCarPackageAuditVo auditVo);

    /**
     * 查询历史批注
     * @param instanceId
     * @return
     */
    public List<HistoricCommentInfo> listHistory(String instanceId);

    /**
     * 获取进度
     * @param id 套餐审核的id
     * @return
     */
    public InputStream getProcessInputStream(Long id);

    /**
     * 已办列表
     * @param auditVo
     * @return
     */
    public List<BusCarPackageAudit> doneQuery(BusCarPackageAuditVo auditVo);

    /**
     * 撤销审核
     * @param id
     */
    void cancelApply(Long id);
}
