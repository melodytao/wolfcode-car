package com.wolfcode.car.audit.service.impl;

import com.wolfcode.car.appointment.constants.AuditStatusEnum;
import com.wolfcode.car.appointment.domain.BusServiceItem;
import com.wolfcode.car.appointment.mapper.BusServiceItemMapper;
import com.wolfcode.car.appointment.service.IBusServiceItemService;
import com.wolfcode.car.audit.constants.CarPackageAuditEnum;
import com.wolfcode.car.audit.domain.BusCarPackageAudit;
import com.wolfcode.car.audit.domain.info.HistoricCommentInfo;
import com.wolfcode.car.audit.domain.vo.BusCarPackageAuditVo;
import com.wolfcode.car.audit.mapper.BusCarPackageAuditMapper;
import com.wolfcode.car.audit.service.IBusCarPackageAuditService;
import com.wolfcode.car.common.core.domain.model.LoginUser;
import com.wolfcode.car.common.utils.DateUtils;
import com.wolfcode.car.common.utils.PageUtils;
import com.wolfcode.car.common.utils.SecurityUtils;
import com.wolfcode.car.flowdefinition.domain.BusBpmnInfo;
import com.wolfcode.car.flowdefinition.service.IBusBpmnInfoService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 套餐审核Service业务层处理
 *
 * @author wolfcode
 * @date 2022-09-26
 */
@Service
public class BusCarPackageAuditServiceImpl implements IBusCarPackageAuditService {
    @Autowired
    private BusCarPackageAuditMapper busCarPackageAuditMapper;

    @Autowired
    private IBusBpmnInfoService busBpmnInfoService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private BusServiceItemMapper serviceItemMapper;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 查询套餐审核
     *
     * @param id 套餐审核主键
     * @return 套餐审核
     */
    @Override
    public BusCarPackageAudit selectBusCarPackageAuditById(Long id) {
        return busCarPackageAuditMapper.selectBusCarPackageAuditById(id);
    }

    /**
     * 查询套餐审核列表
     *
     * @param busCarPackageAudit 套餐审核
     * @return 套餐审核
     */
    @Override
    public List<BusCarPackageAudit> selectBusCarPackageAuditList(BusCarPackageAudit busCarPackageAudit) {
        return busCarPackageAuditMapper.selectBusCarPackageAuditList(busCarPackageAudit);
    }

