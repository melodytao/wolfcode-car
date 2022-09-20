package com.wolfcode.car.common.core.service;

/**
 *  短信发送服务
 *  先存在redis，后面接入短信服务
 */
public interface SmsService {

    /**
     * 发送验证码
     * @param phone  手机号码
     * @param userType 用途
     * @param timeout 过期时间 秒
     */
    void sendCaptcha(String phone,String userType,int timeout);

    /**
     *  检查验证码
     * @param phone 手机号码
     * @param userType 用途
     * @param verifyCode 验证码
     */
    void checkCaptcha(String phone,String userType,String verifyCode);
}
