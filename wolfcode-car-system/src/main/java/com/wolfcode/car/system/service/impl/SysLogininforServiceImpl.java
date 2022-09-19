package com.wolfcode.car.system.service.impl;

import com.wolfcode.car.system.domain.SysLogininfor;
import com.wolfcode.car.system.mapper.SysLogininfoMapper;
import com.wolfcode.car.system.service.ISysLogininforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysLogininforServiceImpl implements ISysLogininforService {
    @Autowired
    private SysLogininfoMapper sysLogininfoMapper;
    @Override
    public void insertLogininfor(SysLogininfor logininfor) {
        sysLogininfoMapper.insertLogininfor(logininfor);
    }

    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    @Override
    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor)
    {
        return sysLogininfoMapper.selectLogininforList(logininfor);
    }

    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    @Override
    public int deleteLogininforByIds(Long[] infoIds)
    {
        return sysLogininfoMapper.deleteLogininforByIds(infoIds);
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLogininfor()
    {
        sysLogininfoMapper.cleanLogininfor();
    }
}
