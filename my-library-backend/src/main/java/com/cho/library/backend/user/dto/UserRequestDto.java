package com.cho.library.backend.user.dto;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private String email;
    private String nickName;
    private String role;
    private int level;
    private int point;
    private String password;

}
