package com.wolfcode.car.appointment.service.impl;

import com.wolfcode.car.appointment.constants.AuditStatusEnum;
import com.wolfcode.car.appointment.constants.CarPackageEnum;
import com.wolfcode.car.appointment.constants.SaleStatusEnum;
import com.wolfcode.car.appointment.domain.BusServiceItem;
import com.wolfcode.car.appointment.domain.info.ServiceItemAuditInfo;
import com.wolfcode.car.appointment.domain.vo.ServiceItemAuditVo;
import com.wolfcode.car.appointment.mapper.BusServiceItemMapper;
import com.wolfcode.car.appointment.service.IBusServiceItemService;
import com.wolfcode.car.audit.constants.CarPackageAuditEnum;
import com.wolfcode.car.audit.domain.BusCarPackageAudit;
import com.wolfcode.car.audit.service.IBusCarPackageAuditService;
import com.wolfcode.car.common.core.domain.entity.SysUser;
import com.wolfcode.car.common.exception.BusinessException;
import com.wolfcode.car.common.utils.ActivitiesUtils;
import com.wolfcode.car.common.utils.AssertUtils;
import com.wolfcode.car.common.utils.DateUtils;
import com.wolfcode.car.common.utils.SecurityUtils;
import com.wolfcode.car.flowdefinition.domain.BusBpmnInfo;
import com.wolfcode.car.flowdefinition.service.IBusBpmnInfoService;
import com.wolfcode.car.system.service.ISysUserService;
import lombok.extern.java.Log;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务项Service业务层处理
 *
 * @author wolfcode
 * @date 2022-09-21
 */
@Service
@Log
public class BusServiceItemServiceImpl implements IBusServiceItemService {
    @Autowired
    private BusServiceItemMapper busServiceItemMapper;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private IBusBpmnInfoService busBpmnInfoService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private IBusCarPackageAuditService busCarPackageAuditService;
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

    @Override
    public ServiceItemAuditInfo auditInfo(Long id) {
        //构建Info返回
        //1 获取服务单项
        BusServiceItem busServiceItem = selectBusServiceItemById(id);
        Assert.notNull(busServiceItem,"非法参数");
        // 在套餐,未上架,初始化，审批拒绝状态才可审核
        Integer carPackage = busServiceItem.getCarPackage();
        Integer saleStatus = busServiceItem.getSaleStatus();
        Integer auditStatus = busServiceItem.getAuditStatus();
        Assert.state(CarPackageEnum.YES.getCode()==carPackage,"套餐才可以审核");
        Assert.state(SaleStatusEnum.OFF.getCode()==saleStatus,"未上架状态才可审核");
        Assert.state(AuditStatusEnum.INIT.getCode()==auditStatus ||
                AuditStatusEnum.REJECT.getCode()==auditStatus,"审批为初始化，审批状态才可审核");

        ServiceItemAuditInfo auditInfo=new ServiceItemAuditInfo();
        auditInfo.setServiceItem(busServiceItem);
        //2 查询店主角色用户
        List<SysUser> showOwner = userService.queryByRoleKey("shopOwner");
        auditInfo.setShopOwners(showOwner);
        //3 假如符合业务，价格大于等于3000，查询财务角色信息
        BusBpmnInfo busBpmnInfo = busBpmnInfoService.getByBpmnType(
                BusCarPackageAudit.FLOW_AUDIT_TYPE);
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(busBpmnInfo.getProcessDefinitionKey())
                .processDefinitionVersion(busBpmnInfo.getVersion()).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        BigDecimal discountPrice = ActivitiesUtils.getGTEDiscountPrice(bpmnModel, busBpmnInfo.getProcessDefinitionKey());
        auditInfo.setDiscountPrice(discountPrice);
        log.info("流程定义折扣价格:"+discountPrice);
        if(busServiceItem.getDiscountPrice().compareTo(discountPrice)>0){
            List<SysUser> finances = userService.queryByRoleKey("financial");
            auditInfo.setFinances(finances);
        }
        return auditInfo;
    }

    /**
     * 开启审核
     * @param serviceItemAuditVo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startAudit(ServiceItemAuditVo serviceItemAuditVo) {
        // 校验是否流程定义
        BusBpmnInfo busBpmnInfo = busBpmnInfoService.getByBpmnType(BusCarPackageAudit.FLOW_AUDIT_TYPE);
        Assert.notNull(busBpmnInfo,"请先上传流程定义");
        //1 获取服务单项
        BusServiceItem busServiceItem = selectBusServiceItemById(serviceItemAuditVo.getId());
        Assert.notNull(busServiceItem,"非法参数");
        // 在套餐,未上架,初始化，审批拒绝状态才可审核
        Integer carPackage = busServiceItem.getCarPackage();
        Integer saleStatus = busServiceItem.getSaleStatus();
        Integer auditStatus = busServiceItem.getAuditStatus();
        Assert.state(CarPackageEnum.YES.getCode()==carPackage,"套餐才可以审核");
        Assert.state(SaleStatusEnum.OFF.getCode()==saleStatus,"未上架状态才可审核");
        Assert.state(AuditStatusEnum.INIT.getCode()==auditStatus ||
                AuditStatusEnum.REJECT.getCode()==auditStatus,"审批为初始化，审批状态才可审核");
        // 2 开启套餐审核流程实例
        // 获取流程定义的key
        String processDefinitionKey = busBpmnInfo.getProcessDefinitionKey();
        // 业务标识BusinessKey(服务单项id)
        String businessKey=serviceItemAuditVo.getId().toString();
        // 存储流程定义变量
        Map<String,Object> params=new HashMap<>();
        params.put("shopOwnerId",serviceItemAuditVo.getShopOwnerId());
        if(serviceItemAuditVo.getFinanceId()!=null){
            params.put("financeId",serviceItemAuditVo.getFinanceId());
        }
        params.put("disCountPrice",busServiceItem.getDiscountPrice().longValue());
        // 开启流程实例对象
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, params);
        // 3 保存套餐审核信息
        //封装CarPackageAudit对象
        BusCarPackageAudit audit = new BusCarPackageAudit();
        audit.setServiceItemId(busServiceItem.getId());
        audit.setServiceItemName(busServiceItem.getName());
        audit.setServiceItemPrice(busServiceItem.getDiscountPrice());
        audit.setServiceItemInfo(busServiceItem.getInfo());
        audit.setInfo(serviceItemAuditVo.getInfo());
        // 深圳审核状态
        audit.setStatus(CarPackageAuditEnum.AUDITING.getCode());
        audit.setCreatorId(SecurityUtils.getUserId().toString());
        audit.setCreateTime(DateUtils.getNowDate());
        audit.setInstanceId(instance.getId());

        busCarPackageAuditService.insertBusCarPackageAudit(audit);
        // 4 更新服务单项状态为审核中
        BusServiceItem serviceItem=new BusServiceItem();
        serviceItem.setId(busServiceItem.getId());
        serviceItem.setAuditStatus(AuditStatusEnum.AUDITING.getCode());
        busServiceItemMapper.updateBusServiceItem(serviceItem);
    }
}
