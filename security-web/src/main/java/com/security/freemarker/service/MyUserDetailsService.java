package com.security.freemarker.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.security.freemarker.dto.Users;
import com.security.freemarker.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersMapper usersMapper;

    /**
     * 获取用户基本详情
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //整个登录认证逻辑 TODO
        //调用usersMapper方法，根据用户名查询数据库
       /* QueryWrapper<Users> wrapper = new QueryWrapper();
        // where username=?
        wrapper.eq("username",username);*/
        //Users users = usersMapper.selectOne(wrapper);
        Users users = new Users();
        users.setId(1);
        users.setUsername("admin");
        users.setPassword("123456");
        //判断
        if (users == null) {
            System.out.println("用户不存在！");
            throw new UsernameNotFoundException("用户不存在！");
        }
        List<GrantedAuthority> auths =
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin,admins,ROLE_sale");
        //从查询数据库返回users对象，得到用户名和密码，返回
        return new User(users.getUsername(),
                new BCryptPasswordEncoder().encode(users.getPassword()), auths);
    }


}
