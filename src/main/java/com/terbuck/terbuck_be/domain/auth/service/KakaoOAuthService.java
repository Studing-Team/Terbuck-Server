package com.terbuck.terbuck_be.domain.auth.service;

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
public class KakaoOAuthService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.token-uri}")
    private String tokenUri;

    @Value("${kakao.user-info-uri}")
    private String userInfoUri;

    @Value("${kakao.admin-key}")
    private String kakaoAdminKey;

    public String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(tokenUri, request, String.class);
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("access_token").asText();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.AUTH_KAKAO_AUTHORIZATION_CODE_NOT_FOUND);
        }
    }

    public UserInfo getKakaoUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                request,
                String.class
        );

        /**
         * TODO 나중에 심사 정보 완료되어서 이름을 가져올 수 있게 되면, 이름을 가져오는 로직 추가하여 UserInfo 에 이름도 포함하기.
         */
        try {
            JsonNode json = objectMapper.readTree(response.getBody());
            String kakaoId = json.get("id").asText();
            String nickname = json.get("properties").get("nickname").asText();
//            String email = json.get("kakao_account").get("email").asText();

            return new UserInfo(kakaoId, nickname, SocialType.KAKAO);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.AUTH_KAKAO_USERINFO_PARSING_FAIL);
        }
    }

    public void unlink(String kakaoUserId) {
        String url = "https://kapi.kakao.com/v1/user/unlink";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoAdminKey);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("target_id_type", "user_id");
        params.add("target_id", kakaoUserId);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            throw new RuntimeException("카카오 unlink 실패: " + e.getMessage(), e);
        }
    }
}
