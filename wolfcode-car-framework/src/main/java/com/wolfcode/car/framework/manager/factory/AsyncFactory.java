package com.wolfcode.car.framework.manager.factory;

import java.util.TimerTask;

import com.wolfcode.car.common.utils.ip.AddressUtils;
import com.wolfcode.car.common.utils.ip.IpUtils;
import com.wolfcode.car.system.domain.SysOperLog;
import com.wolfcode.car.system.service.ISysOperLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wolfcode.car.common.constant.Constants;
import com.wolfcode.car.common.utils.LogUtils;
import com.wolfcode.car.common.utils.ServletUtils;
import com.wolfcode.car.common.utils.StringUtils;
import com.wolfcode.car.common.utils.spring.SpringUtils;
import eu.bitwalker.useragentutils.UserAgent;

/**
 * 异步工厂（产生任务用）
 * 
 * @author ruoyi
 */
public class AsyncFactory
{
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");


    /**
     * 操作日志记录
     * @param operLog 操作日志信息
     * @return
     */
    public static TimerTask recordOper(SysOperLog operLog) {

        return new TimerTask() {
            @Override
            public void run() {
                // 远程查询操作地点
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                // 保存到数据库
                SpringUtils.getBean(ISysOperLogService.class).insertOperLog(operLog);
            }
        };

    }
}
