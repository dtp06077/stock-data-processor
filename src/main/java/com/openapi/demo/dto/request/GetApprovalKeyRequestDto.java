package com.openapi.demo.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetApprovalKeyRequestDto {
    //body 필드
    private String grant_type = "client_credentials";
    private String appkey;
    private String secretkey;
}
