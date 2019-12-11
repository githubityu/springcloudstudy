package com.ityu.studyapi.controller;

import com.ityu.common.utils.R;
import com.ityu.common.utils.RUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@Slf4j
@RefreshScope
public class TestController {


    @Value("${ityu}")
    private String ityu = "";

    @Autowired
    HttpServletRequest servletRequest;

    @ApiOperation(value = "测试調用接口")
    @ApiImplicitParams({ // 参数说明
            @ApiImplicitParam(name = "data", paramType = "query", value = "加密后的数据", dataType = "string"),
    })
    @PostMapping(value = "/test")
    public R<String> test(@RequestParam(value = "data") String data) {
        String header = servletRequest.getHeader("token");
        return RUtil.success(ityu + "api" + data + "=header=" + header);
    }

}