    /**
     * 新增套餐审核
     *
     * @param busCarPackageAudit 套餐审核
     * @return 结果
     */
    @Override
    public int insertBusCarPackageAudit(BusCarPackageAudit busCarPackageAudit) {
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
    public int updateBusCarPackageAudit(BusCarPackageAudit busCarPackageAudit) {
        return busCarPackageAuditMapper.updateBusCarPackageAudit(busCarPackageAudit);
    }

    /**
     * 批量删除套餐审核
     *
     * @param ids 需要删除的套餐审核主键
     * @return 结果
     */
    @Override
    public int deleteBusCarPackageAuditByIds(Long[] ids) {
        return busCarPackageAuditMapper.deleteBusCarPackageAuditByIds(ids);
    }

    /**
     * 删除套餐审核信息
     *
     * @param id 套餐审核主键
     * @return 结果
     */
    @Override
    public int deleteBusCarPackageAuditById(Long id) {
        return busCarPackageAuditMapper.deleteBusCarPackageAuditById(id);
    }

    /**
     * 查询代办套餐审核审核列表
     *
     * @return 办套餐审核审核列表
     */
    @Override
    public List<BusCarPackageAudit> todoQuery(BusCarPackageAuditVo auditVo) {
        //目前根据业务要求，支持套餐审核，后续再考虑扩展
        // 1 获取套餐审核流程定义信息
        BusBpmnInfo busBpmnInfo = busBpmnInfoService.getByBpmnType(BusCarPackageAudit.FLOW_AUDIT_TYPE);
        Assert.notNull(busBpmnInfo, "请先上传流程定义");
        // 当前登录用户id
        //2 查询任务列表
        String userId = SecurityUtils.getUserId().toString();
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey(busBpmnInfo.getProcessDefinitionKey())
                .taskAssignee(userId).list();
        if (CollectionUtils.isEmpty(taskList)) {
            return Collections.emptyList();
        }

        //3 获取businessKeys
        //ru_task(proc_inst_id)  ru_execution(proc_inst_id ---> business_key)
        //流程实例id集合
        Set<String> processInstanceIds = taskList.stream().map((task) ->
                task.getProcessInstanceId()).collect(Collectors.toSet());
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().
                processInstanceIds(processInstanceIds).list();
        List<String> businessKeys = processInstances.stream().map((processInstance) ->
                processInstance.getBusinessKey()).collect(Collectors.toList());

        //4 通过businessKeys，查询套餐审核列表(审核中)
        PageUtils.startPage();
        List<BusCarPackageAudit> busCarPackageAudits = busCarPackageAuditMapper.
                selectBusCarPackageAuditListByBusinessKeys(businessKeys);
        return busCarPackageAudits;
    }

    /**
     * 审核
     *
     * @param auditVo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void audit(BusCarPackageAuditVo auditVo) {
        // 1 获取套餐审核对象
        BusCarPackageAudit busCarPackageAudit = this.selectBusCarPackageAuditById(auditVo.getId());
        Assert.notNull(busCarPackageAudit, "非法参数");
        // 2 审核中的才允许审核
        Assert.state(CarPackageAuditEnum.AUDITING.getCode() == busCarPackageAudit.getStatus(),
                "审核中的才允许审核");
        // 3 完成任务(需要参考bpmn图)
        //  获取当前用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //  获取流程实例目前的任务(根据流程定义id获取任务)
        Task task = taskService.createTaskQuery().
                processInstanceId(busCarPackageAudit.getInstanceId()).singleResult();
        Assert.notNull(task, "非法操作");
        //  添加批注
        String auditLabel = auditVo.isAuditStatus() ? "同意" : "拒绝";
        String comment = loginUser.getUsername() + "[" + auditLabel + "]," + auditVo.getInfo();
        taskService.addComment(task.getId(), busCarPackageAudit.getInstanceId(), comment);
        //  设置流程变量（shopOwner,ru_task(task_def_key)）
        //  shopOwner=false---拒绝根据bpmn图，直接结束事件
        taskService.setVariable(task.getId(), task.getTaskDefinitionKey(), auditVo.isAuditStatus());
        //  完成任务
        taskService.complete(task.getId());
        // 4 根据审核状态（同意还是拒绝）更新套餐审核和服务单项的状态
        BusCarPackageAudit audit = new BusCarPackageAudit();
        BusServiceItem serviceItem = new BusServiceItem();
        if (auditVo.isAuditStatus()) {
            // 同意
            // 判断还有没有下一个任务，没有代表任务结束
            Task nextTask = taskService.createTaskQuery().processInstanceId(busCarPackageAudit.getInstanceId())
                    .singleResult();
            if (nextTask == null) {
                audit.setId(busCarPackageAudit.getId());
                //审核套餐审核通过
                audit.setStatus(CarPackageAuditEnum.APPROVED.getCode());
                busCarPackageAuditMapper.updateBusCarPackageAudit(audit);
                //服务单项审核审核通过
                serviceItem.setId(busCarPackageAudit.getServiceItemId());
                serviceItem.setAuditStatus(AuditStatusEnum.APPROVED.getCode());
                serviceItemMapper.updateBusServiceItem(serviceItem);
            }
        } else {
            // 拒绝
            audit.setId(busCarPackageAudit.getId());
            //审核套餐审核拒绝
            audit.setStatus(CarPackageAuditEnum.REJECT.getCode());
            busCarPackageAuditMapper.updateBusCarPackageAudit(audit);
            //服务单项审核审核拒绝
            serviceItem.setId(busCarPackageAudit.getServiceItemId());
            serviceItem.setAuditStatus(AuditStatusEnum.REJECT.getCode());
            serviceItemMapper.updateBusServiceItem(serviceItem);
        }

    }

    /**
     * 查询历史批注
     *
     * @param instanceId
     * @return
     */
    @Override
    public List<HistoricCommentInfo> listHistory(String instanceId) {
        //1 根据实例id查询历史任务列表(按结束时间排序)---hi_taskinst
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(instanceId).orderByHistoricTaskInstanceEndTime().asc().list();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 2 处理历史任务列表
        List<HistoricCommentInfo> historicCommentInfos = historicTaskInstances.stream().map((taskInstance -> {
            HistoricCommentInfo historicCommentInfo = new HistoricCommentInfo();
            // 任务名称
            historicCommentInfo.setTaskName(taskInstance.getName());
            // 开始时间
            historicCommentInfo.setStartTime(sdf.format(taskInstance.getStartTime()));
            // 结束时间
            if (taskInstance.getEndTime() != null) {
                historicCommentInfo.setEndTime(sdf.format(taskInstance.getEndTime()));
            }
            // 持续时间
            if (taskInstance.getDurationInMillis() != null) {
                String durationMills = DateUtils.getDatePoor(taskInstance.getEndTime(), taskInstance.getStartTime());
                historicCommentInfo.setDurationInMillis(durationMills);
            }
            // 评论
            // 评论设计有问题，应该设计到历史表中，不是再运行的时候，官方也意识到这一点
            List<Comment> comments = taskService.getTaskComments(taskInstance.getId(), "comment");
            if (!CollectionUtils.isEmpty(comments)) {
                historicCommentInfo.setComment(comments.get(0).getFullMessage());
            }
            return historicCommentInfo;
        })).collect(Collectors.toList());
        return historicCommentInfos;
    }

    /**
     * 获取进度
     *
     * @param id 套餐审核的id
     * @return
     */
    @Override
    public InputStream getProcessInputStream(Long id) {
        // 1 获取套餐审核的信息
        BusCarPackageAudit audit = this.selectBusCarPackageAuditById(id);
        Assert.notNull(audit, "非法参数");
        // 2 审核中需要高亮显示，获取流程定义key
        List<String> highLightedActivities = Collections.emptyList();
        String processDefinitionKey=null;
        // 审核中，需要要高亮,获取processDefinitionKey
        if(CarPackageAuditEnum.AUDITING.getCode()==audit.getStatus()){
            ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(audit.getInstanceId()).singleResult();
            Assert.notNull(instance,"查找不到相应的实例记录");
            processDefinitionKey=instance.getProcessDefinitionKey();
            highLightedActivities = runtimeService.getActiveActivityIds(audit.getInstanceId());
        }else{
            //流程已结束的
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().
                    processInstanceId(audit.getInstanceId()).singleResult();
            Assert.notNull(historicProcessInstance,"查找不到相应的流程记录");
            processDefinitionKey= historicProcessInstance.getProcessDefinitionKey();

        }
        // 3 生成资源流()
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey)
                .orderByProcessDefinitionVersion().desc().list();
        Assert.notEmpty(processDefinitions,"非法操作");
        //获取流程定义文件，生成图片流
        ProcessDefinition processDefinition=processDefinitions.get(0);
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        DefaultProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
        //generateDiagram(bpmn模型,需要高亮节点ID集合,需要高亮连线ID集合)
        return generator.generateDiagram(bpmnModel,
                highLightedActivities,
                Collections.emptyList(),
                "宋体",
                "宋体",
                "宋体"
        );
    }

