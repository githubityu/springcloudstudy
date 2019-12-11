package com.ityu.studyadmin.config;

import com.ityu.common.utils.JwtTokenUtil;
import com.ityu.studyadmin.component.JwtPreAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import static com.ityu.studyadmin.utils.Contans.ROLE_ADMIN;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //认证处理类
    @Autowired
    private DaoAuthenticationProvider daoAuthenticationProvider;

    //认证成功
    @Autowired
    private AuthenticationSuccessHandler successHandler;

    //认证失败
    @Autowired
    private AuthenticationFailureHandler failureHandler;

    //登出成功
    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private AccessDeniedHandler deniedHandler;

    //认证EntryPoint
    @Autowired
    private AuthenticationEntryPoint entryPoint;

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.authenticationProvider(daoAuthenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/api/**")
                .antMatchers("/swagger-ui.html")
                .antMatchers("/webjars/**")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/v2/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //用户端
//        http.authorizeRequests().antMatchers("/**").permitAll()
//                .anyRequest().authenticated().and().csrf().disable();
        //后台管理
//        http.csrf().disable()
//                .exceptionHandling()
//                .authenticationEntryPoint(entryPoint)
//                .accessDeniedHandler(deniedHandler)
//                .and().authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                //不用再写登录接口了
//                .formLogin().loginPage("/api/user/login")
//                .usernameParameter("user")
//                .passwordParameter("pwd")
//                .successHandler(successHandler)
//                .failureHandler(failureHandler)
//                .and()
//                //不用再写退出登录接口了
//                .logout().logoutUrl("/api/user/logout")
//                .logoutSuccessHandler(logoutSuccessHandler)
//                .and()
//                .headers()
//                .frameOptions()
//                .disable()
//                .and().sessionManagement().maximumSessions(1800);


        http.cors()
                .and()
                // 关闭csrf
                .csrf().disable()
                // 关闭session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(deniedHandler)
                .authenticationEntryPoint(entryPoint)
                .and()
                .authorizeRequests()
                // 需要角色为ADMIN才能删除该资源
                .antMatchers(HttpMethod.DELETE, "/tasks/**").hasAnyRole(ROLE_ADMIN)
                // 测试用资源，需要验证了的用户才能访问
                .antMatchers("/tasks/**").authenticated()
                // 其他都放行了
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/api/user/login")
                .usernameParameter("user")
                .passwordParameter("pwd")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .permitAll()
                .and()
                .logout()//默认注销行为为logout
                .logoutUrl("/api/user/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                // 添加到过滤链中
                // 先是UsernamePasswordAuthenticationFilter用于login校验
                // 再通过OncePerRequestFilter，对其他请求过滤
                .addFilter(new JwtPreAuthFilter(authenticationManager(), getJwtTokenUtil()));


    }

    @Bean
    public JwtTokenUtil getJwtTokenUtil(){
        return  new JwtTokenUtil();
    }

}
