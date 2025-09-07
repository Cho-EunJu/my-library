package com.cho.library.backend.auth.controller;

import com.cho.library.backend.auth.dto.LoginRequestDto;
import com.cho.library.backend.auth.dto.LoginResponseDto;
import com.cho.library.backend.auth.dto.SignUpRequestDto;
import com.cho.library.backend.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Value("${app.front-base-url}")
    private String frontBaseUrl;

    @PostMapping("/oauth2/{provider}")
    public Map<String, String> redirectToProvider(@PathVariable String provider) {

        ClientRegistration registration = clientRegistrationRepository.findByRegistrationId(provider);

        if (registration == null) {
            throw new IllegalArgumentException("지원하지 않는 로그인 제공자: " + provider);
        }

        String redirectUri = registration.getRedirectUri()
                .replace("{baseUrl}", frontBaseUrl)
                .replace("{registrationId}", registration.getRegistrationId());

        String redirectUrl = registration.getProviderDetails().getAuthorizationUri()
                + "?client_id=" + registration.getClientId()
                + "&redirect_uri=" + redirectUri
                + "&response_type=code"
                + "&scope=" + String.join(" ", registration.getScopes());

        return Map.of("redirectUrl", redirectUrl);
    }

    /** 2) 프론트 콜백에서 code 받아 토큰 교환 & 로그인 처리 (google/naver/kakao 공통) */
    @PostMapping("/oauth2/cb/{provider}")
    public LoginResponseDto googleCallback(@PathVariable String provider, @RequestBody LoginRequestDto reqDto) {
        return authService.processLogin(provider, reqDto.getCode());
    }

    @PostMapping("/sign-up")
    public LoginResponseDto signUp(@RequestBody SignUpRequestDto reqDto) {
        return authService.signUp(reqDto);
    }

    @PostMapping("/dup-email")
    public ResponseEntity<String> duplicationEmail(@RequestBody SignUpRequestDto reqDto){
        return authService.duplicationCheckForEmail(reqDto.getEmail());
    }
}
