package com.cho.library.backend.auth.service;

import com.cho.library.backend.auth.dto.LoginResponseDto;
import com.cho.library.backend.auth.model.Provider;
import com.cho.library.backend.auth.util.JwtUtil;
import com.cho.library.backend.user.entity.UserEntity;
import com.cho.library.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2Service{

    @Value("${app.front-base-url}")
    private String frontBaseUrl;

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    /**
     * 공통 처리:
     * 1) ClientRegistration 조회
     * 2) code -> access_token 교환
     * 3) access_token -> userinfo 조회
     * 4) DB 조회/신규 저장
     * 5) JWT 발급 + 응답
     */
    public LoginResponseDto processLogin(String providerStr, String code) {

        Provider provider = Provider.valueOf(providerStr.toUpperCase());
        ClientRegistration reg = getRegistrationOrThrow(providerStr);

        // 1) redirect_uri 템플릿 치환
        String redirectUri = reg.getRedirectUri()
                .replace("{baseUrl}", frontBaseUrl)
                .replace("{registrationId}", reg.getRegistrationId());

        // 2) 토큰 교환 (application/x-www-form-urlencoded) : RFC 6749 (OAuth2 표준 스펙) 에 정의된 방식
        Map<String, Object> tokenResponse = exchangeCodeForToken(reg, code, redirectUri);
        String accessToken = (String) tokenResponse.get("access_token");

        // 3) 사용자 정보 조회
        Map<String, Object> userInfo = fetchUserInfo(reg, accessToken);

        // 4) provider 별로 email, providerId 추출
        String email = extractEmail(provider, userInfo);
        String providerId = extractProviderId(provider, userInfo);

        // 5) 사용자 조회 or 신규 생성
        UserEntity user = findOrCreateUser(provider, email, providerId);

        // 6) 마지막 로그인 시각 갱신
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        // 7) JWT 발급
        String jwt = jwtUtil.createToken(user.getId(), user.getEmail(), user.getRole());

        boolean needProfile = (user.getUserName() == null || user.getUserName().isBlank())
                || (user.getNickName() == null || user.getNickName().isBlank());

        return LoginResponseDto.builder()
                .jwt(jwt)
                .userId(user.getId())
                .email(user.getEmail())
                .userName(user.getUserName())
                .nickName(user.getNickName())
                .needProfile(needProfile)
                .lastLoginAt(user.getLastLoginAt())
                .build();

    }

    private ClientRegistration getRegistrationOrThrow(String provider) {
        ClientRegistration reg = clientRegistrationRepository.findByRegistrationId(provider);
        if (reg == null) throw new IllegalArgumentException("지원하지 않는 provider: " + provider);
        return reg;
    }

    private Map<String, Object> exchangeCodeForToken(ClientRegistration reg, String code, String redirectUri) {
        // Spring 6+ RestClient 사용 (RestTemplate보다 최신)
        RestClient client = RestClient.create();

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("code", code);
        form.add("client_id", reg.getClientId());
        form.add("client_secret", reg.getClientSecret());
        form.add("redirect_uri", redirectUri);
        form.add("grant_type", "authorization_code");

        // 토큰 엔드포인트 (registration에 정의됨)
        String tokenUri = reg.getProviderDetails().getTokenUri();

        return client.post()
                .uri(tokenUri)
                .contentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    private Map<String, Object> fetchUserInfo(ClientRegistration reg, String accessToken) {
        RestClient client = RestClient.create();

        // Bearer 토큰으로 호출
        return client.get()
                .uri(uriBuilder -> UriComponentsBuilder
                        .fromUriString(reg.getProviderDetails().getUserInfoEndpoint().getUri())
                        .build(true)
                        .toUri())
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    private String extractEmail(Provider provider, Map<String, Object> attributes) {
        // provider별 JSON 구조가 다르므로 분기
        return switch (provider) {
            case GOOGLE -> (String) attributes.get("email");
            case NAVER -> {
                Map<String, Object> res = (Map<String, Object>) attributes.get("response");
                yield res != null ? (String) res.get("email") : null;
            }
            case KAKAO -> {
                Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
                yield account != null ? (String) account.get("email") : null;
            }
            case LOCAL -> null;
        };
    }

    private String extractProviderId(Provider provider, Map<String, Object> attributes) {
        return switch (provider) {
            case GOOGLE -> (String) attributes.get("id"); // 구글 v2 userinfo에는 "id" 또는 "sub" 케이스. 둘 다 확인 필요
            case NAVER -> {
                Map<String, Object> res = (Map<String, Object>) attributes.get("response");
                yield res != null ? (String) res.get("id") : null;
            }
            case KAKAO -> String.valueOf(attributes.get("id"));
            case LOCAL -> null;
        };
    }

    private UserEntity findOrCreateUser(Provider provider, String email, String providerId) {

        // 1) (email, provider)로 먼저 조회
        Optional<UserEntity> existing = userRepository.findByEmailAndProvider(email,provider);
        if(existing.isPresent()) return existing.get();

        // 2) (provider, providerId)로도 조회 (이메일 미제공 케이스 대비)
        if(providerId != null){
            Optional<UserEntity> byPid = userRepository.findByProviderAndProviderId(provider, providerId);
            if(byPid.isPresent()) return byPid.get();
        }

        UserEntity user = UserEntity.builder()
                .email(email)
                .userName(null)
                .nickName(null)
                .provider(provider)
                .providerId(providerId)
                .role("ROLE_USER")
                .point(0)
                .level(1)
                .lastLoginAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }
}
