package com.terbuck.terbuck_be.domain.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

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

    @Value("${apple.team-id}")
    private String teamId;

    @Value("${apple.key-id}")
    private String keyId;

    @Value("${apple.private-key-path}")
    private String privateKeyPath;

    @Value("${apple.token-uri}")
    private String tokenUri;

    public UserInfo getAppleUserInfo(String authorizationCode, String name) {

        String clientSecret = generateClientSecret();
        log.info("clientSecret : {}", clientSecret);

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

    private String generateClientSecret() {

        String privateKey = loadPrivateKeyFromFile();

        log.info("privateKey : {}", privateKey);

        try {
            Algorithm algorithm = Algorithm.ECDSA256(
                    null,
                    (ECPrivateKey) KeyFactory.getInstance("EC")
                            .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey.replaceAll("-----\\w+ PRIVATE KEY-----", "").replaceAll("\\s", ""))))
            );

            long now = Instant.now().getEpochSecond();
            long exp = now + 60 * 60 * 24 * 180; // 6개월

            return JWT.create()
                    .withKeyId(keyId)
                    .withIssuer(teamId)
                    .withIssuedAt(new Date(now * 1000))
                    .withExpiresAt(new Date(exp * 1000))
                    .withAudience("https://appleid.apple.com")
                    .withSubject(clientId)
                    .sign(algorithm);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String loadPrivateKeyFromFile() {
        try {
            return Files.readString(Paths.get(privateKeyPath));
        } catch (IOException e) {
            throw new RuntimeException("Apple private key 파일을 읽을 수 없습니다.", e);
        }
    }
}
