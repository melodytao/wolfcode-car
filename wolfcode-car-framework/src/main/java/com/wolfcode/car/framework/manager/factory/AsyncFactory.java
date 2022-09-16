package com.wolfcode.car.framework.manager.factory;

import java.util.TimerTask;

import com.wolfcode.car.common.utils.ip.AddressUtils;
import com.wolfcode.car.common.utils.ip.IpUtils;
import com.wolfcode.car.system.domain.SysLogininfor;
import com.wolfcode.car.system.domain.SysOperLog;
import com.wolfcode.car.system.service.ISysLogininforService;
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
public class AsyncFactory {
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param msg      消息
     * @param args     参数列表
     * @return 任务task
     */
    public static TimerTask recordLogininfor(String username, String status, String msg, Object... args) {
        //获取用户代理：可以获取用户浏览器，操作系统的信息
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        return new TimerTask() {
            @Override
            public void run() {
                // 打印信息到日志
                // 地址，ip，用户名，状态，消息
                String address= AddressUtils.getRealAddressByIP(ip);
                StringBuilder sb=new StringBuilder();
                sb.append(LogUtils.getBlock(ip));
                sb.append(address);
                sb.append(LogUtils.getBlock(username));
                sb.append(LogUtils.getBlock(status));
                sb.append(LogUtils.getBlock(msg));
                sys_user_logger.info(sb.toString(),args);
                // 保存到数据库
                // 获取客户端操作系统名称
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String brower = userAgent.getBrowser().getName();
                SysLogininfor logininfor=new SysLogininfor();
                logininfor.setUserName(username);
                logininfor.setIpaddr(ip);
                logininfor.setLoginLocation(address);
                logininfor.setBrowser(brower);
                logininfor.setOs(os);
                logininfor.setMsg(msg);
                if(StringUtils.equalsAny(status,Constants.LOGIN_SUCCESS,
                        Constants.LOGOUT,Constants.REGISTER)){
                    logininfor.setStatus(Constants.SUCCESS);
                }else if(Constants.LOGIN_FAIL.equals(status)){
                    logininfor.setStatus(Constants.FAIL);
                }
                SpringUtils.getBean(ISysLogininforService.class).insertLogininfor(logininfor);
            }
        };
    }

    /**
     * 操作日志记录
     *
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
