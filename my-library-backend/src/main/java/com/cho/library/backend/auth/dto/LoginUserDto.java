package com.cho.library.backend.auth.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LoginUserDto {

    private String userName;
    private String nickName;
    private String email;
    private LocalDateTime lastLoginAt;
    private boolean needProfile;
}
