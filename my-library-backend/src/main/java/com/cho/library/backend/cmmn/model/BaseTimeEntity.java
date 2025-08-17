package com.cho.library.backend.cmmn.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/** 생성/수정 시각 공통 상속 */
@Getter
@MappedSuperclass
public abstract class BaseTimeEntity {

    @CreationTimestamp
    @Column(updatable = false, name = "create_at")
    protected LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    protected LocalDateTime updatedAt;
}
