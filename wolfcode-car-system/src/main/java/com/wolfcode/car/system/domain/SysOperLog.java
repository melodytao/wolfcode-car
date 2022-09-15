package com.wolfcode.car.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wolfcode.car.common.annotation.Excel;
import com.wolfcode.car.common.core.domain.BaseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@ApiModel(value = "SysOperLog",description = "日志实体")
public class SysOperLog implements Serializable {
    /**
     * 日志主键
     */
    @ApiModelProperty(value = "操作序号",hidden = true)
    @Excel(name = "操作序号",cellType = Excel.ColumnType.NUMERIC)
    private Long operId;

    /**
     * 操作模块
     */
    @ApiModelProperty("操作模块")
    @Excel(name = "操作模块")
    private String title;

    /**
     * 业务类型 (0其他 1新增 2修改 3删除 ...)
     */
    @ApiModelProperty("业务类型")
    @Excel(name = "业务类型", readConverterExp = "0=其它,1=新增,2=修改,3=删除,4=授权,5=导出,6=导入,7=强退,8=生成代码,9=清空数据")
    private Integer businessType;

    /**
     * 业务类型数组
     */
    @ApiModelProperty(value = "业务类型数组",hidden = true)
    private Integer[] businessTypes;

    /**
     * 请求方法
     */
    @Excel(name = "请求方法")
    @ApiModelProperty(value = "请求方法",hidden = true)
    private String method;

    /**
     * 请求方式
     */
    @ApiModelProperty(value = "请求方式",hidden = true)
    @Excel(name = "请求方式")
    private String requestMethod;

    /**
     * 操作类型(0其他 1后台用户 2手机端用户)
     */
    @Excel(name = "操作类型",readConverterExp = "0=其它,1=后台用户,2=手机端用户")
    @ApiModelProperty(value = "操作类型",hidden = true)
    private Integer operatorType;

    /**
     * 操作人员
     */
    @Excel(name = "操作人员")
    @ApiModelProperty("操作人员")
    private String operName;

    /**
     * 部门名称
     */
    @Excel(name = "部门名称")
    @ApiModelProperty(value = "部门名称",hidden = true)
    private String deptName;

    /**
     * 请求url
     */
    @ApiModelProperty(value = "请求地址",hidden = true)
    @Excel(name = "请求地址")
    private String operUrl;

    /**
     * 操作地址
     */
    @Excel(name = "操作地址")
    @ApiModelProperty(value = "操作地址",hidden = true)
    private String operIp;

    /**
     * 操作地点
     */
    @Excel(name = "操作地点")
    @ApiModelProperty(value = "操作地点",hidden = true)
    private String operLocation;

    /**
     * 请求参数
     */
    @Excel(name = "请求参数")
    @ApiModelProperty(value = "请求参数",hidden = true)
    private String operParam;

    /**
     * 返回参数
     */
    @Excel(name="返回参数")
    @ApiModelProperty(value = "返回参数",hidden = true)
    private String jsonResult;

    /**
     * 操作状态(0正常 1异常)
     */
    @ApiModelProperty("操作状态")
    @Excel(name = "状态",readConverterExp = "0=正常,1=异常")
    private Integer status;

    /**
     * 错误信息
     */
    @ApiModelProperty(value = "错误消息",hidden = true)
    @Excel(name = "错误消息")
    private String errorMsg;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "操作时间",width = 30,dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date operTime;

    /**
     * 请求参数
     */
    @ApiModelProperty(value = "请求参数",hidden = true)
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
