package com.security.jwt.controller;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.security.jwt.security.JwtAuthenticatioToken;
import com.security.jwt.utils.SecurityUtils;
import com.security.jwt.vo.HttpResult;
import com.security.jwt.vo.LoginBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * 登录控制器
 * https://www.w3cschool.cn/springsecurity/
 * https://www.springcloud.cc/spring-security-zhcn.html#what-is-acegi-security
 */
@RestController
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 登录接口
     */
    @PostMapping(value = "/login")
    public HttpResult login(@RequestBody LoginBean loginBean, HttpServletRequest request) throws IOException {
        String username = loginBean.getUsername();
        String password = loginBean.getPassword();
        /**
         *  AuthenticationManager 的 authenticate(Authentication authentication) 方法，最终返回认证成功的 Authentication 实现类并存储到SpringContexHolder
         *  上下文即可，这样后面授权的时候就可以从 SpringContexHolder 中获取登录认证信息，并根据其中的用户信息和权限信息决定是否进行授权。
         */
        // 系统登录认证
        JwtAuthenticatioToken token = SecurityUtils.login(request, username, password, authenticationManager);
                
        return HttpResult.ok(token);
    }

}