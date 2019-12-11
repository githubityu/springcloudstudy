package com.ityu.studyadmin.config;

import com.alibaba.fastjson.JSONObject;
import com.ityu.common.utils.JwtTokenUtil;
import com.ityu.common.utils.RUtil;
import com.ityu.studyadmin.domain.AdminUser;
import com.ityu.studyadmin.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
public class CustomHandlerConfiguration {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    /**
     * 访问接入点处理
     *
     * @return
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        AuthenticationEntryPoint entryPoint = (request, response, e) -> {
            //NO_LOGIN
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(JSONObject.toJSONString(RUtil.error(-1, e.getMessage())));
        };
        return entryPoint;
    }

    /**
     * 接入过后问题处理
     *
     * @return
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        AccessDeniedHandler accessDeniedHandler = (request, response, e) -> {
            //NO_PERMISSION
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(JSONObject.toJSONString(RUtil.error(-1, e.getMessage())));
        };
        return accessDeniedHandler;
    }

    /**
     * 登录成功后的处理
     *
     * @return
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        AuthenticationSuccessHandler authenticationSuccessHandler = (request, response, authentication) -> {
            //返回数据
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            String s = JwtTokenUtil.TOKEN_PREFIX+jwtTokenUtil.generateToken(SecurityUtils.getCurrentUser());
            response.getWriter().write(JSONObject.toJSONString(RUtil.success(AdminUser.getInstance().setToken(s))));
        };
        return authenticationSuccessHandler;
    }

    /**
     * 登录失败后的处理
     *
     * @return
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        AuthenticationFailureHandler authenticationFailureHandler = (request, response, e) -> {
            //ACCOUNT_ERROR
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(JSONObject.toJSONString(RUtil.error(-1, e.getMessage())));
        };
        return authenticationFailureHandler;
    }

    /**
     * 登出成功后的处理
     *
     * @return
     */
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        LogoutSuccessHandler logoutSuccessHandler = (request, response, authentication) -> {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(JSONObject.toJSONString(RUtil.success("退出成功")));
        };
        return logoutSuccessHandler;
    }
}
