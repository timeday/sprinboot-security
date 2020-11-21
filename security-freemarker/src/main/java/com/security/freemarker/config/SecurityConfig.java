package com.security.freemarker.config;

import com.security.freemarker.authentication.DemoAuthenticationFailureHandler;
import com.security.freemarker.authentication.DemoAuthenticationSuccessHandler;
import com.security.freemarker.filter.SmsCodeFilter;
import com.security.freemarker.filter.ValidateCodeFilter;
import com.security.freemarker.properties.CodeProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DemoAuthenticationSuccessHandler demoAuthenticationSuccessHandler;

    @Autowired
    private DemoAuthenticationFailureHandler demoAuthenticationFailureHandler;


    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setDemoAuthenticationFailureHandler(demoAuthenticationFailureHandler);

        SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
        smsCodeFilter.setDemoAuthenticationFailureHandler(demoAuthenticationFailureHandler);

        http.addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)//在UsernamePasswordAuthenticationFilter添加新添加的拦截器
                .formLogin()//表示使用form表单提交
                .loginPage("/login")//我们定义的登录页
                .loginProcessingUrl("/authentication/form")//因为SpringSecurity默认是login请求为登录请求，所以需要配置自己的请求路径
                .successHandler(demoAuthenticationSuccessHandler)//登录成功的操作
                .failureHandler(demoAuthenticationFailureHandler)//登录失败的操作
                .and()
                    .logout()
                    .logoutUrl("/logout")
                .and()
                .authorizeRequests()//对请求进行授权
                .antMatchers("/login", "/code/*", "/user/regist", "/signup").permitAll()//表示login.html路径不会被拦截
                .anyRequest()//表示所有请求
                .authenticated()//需要权限认证
                .and()
                .csrf().disable()
                //短信
                .apply(smsCodeAuthenticationSecurityConfig);//这是SpringSecurity的安全控制，我们这里先关掉

    }
}
