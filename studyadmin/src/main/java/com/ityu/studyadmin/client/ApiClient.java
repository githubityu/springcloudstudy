package com.ityu.studyadmin.client;

import com.ityu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "study-api", fallback = ApiClientImpl.class)
@Component
public interface ApiClient {
    //, @RequestHeader(value = "token", required = false) String token
    @PostMapping(value = "/api/test")
    R<String> test(@RequestParam(value = "data") String data);
}
