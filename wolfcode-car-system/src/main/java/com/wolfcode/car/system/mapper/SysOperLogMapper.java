package com.wolfcode.car.system.mapper;

import com.wolfcode.car.system.domain.SysOperLog;

import java.util.List;

/**
 * 操作日志 数据层
 */
public interface SysOperLogMapper {

    /**
     * 新增操作日志
     * @param operLog 操作日志对象
     */
    void insertOperLog(SysOperLog operLog);

    /**
     * 查询系统操作日志集合
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    List<SysOperLog> selectOperLogList(SysOperLog operLog);

    /**
     * 批量删除系统操作日志
     * @param operIdes 需要删除的操作日志的ID
     * @return 删除条数
     */
    int deleteOperLogByIds(Long[] operIdes);

    /**
     * 查询操作日志详细
     * @param operId 操作ID
     * @return 操作日志对象
     */
    SysOperLog selectOperLogById(Long operId);

    /**
     * 清空操作日志
     */
    void cleanOperLog();
}
