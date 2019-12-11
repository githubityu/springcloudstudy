package com.ityu.studyadmin.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 文档配置
 *
 * @author lihe
 */
public abstract class BaseSwaggerConfig {


    private final static String TOKEN = "token";
    private final static String DEVICE_NO = "deviceNo";
    private final static String USER_ID = "userId";

    private final static String PASS_AS = "header";



    /**
     * 子类用于自定义接口参数
     *
     * @return api 配置
     */
    protected abstract ApiInfo apiInfo();


    @Bean
    public Docket createRestApi() {
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
//        Parameter parameter = ticketPar.name("Authorization").description("用户信息")
//                .modelRef(new ModelRef("string")).parameterType("header")
//                .required(false).build();//header中的ticket参数非必填，传空也可以
//        pars.add(parameter);    //根据每个方法名也知道当前方法在设置什么参数

        List<SecurityContext> data = new ArrayList();
        data.add(securityContext());
        return new Docket(DocumentationType.SWAGGER_2)
                .securitySchemes(security())
                .securityContexts(data)
                .apiInfo(apiInfo())
                .globalOperationParameters(pars)
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }


    private Stream<String> getKeyStream(){
        return Stream.of(TOKEN, DEVICE_NO, USER_ID);
    }


    private List<ApiKey> security() {
        return  this.getKeyStream()
                .map(x -> new ApiKey(x, x, PASS_AS))
                .collect(Collectors.toList());
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return this.getKeyStream()
                .map(x -> new SecurityReference(x, authorizationScopes))
                .collect(Collectors.toList());
    }
}
