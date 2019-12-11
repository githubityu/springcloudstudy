package com.ityu.studyadmin.client;

import com.ityu.common.utils.R;
import com.ityu.common.utils.RUtil;
import org.springframework.stereotype.Component;

@Component
public class ApiClientImpl implements ApiClient {

    @Override
    public R<String> test(String data) {
        return RUtil.success("熔断器起作用了" + data);
    }
}
