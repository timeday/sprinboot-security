package com.security.freemarker.validcode;


import com.security.freemarker.config.excption.ValiDateCodeDataException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class ValiDateCodeFilter extends AbstractAuthenticationProcessingFilter {

    private String servletPath;

    public ValiDateCodeFilter(String servletPath, String failureUrl) {
        super(servletPath);
        this.servletPath = servletPath;
        setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(failureUrl));
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if ("POST".equalsIgnoreCase(request.getMethod()) && servletPath.equals(request.getServletPath())) {
            validate(request);
        }
        chain.doFilter(request, response);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        return null;
    }

    private void validate(HttpServletRequest request) throws ServletRequestBindingException {
        HttpSession session = request.getSession();
        ImageCode codeInSession = (ImageCode) session.getAttribute(ValidController.SESSION_KEY);

        String codeInRequest = ServletRequestUtils.getStringParameter(request, "imageCode");
        System.out.println(codeInRequest + " " + codeInSession.getCode());
        if (StringUtils.isEmpty(codeInRequest)) {
            throw new ValiDateCodeDataException("验证码不能为空");
        }

        if (codeInSession.isExpire()) {
            session.removeAttribute(ValidController.SESSION_KEY);
            throw new ValiDateCodeDataException("验证码已过期");
        }

        if (!codeInSession.getCode().toLowerCase().equals(codeInRequest.toLowerCase())) {
            throw new ValiDateCodeDataException("验证码不匹配");
        }

        session.removeAttribute(ValidController.SESSION_KEY);
    }


}