    /**
     * 已办列表
     * @param auditVo
     * @return
     */
    @Override
    public List<BusCarPackageAudit> doneQuery(BusCarPackageAuditVo auditVo) {
        //目前根据业务要求，支持套餐审核，后续再考虑扩展
        // 1 获取套餐审核流程定义信息
        BusBpmnInfo busBpmnInfo = busBpmnInfoService.getByBpmnType(BusCarPackageAudit.FLOW_AUDIT_TYPE);
        Assert.notNull(busBpmnInfo, "请先上传流程定义");
        // 当前登录用户id
        //2 查询历史列表
        String userId = SecurityUtils.getUserId().toString();
        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery().
                processDefinitionKey(busBpmnInfo.getProcessDefinitionKey()).
                finished().taskAssignee(userId).list();
        if (CollectionUtils.isEmpty(taskList)) {
            return Collections.emptyList();
        }
        //流程实例id集合
        //3 获取businessKeys
        Set<String> processInstanceIds = taskList.stream().map((task) ->
                task.getProcessInstanceId()).collect(Collectors.toSet());
        List<HistoricProcessInstance> processInstances = historyService.createHistoricProcessInstanceQuery().processInstanceIds(processInstanceIds).list();
        List<String> businessKeys = processInstances.stream().map((processInstance) ->
                processInstance.getBusinessKey()).collect(Collectors.toList());
        //4 通过businessKeys，查询套餐审核列表(不是审核中)
        //开启分页
        PageUtils.startPage();
        return busCarPackageAuditMapper.selectDoneAuditListByBusinessKeys(businessKeys);
    }

    /**
     * 撤销审核
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelApply(Long id) {
        // 1 获取套餐审核对象
        BusCarPackageAudit busCarPackageAudit = this.selectBusCarPackageAuditById(id);
        Assert.notNull(busCarPackageAudit, "非法参数");
        // 2 审核中的才允许撤销
        Assert.state(CarPackageAuditEnum.AUDITING.getCode() == busCarPackageAudit.getStatus(),
                "审核中的才允许撤销");

        // 3 删除流程实例
        ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(busCarPackageAudit.getInstanceId()).singleResult();
        Assert.notNull(instance,"找不到流程实例");
        runtimeService.deleteProcessInstance(busCarPackageAudit.getInstanceId(),"流程撤销");
        // 4 更改状态
          // 审核套餐更改为撤销状态
        BusCarPackageAudit audit = new BusCarPackageAudit();
        audit.setId(busCarPackageAudit.getId());
        audit.setStatus(CarPackageAuditEnum.CANCEL.getCode());
        busCarPackageAuditMapper.updateBusCarPackageAudit(audit);
         //  服务单项更改为初始化
        BusServiceItem serviceItem = new BusServiceItem();
        serviceItem.setId(busCarPackageAudit.getServiceItemId());
        serviceItem.setAuditStatus(AuditStatusEnum.INIT.getCode());
        serviceItemMapper.updateBusServiceItem(serviceItem);
    }


}
