package com.wolfcode.car.system.service;

import com.wolfcode.car.system.domain.SysLogininfor;

/**
 * 系统访问日志情况信息 服务层
 */
public interface ISysLogininforService {
    /**
     * 新增系统登录日志
     *
     * @param logininfor 访问日志对象
     */
    void insertLogininfor(SysLogininfor logininfor);
}
