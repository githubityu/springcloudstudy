package com.ityu.studyadmin.component;


import com.ityu.common.utils.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;



/**
 * @author: 倪明辉
 * @date: 2019/3/6 16:20
 * @description: 对所有请求进行过滤
 * BasicAuthenticationFilter继承于OncePerRequestFilter==》确保在一次请求只通过一次filter，而不需要重复执行。
 */
public class JwtPreAuthFilter extends BasicAuthenticationFilter {

    private final JwtTokenUtil jwtTokenUtil;

    public JwtPreAuthFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        super(authenticationManager);
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * description: 从request的header部分读取Token
     *
     * @param request
     * @param response
     * @param chain
     * @return void
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String tokenHeader = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        System.out.println("tokenHeader:" + tokenHeader);
        // 如果请求头中没有Authorization信息则直接放行了
        if (tokenHeader == null || !tokenHeader.startsWith(JwtTokenUtil.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        // 如果请求头中有token，则进行解析，并且设置认证信息
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
        super.doFilterInternal(request, response, chain);
    }

    /**
     * description: 读取Token信息，创建UsernamePasswordAuthenticationToken对象
     *
     * @param tokenHeader
     * @return org.springframework.security.authentication.UsernamePasswordAuthenticationToken
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
        //解析Token时将“Bearer ”前缀去掉
        String token = tokenHeader.replace(JwtTokenUtil.TOKEN_PREFIX, "");
        String username = jwtTokenUtil.getUserNameFromToken(token);
        List<String> roles = jwtTokenUtil.getUserRole(token);
        Collection<GrantedAuthority> authorities = new HashSet<>();
        if (roles != null) {
            for (String role : roles) {
                // authorities.add(new SimpleGrantedAuthority(ROLE_PRE+role));
                //hasRole('admin')

                //hasAuthority('admin')
                //@PreAuthorize("hasAuthority('ROLE_XYZ')")与@PreAuthorize("hasRole('XYZ')")相同
                authorities.add(new SimpleGrantedAuthority(role));
            }
        }
        if (username != null) {
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        }
        return null;
    }
}