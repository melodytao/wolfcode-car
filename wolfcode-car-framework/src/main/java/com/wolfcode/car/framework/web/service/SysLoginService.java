package com.wolfcode.car.framework.web.service;

import javax.annotation.Resource;

import com.wolfcode.car.common.core.domain.entity.SysUser;
import com.wolfcode.car.common.core.domain.model.LoginUser;
import com.wolfcode.car.common.core.redis.RedisCache;
import com.wolfcode.car.framework.manager.AsyncManager;
import com.wolfcode.car.system.service.ISysConfigService;
import com.wolfcode.car.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.wolfcode.car.common.constant.CacheConstants;
import com.wolfcode.car.common.constant.Constants;
import com.wolfcode.car.common.exception.ServiceException;
import com.wolfcode.car.common.exception.user.CaptchaException;
import com.wolfcode.car.common.exception.user.CaptchaExpireException;
import com.wolfcode.car.common.exception.user.UserPasswordNotMatchException;
import com.wolfcode.car.common.utils.DateUtils;
import com.wolfcode.car.common.utils.MessageUtils;
import com.wolfcode.car.common.utils.ServletUtils;
import com.wolfcode.car.common.utils.StringUtils;
import com.wolfcode.car.common.utils.ip.IpUtils;
import com.wolfcode.car.framework.manager.factory.AsyncFactory;
import com.wolfcode.car.framework.security.context.AuthenticationContextHolder;

/**
 * 登录校验方法
 * 
 * @author ruoyi
 */
@Component
public class SysLoginService
{
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    /**
     * 登录验证
     * 
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid)
    {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        // 验证码开关
        if (captchaEnabled)
        {
            validateCaptcha(username, code, uuid);
        }
        // 用户验证
        Authentication authentication = null;
        try
        {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                //TODO: 记录用户登录失败，密码不匹配日志
                throw new UserPasswordNotMatchException();
            }
            else
            {
                //TODO: 记录用户登录失败日志
                throw new ServiceException(e.getMessage());
            }
        }
        //TODO: 记录用户登录成功日志
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 校验验证码
     * 
     * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid)
    {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null)
        {
            //TODO: 记录用户登录失败，验证码过期
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            //TODO: 记录用户登录失败，验证码错误
            throw new CaptchaException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId)
    {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }
}
