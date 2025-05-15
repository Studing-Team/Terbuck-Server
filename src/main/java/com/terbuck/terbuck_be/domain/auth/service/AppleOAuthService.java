package com.terbuck.terbuck_be.domain.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terbuck.terbuck_be.common.enums.SocialType;
import com.terbuck.terbuck_be.common.exception.BusinessException;
import com.terbuck.terbuck_be.common.exception.ErrorCode;
import com.terbuck.terbuck_be.domain.auth.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleOAuthService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${apple.client-id}")
    private String clientId;

    @Value("${apple.redirect-uri}")
    private String redirectUri;

    @Value("${apple.client-secret")
    private String clientSecret;

    @Value("${apple.token-uri}")
    private String tokenUri;

    public UserInfo getAppleUserInfo(String authorizationCode, String name) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", authorizationCode);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    tokenUri,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            String idToken = jsonNode.get("id_token").asText();

            DecodedJWT decodedJWT = JWT.decode(idToken);
            String sub = decodedJWT.getSubject();

            return new UserInfo(sub, name, SocialType.APPLE);
        } catch (Exception e) {
            log.info("e : " + e);
            throw new BusinessException(ErrorCode.AUTH_APPLE_AUTHORIZATION_CODE_NOT_FOUND);
        }
    }
}
