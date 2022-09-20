package com.wolfcode.car.common.core.service.impl;

import com.wolfcode.car.common.core.redis.RedisCache;
import com.wolfcode.car.common.core.service.SmsService;
import com.wolfcode.car.common.utils.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * 短信发送实现类
 */
@Service
public class SmsServiceImpl implements SmsService {
    @Autowired
    private RedisCache redisCache;
    @Override
    public void sendCaptcha(String phone, String userType, int timeout) {
        //校验手机号码是否合法
        AssertUtils.checkPhone(phone);
        Assert.hasLength(userType,"请输入用途");
        String key ="SMS_CODE:".concat(userType).concat(phone);
        redisCache.setCacheObject(key,"666666",timeout, TimeUnit.SECONDS);
    }

    @Override
    public void checkCaptcha(String phone, String userType, String verifyCode) {
        //校验手机号码是否合法
        AssertUtils.checkPhone(phone);
        Assert.hasLength(userType,"请输入用途");
        Assert.hasLength(verifyCode,"请输入验证码");

        //获取验证码
        String key ="SMS_CODE:".concat(userType).concat(phone);
        String cacheCode = redisCache.getCacheObject(key);

        //校验验证码
        Assert.hasLength(cacheCode,"验证码已失效");
        Assert.isTrue(cacheCode.equals(verifyCode),"验证码错误");
    }
}
