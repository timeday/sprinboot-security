package com.springbootsecurity.demo.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @ClassName: MySecurityConfig
 * =================================================
 * @Description: SpringSecurity的配置类
 * =================================================
 */
@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 定制请求授权规则
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //所有角色都能访问
        http.authorizeRequests().antMatchers("/").permitAll()
                //订单管理员的访问权限
                .antMatchers("/page/order/**","/page/report/**","/page/customer/**").hasRole("orderManager")
                //产品管理员的访问权限
                .antMatchers("/page/product/**","/page/report/**").hasRole("productManager")
                //系统管理员的访问权限
                .antMatchers("/page/**").hasRole("systemManager")
                //登录才能访问
                .antMatchers("/main.html").authenticated();
        //开启自动配置的登录模式
        http.formLogin()
                //定制表单的名称
                .usernameParameter("userName").passwordParameter("password")
                //the URL "/login", redirecting to "/login?error" for authentication failure.
                //这里配置默认是SpringSecurity的登录页面，需要配置成自己的登录页面
                .loginPage("/")
                //定制URL处理器登录请求
                .loginProcessingUrl("/user/login")
                //登录成功后跳转的页面
                .successForwardUrl("/page/main")
                //登录失败后跳转的页面
                .failureForwardUrl("/");
        //开启自动配置的注销功能,注销请求路径/logout并注销session
        http.logout()
                //由于页面采用的是超链接get请求方式进行注销，而自动配置默认使用的post请求
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET"))
                //配置注销成功跳转的url
                .logoutSuccessUrl("/");
        //开启自动配置的记住我，这里form表单的name默认是remember-me，也可以自己定义参数名
        http.rememberMe();
    }

    /**
     * 定制认证规则
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //在内存里面校验
        auth.inMemoryAuthentication()
                //这里需要对密码进行编码不然会抛异常，详细情况可以参考错误信息及官方文档
                .passwordEncoder(new BCryptPasswordEncoder())
                //分别赋予登录的角色编码
                .withUser("admin").password(new BCryptPasswordEncoder().encode("123456")).roles("systemManager","productManager","orderManager")
                .and()
                .withUser("zhangsan").password(new BCryptPasswordEncoder().encode("123456")).roles("productManager")
                .and()
                .withUser("lisi").password(new BCryptPasswordEncoder().encode("123456")).roles("orderManager");
    }
}
