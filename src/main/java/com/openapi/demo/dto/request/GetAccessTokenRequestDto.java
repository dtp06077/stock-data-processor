package com.openapi.demo.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetAccessTokenRequestDto {
    //body 필드
    String grant_type;
    String appkey;
    String appsecret;
}
