package com.security.freemarker.config;

import com.security.freemarker.authentication.AccessDeniedAuthenticationHandler;
import com.security.freemarker.authentication.DemoAuthenticationFailureHandler;
import com.security.freemarker.authentication.DemoAuthenticationSuccessHandler;
import com.security.freemarker.authentication.MyLogoutSuccessHandler;
import com.security.freemarker.filter.SmsCodeFilter;
import com.security.freemarker.filter.ValidateCodeFilter;
import com.security.freemarker.properties.CodeProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DemoAuthenticationSuccessHandler demoAuthenticationSuccessHandler;

    @Autowired
    private DemoAuthenticationFailureHandler demoAuthenticationFailureHandler;

    /* 没有权限*/
    @Autowired
    private AccessDeniedAuthenticationHandler accessDeniedAuthenticationHandler;

    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    //注入数据源
    @Autowired
    private DataSource dataSource;

    //配置对象 记住我功能
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        //自动化创建表
        //jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

    /*短信验证码*/
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /* 配置认证*/
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


    /* 配置路径和权限 各种拦截器*/
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //退出
        http.logout().logoutUrl("/logout").logoutSuccessHandler(myLogoutSuccessHandler)
                /*logoutSuccessUrl("/hello")*/.permitAll();

        /* 图片验证码*/
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setDemoAuthenticationFailureHandler(demoAuthenticationFailureHandler);

        /* 手机验证码 */
        SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
        smsCodeFilter.setDemoAuthenticationFailureHandler(demoAuthenticationFailureHandler);

        http.addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)//在UsernamePasswordAuthenticationFilter添加新添加的拦截器
                .formLogin()
                //指定登录页的路径
                .loginPage("/login")
                //指定自定义form表单请求的路径
                .loginProcessingUrl("/authentication/form")
                /*.failureUrl("/login?error")
                .defaultSuccessUrl("/success")*/
                .successHandler(demoAuthenticationSuccessHandler)//登录成功的操作
                .failureHandler(demoAuthenticationFailureHandler)//登录失败的操作
                .and()
                .authorizeRequests()//对请求进行授权
                .antMatchers("/login", "/code/*", "/success", "/unauth").permitAll()//表示login.html路径不会被拦截
                .anyRequest()//表示所有请求
                .authenticated()//需要权限认证
                /* 无权限处理*/
                .and().exceptionHandling().accessDeniedHandler(accessDeniedAuthenticationHandler)
                .and().rememberMe().tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(60)//设置有效时长，单位秒
                .and()
                .csrf().disable()
                //短信
                .apply(smsCodeAuthenticationSecurityConfig);//这是SpringSecurity的安全控制，我们这里先关掉

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

}
