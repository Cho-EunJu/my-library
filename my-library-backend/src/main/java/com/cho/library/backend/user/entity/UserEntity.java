package com.cho.library.backend.user.entity;

import com.cho.library.backend.auth.model.Provider;
import com.cho.library.backend.cmmn.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/*
* 운영 환경에서는 DBA가 직접 인덱스, key 관리 → @Table 최소한의 설정(또는 테이블명 정도) 유지
* */
@Entity
@Table(
        name = "user",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_email", columnNames = {"email"}) // (email, provider) 조합 유니크
        },
        indexes = {
                @Index(name = "idx_user_email", columnList = "email"),
                @Index(name = "idx_user_provider_providerId", columnList = "provider,provider_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 이메일 (로그인 시 사용) */
    @Column(length = 200, nullable = false, unique = true)
    private String email;

    @Column(length = 100)
    private String password;

    /** 로그인 제공자 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Provider provider;

    /** 제공자 내부 식별자 (구글의 sub, 카카오 id 등) */
    @Column(length = 100, name = "provider_id")
    private String providerId;

    /** 닉네임 — 소셜 첫 가입 시 null 가능, 이후 프로필 단계에서 수집 */
    @Column(length = 50, name = "nick_name")
    private String nickName;


    /** 권한(간단 버전) */
    @Column(length = 20)
    private String role; // ex) ROLE_USER, ROLE_ADMIN

    /** 마지막 로그인 시각 */
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    /** 포인트/레벨 같은 게임화 요소를 나중에 확장하기 위한 필드 */
    @Column(nullable = false)
    private Integer point;

    @Column(nullable = false)
    private Integer level;
}
