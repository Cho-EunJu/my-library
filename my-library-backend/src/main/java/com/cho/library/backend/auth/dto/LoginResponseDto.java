package com.cho.library.backend.auth.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LoginResponseDto {
    private String jwt;
    private Long userId;
    private LoginUserDto user;
}
