package com.openapi.demo.dto.response;

import lombok.Data;

@Data
public class GetAccessTokenResponseDto {

    private String access_token;
    private String token_type;
    private long expires_in;
    private String access_token_token_expired;
}
