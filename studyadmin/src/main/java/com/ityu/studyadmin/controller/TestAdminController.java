package com.ityu.studyadmin.controller;

import com.ityu.common.utils.R;
import com.ityu.common.utils.RUtil;
import com.ityu.studyadmin.client.ApiClient;
import com.ityu.studyadmin.utils.SecurityUtils;
import feign.HeaderMap;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin")
@Slf4j
public class TestAdminController {

    @Autowired
    ApiClient apiClient;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    HttpServletRequest servletRequest;

    @ApiOperation(value = "测试調用接口")
    @ApiImplicitParams({ // 参数说明
            @ApiImplicitParam(name = "data", paramType = "query", value = "加密后的数据", dataType = "string"),
    })
    @PostMapping(value = "/test")
    public R<String> test(@RequestParam(value = "data") String data) {
        return RUtil.success("admin" + data);
    }


    @ApiOperation(value = "调用其他模块接口测试")
    @ApiImplicitParams({ // 参数说明
            @ApiImplicitParam(name = "data", paramType = "query", value = "加密后的数据", dataType = "string"),
    })
    @PostMapping(value = "/feign")
    public R<String> testApi(@RequestParam(value = "data") String data) {
        String header = servletRequest.getHeader("token");
        System.out.println(header);
        return apiClient.test(data);
    }

    @ApiOperation(value = "测试BCryptPasswordEncoder加密")
    @ApiImplicitParams({ // 参数说明
            @ApiImplicitParam(name = "data", paramType = "query", value = "需要加密的数据", dataType = "string"),
    })
    @PostMapping(value = "/encoder")
    public R<String> encoder(@RequestParam(value = "data") String data) {
        String encode = bCryptPasswordEncoder.encode(data);
        return RUtil.success(encode);
    }

    @ApiOperation(value = "测试BCryptPasswordEncoder 密码是否正确")
    @ApiImplicitParams({ // 参数说明
            @ApiImplicitParam(name = "data", paramType = "query", value = "需要解密的数据", dataType = "string"),
            @ApiImplicitParam(name = "encodeData", paramType = "query", value = "需要解密的数据", dataType = "string"),
    })
    @PostMapping(value = "/matches")
    public R<String> matches(@RequestParam(value = "data") String data, @RequestParam("encodeData") String encodeData) {
        boolean matches = bCryptPasswordEncoder.matches(data, encodeData);
        return RUtil.success(matches);
    }

    @ApiOperation(value = "测试权限接口hasAuthority")
    @PostMapping(value = "/testPreAuthorize")
    @PreAuthorize("hasAuthority('admin')")
    public R<String> testPreAuthorize() {
        return RUtil.success("admin" + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }


    @ApiOperation(value = "测试权限接口")
    @PostMapping(value = "/testPreAuthorizeRole")
    @PreAuthorize("hasRole('admin')")
    public R<String> testPreAuthorizeRole() {
        return RUtil.success("admin" + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

}
