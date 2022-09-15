package com.wolfcode.car.system.service.impl;

import com.wolfcode.car.system.domain.SysOperLog;
import com.wolfcode.car.system.mapper.SysOperLogMapper;
import com.wolfcode.car.system.service.ISysOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志 服务层处理
 */
@Service
public class SysOperLogServiceImpl implements ISysOperLogService {
    @Autowired
    private SysOperLogMapper sysOperLogMapper;

    /**
     * 新增操作日志
     * @param sysOperLog 操作日志对象
     */
    @Override
    public void insertOperLog(SysOperLog sysOperLog) {
        sysOperLogMapper.insertOperLog(sysOperLog);
    }

    /**
     * 查询系统操作日志集合
     * @param operLog 操作日志对象
     * @return
     */
    @Override
    public List<SysOperLog> selectOperLogList(SysOperLog operLog) {
        return sysOperLogMapper.selectOperLogList(operLog);
    }

    /**
     * 批量删除系统操作日志
     * @param operIdes 需要删除的操作日志的ID
     * @return
     */
    @Override
    public int deleteOperLogByIds(Long[] operIdes) {
        return sysOperLogMapper.deleteOperLogByIds(operIdes);
    }

    /**
     * 查询操作日志详细
     * @param operId 操作ID
     * @return
     */
    @Override
    public SysOperLog selectOperLogById(Long operId) {
        return sysOperLogMapper.selectOperLogById(operId);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog() {
        sysOperLogMapper.cleanOperLog();
    }
}
