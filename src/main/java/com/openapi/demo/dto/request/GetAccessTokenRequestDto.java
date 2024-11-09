package com.openapi.demo.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetAccessTokenRequestDto {
    //body 필드
    private String grant_type;
    private String appkey;
    private String appsecret;
}
