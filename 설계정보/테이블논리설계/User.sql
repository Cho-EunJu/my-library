-- cho.`user` definition

CREATE TABLE `user`
(
    `id`            bigint                                                                                   NOT NULL AUTO_INCREMENT,
    `email`         varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci                            NOT NULL,
    `nick_name`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `role`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `level`         int                                                                                      NOT NULL,
    `point`         int                                                                                      NOT NULL,
    `provider`      enum ('GOOGLE','KAKAO','LOCAL','NAVER') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `provider_id`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci                            NOT NULL,
    `last_login_at` datetime(6)                                                  DEFAULT NULL,
    `create_at`     datetime(6)                                                  DEFAULT NULL,
    `update_at`     datetime(6)                                                  DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_email` (`email`),
    KEY `idx_user_email` (`email`),
    KEY `idx_user_provider_providerId` (`provider`, `provider_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;