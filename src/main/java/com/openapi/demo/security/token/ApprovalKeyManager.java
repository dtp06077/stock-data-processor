package com.openapi.demo.security.token;


import com.openapi.demo.common.KisConstant;
import com.openapi.demo.dto.request.GetApprovalKeyRequestDto;
import com.openapi.demo.dto.response.GetApprovalKeyResponseDto;
import com.openapi.demo.security.costant.KisSecret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
public class ApprovalKeyManager {

    @Autowired
    private KisSecret kisSecret;
    private final WebClient webClient;
    private static String APPROVAL_KEY;

    public ApprovalKeyManager(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(KisConstant.REST_BASE_URL).build();
    }

    public String getApprovalKey() {
        if (APPROVAL_KEY == null) {
            APPROVAL_KEY = generateApprovalKey();
            System.out.println("generate APPROVAL_KEY: " + APPROVAL_KEY);
        }

        return APPROVAL_KEY;
    }


    public String generateApprovalKey() {

        GetApprovalKeyRequestDto requestDto = new GetApprovalKeyRequestDto();

        requestDto.setAppkey(kisSecret.getAPPKEY());
        requestDto.setSecretkey(kisSecret.getAPPSECRET());

        try {
            Mono<GetApprovalKeyResponseDto> mono = webClient.post()
                    .uri(KisConstant.GET_APPROVAL_PATH)
                    .header("content-type", "application/json")
                    .bodyValue(requestDto)
                    .retrieve()
                    .bodyToMono(GetApprovalKeyResponseDto.class);

            GetApprovalKeyResponseDto responseDto = mono.block();

            if (responseDto == null) {
                throw new RuntimeException("Can't get approval key.");
            }

            return responseDto.getApproval_key();
        } catch (WebClientResponseException e) {
            System.err.println("Error response: " + e.getResponseBodyAsString());
            throw new RuntimeException("API Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected Error: " + e.getMessage());
        }
    }
}
