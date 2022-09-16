package com.wolfcode.car.system.mapper;

import com.wolfcode.car.system.domain.SysLogininfor;

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
}
