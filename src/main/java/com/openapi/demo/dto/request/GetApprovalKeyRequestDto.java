package com.openapi.demo.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetApprovalKeyRequestDto {
    //body 필드
    String grant_type;
    String appkey;
    String secretkey;
}
