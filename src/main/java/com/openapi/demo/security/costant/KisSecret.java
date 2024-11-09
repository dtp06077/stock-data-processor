package com.openapi.demo.security.costant;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KisSecret {

    @Value("${kis.appkey}")
    private String APPKEY;
    @Value("${kis.appsecret}")
    private String APPSECRET;
}
