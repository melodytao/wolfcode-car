package com.wolfcode.car.system.service.impl;

import com.wolfcode.car.system.domain.SysLogininfor;
import com.wolfcode.car.system.mapper.SysLogininfoMapper;
import com.wolfcode.car.system.service.ISysLogininforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysLogininforServiceImpl implements ISysLogininforService {
    @Autowired
    private SysLogininfoMapper sysLogininfoMapper;
    @Override
    public void insertLogininfor(SysLogininfor logininfor) {
        sysLogininfoMapper.insertLogininfor(logininfor);
    }
}
