package com.wolfcode.car.common.utils;

import com.wolfcode.car.common.exception.BusinessException;
import org.apache.poi.ss.formula.functions.T;

import java.util.Date;
import java.util.List;

/**
 * 断言工具
 */
public class AssertUtils {

    /**
     * @param status
     * @param expectStatus
     */
    public static void checkStatus(int status,int expectStatus,String msg){
        if(status!=expectStatus){
            throw new BusinessException(msg);
        }
    }

    /**
     * @param status
     * @param expectStatus
     */
    public static void checkStatus(int status,int expectStatus){
        if(status!=expectStatus){
            throw new BusinessException("非法参数");
        }
    }

    /**
     * @param date
     * @param expect
     */
    public static void checkAppointmentDate(Date date, Date expect){
        // 假如传进的预约日期，在今天之前，校验不通过
        if(date.before(expect)){
            throw new BusinessException("不能预约当日之前时间");
        }
    }

    /**
     * 校验手机号码
     * @param phone
     */
    public static void checkPhone(String phone){
        if(!PhoneUtil.isMobileNumber(phone)){
            throw new BusinessException("不是合法的手机号");
        }
    }

    /**
     * 期望状态不相等，相等抛异常
     * @param status
     * @param expectStatus
     * @param msg
     */
    public static void checkNotEqualsStatus(int status,int expectStatus,String msg){
        if(status==expectStatus){
            throw new BusinessException(msg);
        }
    }

    /**
     * 期望状态不相等，相等抛异常
     * @param status
     * @param expectStatus
     */
    public static void checkNotEqualsStatus(int status,int expectStatus){
        checkNotEqualsStatus(status,expectStatus,"非法参数");
    }




}
