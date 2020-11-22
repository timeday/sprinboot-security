package com.security.freemarker.authentication;

import com.security.freemarker.dto.Users;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

/**
 * 自定义验证工具
 */
public class MyauthenticationProvider extends DaoAuthenticationProvider {

    public MyauthenticationProvider(UserDetailsService userDetailsService) {
        //用户详情信息
        setUserDetailsService(userDetailsService);
        //密码加密方式
        setPasswordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * Description:登录认证
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        /*String status = "1";
        String userName = authentication.getName();
        String passWord = (String) authentication.getCredentials();
        Users users = new Users();
        users.setId(1);
        users.setUsername(userName);
        users.setPassword(passWord);
        *//**
         *  UsernameNotFoundException 用户找不到
         BadCredentialsException 坏的凭据
         AccountStatusException 用户状态异常它包含如下子类
         AccountExpiredException 账户过期
         LockedException 账户锁定
         DisabledException 账户不可用
         CredentialsExpiredException 证书过期
         *//*
        if (users == null) {
            System.out.println("用户不存在！");
            throw new UsernameNotFoundException("用户不存在！");
        }
        // 加密过程在这里体现,matches(CharSequence rawPassword, String encodedPassword)
		*//*if (!passwordEncoder.matches(passWord, user.getPassword())) {
			throw new BadCredentialsException("密码错误！");
		}*//*
        if (!users.getPassword().equals(passWord)) {
            System.out.println("密码错误！");
            throw new BadCredentialsException("密码错误！");
        }
        if (status.equals("2")) {
            throw new DisabledException("账号不可用，请联系系统管理员！");
        }
        if (status.equals("3")) {
            throw new LockedException("账号已锁定，请联系系统管理员！");
        }
        List<GrantedAuthority> auths =
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin,admins,ROLE_sale");
        return new UsernamePasswordAuthenticationToken(users, passWord, auths);*/
        // 可以在此处覆写整个登录认证逻辑
        return super.authenticate(authentication);
    }
    public boolean supports(Class<?> arg0) {
        return true;
    }
}