package com.wolfcode.car.flowdefinition.service;

import com.wolfcode.car.flowdefinition.domain.BusBpmnInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 流程定义明细Service接口
 * 
 * @author wolfcode
 * @date 2022-09-26
 */
public interface IBusBpmnInfoService 
{
    /**
     * 查询流程定义明细
     * 
     * @param id 流程定义明细主键
     * @return 流程定义明细
     */
    public BusBpmnInfo selectBusBpmnInfoById(Long id);

    /**
     * 查询流程定义明细列表
     * 
     * @param busBpmnInfo 流程定义明细
     * @return 流程定义明细集合
     */
    public List<BusBpmnInfo> selectBusBpmnInfoList(BusBpmnInfo busBpmnInfo);

    /**
     * 新增流程定义明细
     * 
     * @param busBpmnInfo 流程定义明细
     * @return 结果
     */
    public int insertBusBpmnInfo(BusBpmnInfo busBpmnInfo);

    /**
     * 修改流程定义明细
     * 
     * @param busBpmnInfo 流程定义明细
     * @return 结果
     */
    public int updateBusBpmnInfo(BusBpmnInfo busBpmnInfo);

    /**
     * 批量删除流程定义明细
     * 
     * @param ids 需要删除的流程定义明细主键集合
     * @return 结果
     */
    public int deleteBusBpmnInfoByIds(Long[] ids);

    /**
     * 删除流程定义明细信息
     * 
     * @param id 流程定义明细主键
     * @return 结果
     */
    public int deleteBusBpmnInfoById(Long id);


    /**
     * 返回不同输入流
     * @param id bpmn_info 的主键id
     * @param type xml还是png...
     * @return
     */
    public InputStream getFileInputStream(Long id,String type);


    /**
     * 返回流程定义信息
     * @param type 流程定义类型
     * @return
     */
    public BusBpmnInfo getByBpmnType(Integer type);

    public void deploy(MultipartFile file,String bpmnLabel,Integer bpmnType,String info) throws IOException;;
}
