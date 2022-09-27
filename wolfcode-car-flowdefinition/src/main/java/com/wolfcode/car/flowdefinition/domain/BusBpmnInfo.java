package com.wolfcode.car.flowdefinition.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wolfcode.car.common.annotation.Excel;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 流程定义明细对象 bus_bpmn_info
 * 
 * @author wolfcode
 * @date 2022-09-26
 */
@Data
public class BusBpmnInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 流程名称 */
    @Excel(name = "流程名称")
    private String bpmnLabel;

    /** 流程类型 */
    @Excel(name = "流程类型")
    private Integer bpmnType;

    /** activity流程定义生成的key */
    @Excel(name = "activity流程定义生成的key")
    private String processDefinitionKey;

    /** 部署时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "部署时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deployTime;

    /** 版本号 */
    @Excel(name = "版本号")
    private Integer version;

    /** 描述信息 */
    @Excel(name = "描述信息")
    private String info;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createTime;
    /** 请求参数 */
    private Map<String, Object> params;
    public Map<String, Object> getParams()
    {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }
}
