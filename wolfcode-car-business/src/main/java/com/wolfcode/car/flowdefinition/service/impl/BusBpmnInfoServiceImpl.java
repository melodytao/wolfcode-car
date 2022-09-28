package com.wolfcode.car.flowdefinition.service.impl;

import com.wolfcode.car.common.utils.DateUtils;
import com.wolfcode.car.flowdefinition.domain.BusBpmnInfo;
import com.wolfcode.car.flowdefinition.mapper.BusBpmnInfoMapper;
import com.wolfcode.car.flowdefinition.service.IBusBpmnInfoService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * 流程定义明细Service业务层处理
 * 
 * @author wolfcode
 * @date 2022-09-26
 */
@Service
public class BusBpmnInfoServiceImpl implements IBusBpmnInfoService 
{
    @Autowired
    private BusBpmnInfoMapper busBpmnInfoMapper;

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 查询流程定义明细
     * 
     * @param id 流程定义明细主键
     * @return 流程定义明细
     */
    @Override
    public BusBpmnInfo selectBusBpmnInfoById(Long id)
    {
        return busBpmnInfoMapper.selectBusBpmnInfoById(id);
    }

    /**
     * 查询流程定义明细列表
     * 
     * @param busBpmnInfo 流程定义明细
     * @return 流程定义明细
     */
    @Override
    public List<BusBpmnInfo> selectBusBpmnInfoList(BusBpmnInfo busBpmnInfo)
    {
        return busBpmnInfoMapper.selectBusBpmnInfoList(busBpmnInfo);
    }

    /**
     * 新增流程定义明细
     * 
     * @param busBpmnInfo 流程定义明细
     * @return 结果
     */
    @Override
    public int insertBusBpmnInfo(BusBpmnInfo busBpmnInfo)
    {
        return busBpmnInfoMapper.insertBusBpmnInfo(busBpmnInfo);
    }

    /**
     * 修改流程定义明细
     * 
     * @param busBpmnInfo 流程定义明细
     * @return 结果
     */
    @Override
    public int updateBusBpmnInfo(BusBpmnInfo busBpmnInfo)
    {
        return busBpmnInfoMapper.updateBusBpmnInfo(busBpmnInfo);
    }

    /**
     * 批量删除流程定义明细
     * 
     * @param ids 需要删除的流程定义明细主键
     * @return 结果
     */
    @Override
    public int deleteBusBpmnInfoByIds(Long[] ids)
    {
        return busBpmnInfoMapper.deleteBusBpmnInfoByIds(ids);
    }

    /**
     * 删除流程定义明细信息
     * @param id 流程定义明细主键
     * @return 结果
     */
    @Override
    public int deleteBusBpmnInfoById(Long id)
    {
        BusBpmnInfo busBpmnInfo = selectBusBpmnInfoById(id);
        Assert.notNull(busBpmnInfo,"非法参数");
        //流程定义key
        String processDefinitionKey = busBpmnInfo.getProcessDefinitionKey();
        //版本号
        Integer version = busBpmnInfo.getVersion();
        // 删除bpmnInfo明细
        int result = busBpmnInfoMapper.deleteBusBpmnInfoById(id);
        // 删除流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .processDefinitionVersion(version).singleResult();
        repositoryService.deleteDeployment(processDefinition.getDeploymentId());
        return result;
    }
    /**
     * 返回不同输入流
     * @param id bpmn_info 的主键id
     * @param type xml还是png...
     * @return
     */
    @Override
    public InputStream getFileInputStream(Long id, String type) {
        InputStream inputStream=null;
        //获取流程定义
        BusBpmnInfo busBpmnInfo = selectBusBpmnInfoById(id);
        Assert.notNull(busBpmnInfo,"非法参数");
        //通过流程定义key和版本查询流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(busBpmnInfo.getProcessDefinitionKey())
                .processDefinitionVersion(busBpmnInfo.getVersion())
                .singleResult();
        if("xml".equalsIgnoreCase(type)){
            String resourceName=processDefinition.getResourceName();
            inputStream=repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),resourceName);
        }else{
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
            //生成png图片
            DefaultProcessDiagramGenerator generator=new DefaultProcessDiagramGenerator();
            //假如服务器没有相应字体会出现，中文相应的乱码
            //generateDiagram(bpmn模型,需要高亮节点ID集合,需要高亮连线ID集合)
            inputStream=generator.generateDiagram(bpmnModel,
                    Collections.EMPTY_LIST,
                    Collections.EMPTY_LIST,
                    "宋体",
                    "宋体",
                    "宋体");
        }
        return inputStream;
    }

    @Override
    public BusBpmnInfo getByBpmnType(Integer type) {
        return busBpmnInfoMapper.getByBpmnType(type);
    }

    @Override
    public void deploy(MultipartFile file, String bpmnLabel, Integer bpmnType, String info) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extName=originalFilename.substring(originalFilename.indexOf(".")+1);
        //只能上传bpmn格式的流程文件
        Assert.state("bpmn".equalsIgnoreCase(extName),"只能上传bpmn格式的流程文件");
        //部署到activities
        Deployment deploy = repositoryService.createDeployment().
                addInputStream(originalFilename, file.getInputStream()).
                deploy();
        // 查询流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().
                deploymentId(deploy.getId()).singleResult();
        //存储到bpmnInfo表
        BusBpmnInfo busBpmnInfo=new BusBpmnInfo();
        busBpmnInfo.setBpmnLabel(bpmnLabel);
        busBpmnInfo.setBpmnType(bpmnType);
        busBpmnInfo.setProcessDefinitionKey(processDefinition.getKey());
        busBpmnInfo.setVersion(processDefinition.getVersion());
        busBpmnInfo.setDeployTime(DateUtils.getNowDate());
        busBpmnInfo.setInfo(info);
        busBpmnInfoMapper.insertBusBpmnInfo(busBpmnInfo);
    }
}
