package com.wolfcode.car.system.mapper;

import com.wolfcode.car.system.domain.SysLogininfor;

import java.util.List;

/**
 * 系统访问日志清空信息 数据层
 */
public interface SysLogininfoMapper {

    /**
     * 新增系统登录日志
     * @param logininfor
     * 访问日志对象
     */
     void insertLogininfor(SysLogininfor logininfor);

    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    List<SysLogininfor> selectLogininforList(SysLogininfor logininfor);

    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    int deleteLogininforByIds(Long[] infoIds);

    /**
     * 清空系统登录日志
     *
     * @return 结果
     */
    int cleanLogininfor();
}